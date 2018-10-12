package com.example.harpalsingh.fabgallery.utilities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.harpalsingh.fabgallery.R;
import com.example.harpalsingh.fabgallery.activities.MainActivity;
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

import java.util.Objects;

public class Utilities {

    private static final String PREFS_NAME = "FabGalleryPreferences";
    private static final String PROTECTED_RESOURCE_URL = "https://api.flickr.com/services/rest/";

    public static void setupToolbarAndNavigationBar(final MainActivity mainActivity, Toolbar toolbar, NavigationView navigationView, final DrawerLayout drawerLayout) {
        mainActivity.setSupportActionBar(toolbar);
        ActionBar actionbar = mainActivity.getSupportActionBar();
        Objects.requireNonNull(actionbar).setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
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

    public static void makeNetworkCall(Context applicationContext, EditText editText, OAuth10aService service, OAuth1RequestToken requestToken, Class targetClass) {
        JSONObject jsonObject;
        String oauthVerifier;
        oauthVerifier = String.valueOf(editText.getText());

        try {
            OAuth1AccessToken accessToken = service.getAccessToken(requestToken, oauthVerifier);
            final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
            request.addQuerystringParameter("method", "flickr.photos.search");
            request.addQuerystringParameter("format", "json");
            request.addQuerystringParameter("nojsoncallback", "1");
            request.addQuerystringParameter("per_page", "500");
            request.addQuerystringParameter("min_taken_date", "2008");
            request.addQuerystringParameter("max_taken_date", "2017");


            service.signRequest(accessToken, request);
            Response response;
            response = service.execute(request);
            jsonObject = new JSONObject(response.getBody());

            setPreference(applicationContext, jsonObject.toString());

            Gson gson = new Gson();

            PhotoData photoData = gson.fromJson(jsonObject.toString(), PhotoData.class);
            Photos photos = photoData.getPhotos();
            AllData.getInstance().setPhotoData(photos);
            Activity activity = (Activity) applicationContext;
            activity.startActivity(new Intent(activity, targetClass));
            activity.finish();
        } catch (Exception e) {
            Toast.makeText(applicationContext, e.getMessage()+"Something went wrong.Try Again", Toast.LENGTH_LONG).show();
        }
    }
}