package com.example.harpalsingh.fabgallery.utilities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.harpalsingh.fabgallery.R;
import com.example.harpalsingh.fabgallery.activities.MainActivity;
import com.example.harpalsingh.fabgallery.constants.Constants;
import com.example.harpalsingh.fabgallery.interfaces.KeyConfig;
import com.example.harpalsingh.fabgallery.models.AllData;
import com.example.harpalsingh.fabgallery.models.PhotoData;
import com.example.harpalsingh.fabgallery.models.Photos;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.security.Key;
import java.util.List;
import java.util.Objects;

public class Utilities {

    private static final String PREFS_NAME = "FabGalleryPreferences";
    private static final String FLICKR_URL = Constants.ROOT_URL+Constants.SEARCH_PHOTO;

    public static void setupToolbarAndNavigationBar(final MainActivity mainActivity, Toolbar toolbar, NavigationView navigationView, final DrawerLayout drawerLayout, final Context context) {
        mainActivity.setSupportActionBar(toolbar);
        ActionBar actionbar = mainActivity.getSupportActionBar();
        Objects.requireNonNull(actionbar).setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;

                switch(menuItem.getItemId()){
                    case R.id.call:
                        String phone = "+1 905-867-6640";
                        intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                        context.startActivity(intent);

                        break;

                    case R.id.linkedIn:
                         intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://you"));
                        final PackageManager packageManager = mainActivity.getPackageManager();
                        final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                        if (list.isEmpty()) {
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/harpal-singh-a05271ab/"));
                        }
                        context.startActivity(intent);
                        break;
                    case R.id.email:
                        intent  = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto","harpals122@gmail.com", null));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Fab Gallery Customer Support");
                        intent.putExtra(Intent.EXTRA_TEXT, "");
                        context.startActivity(Intent.createChooser(intent, "Send email..."));
                        break;

                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    public static void showSnackBar(@Nullable String message, View snackBarParentView) {

        String messageString = message;
        if(messageString!=null)
            messageString = message;
        else
            messageString = "Something went wrong";

        Snackbar snackbar = Snackbar.make(snackBarParentView, messageString, Snackbar.LENGTH_INDEFINITE).setAction("dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        }).setActionTextColor(Color.YELLOW);
        snackbar.show();
    }


    public static void registerNetworkStateChangerReciever(MainActivity mainActivity, BroadcastReceiver networkBroadcast) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        mainActivity.registerReceiver(networkBroadcast, filter);
    }

    private static void setPreference(Context context, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("json", value);
        editor.apply();
    }

    private static String getPreference(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString("json", "");
    }

    public static void isDataSaved(Context context, Class targetedClass) {
        if (!getPreference(context).equals("")) {
            Gson gson = new Gson();
            PhotoData photoData = gson.fromJson(getPreference(context), PhotoData.class);
            Photos photos = photoData.getPhotos();
            AllData.getInstance().setPhotoData(photos);

            Activity activity = (Activity) context;
            activity.startActivity(new Intent(activity, targetedClass));
            activity.finish();
        }
    }

    public static void makeNetworkCall(final Context applicationContext, EditText editText, OAuth10aService service, OAuth1RequestToken requestToken, Class targetClass, final View snackBarParentView) {
        Activity activity = (Activity) applicationContext;
        JSONObject jsonObject;
        String oauthVerifier;
        oauthVerifier = String.valueOf(editText.getText());

        try {
            OAuth1AccessToken accessToken = service.getAccessToken(requestToken, oauthVerifier);
            final OAuthRequest request = new OAuthRequest(Verb.GET, FLICKR_URL);
            request.addQuerystringParameter("method", KeyConfig.FLICKR_PHOTOS_SEARCH);
            request.addQuerystringParameter("format", KeyConfig.FORMAT);
            request.addQuerystringParameter("nojsoncallback", KeyConfig.NOJSONCALLBACK);
            request.addQuerystringParameter("per_page", KeyConfig.PER_PAGE);
            request.addQuerystringParameter("min_taken_date", KeyConfig.MIN_DATE_TAKEN);
            request.addQuerystringParameter("privacy_filter", KeyConfig.PRIVACY_FILTER);




            service.signRequest(accessToken, request);
            Response response;
            response = service.execute(request);
            jsonObject = new JSONObject(response.getBody());

            setPreference(applicationContext, jsonObject.toString());

            Gson gson = new Gson();

            PhotoData photoData = gson.fromJson(jsonObject.toString(), PhotoData.class);
            Photos photos = photoData.getPhotos();
            AllData.getInstance().setPhotoData(photos);

            activity.startActivity(new Intent(activity, targetClass));
            activity.finish();
        } catch (Exception e) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    showSnackBar("Invalid Code. If problem persists contact us)", snackBarParentView);
                }
            });
        }
    }
}