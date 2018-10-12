package com.example.harpalsingh.fabgallery.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harpalsingh.fabgallery.R;
import com.example.harpalsingh.fabgallery.models.AllData;
import com.example.harpalsingh.fabgallery.models.PhotoData;
import com.example.harpalsingh.fabgallery.models.Photos;
import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView;
    Button button, continueLogin;
    WebView webView;
    ImageView imageView;
    EditText editText;
    OAuth1RequestToken requestToken;
    private static final String PROTECTED_RESOURCE_URL = "https://api.flickr.com/services/rest/";
    String authorizationUrl;
    LinearLayout loginLinearLayout, conitnueLinearLayout;
    OAuth10aService service;
    OAuth1AccessToken accessToken = null;
    private static final String PREFS_NAME = "jsonObject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (!getPreference(getApplicationContext(),"json").equals("")) {
            Gson gson = new Gson();
            PhotoData photoData = gson.fromJson(getPreference(getApplicationContext(),"json"), PhotoData.class);
            Photos photos = photoData.getPhotos();
            AllData.getInstance().setPhotoData(photos);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        textView = (TextView) findViewById(R.id.url);
        button = (Button) findViewById(R.id.yahoo_login);
        continueLogin = (Button) findViewById(R.id.login);
        editText = findViewById(R.id.auth_code);
        button.setOnClickListener(this);
        continueLogin.setOnClickListener(this);
        webView = (WebView) findViewById(R.id.simpleWebView);
        webView.setWebViewClient(new MyBrowser());

        loginLinearLayout = (LinearLayout) findViewById(R.id.login_layout);
        conitnueLinearLayout = (LinearLayout) findViewById(R.id.continue_layout);

        imageView = (ImageView) findViewById(R.id.logo);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        try {
            getAuthToken();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getAuthToken() throws InterruptedException, ExecutionException, IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        final String apiKey = "7486602c1ebc89c43070445b4efa85b5";
        final String apiSecret = "a3f437985c1305ba";

        service = new ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .build(FlickrApi.instance(FlickrApi.FlickrPerm.DELETE));

        requestToken = service.getRequestToken();


        authorizationUrl = service.getAuthorizationUrl(requestToken);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yahoo_login:
                imageView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(authorizationUrl);
                loginLinearLayout.setVisibility(View.GONE);
                conitnueLinearLayout.setVisibility(View.VISIBLE);

                break;

            case R.id.login:
                JSONObject jsonObject = null;
                String oauthVerifier = "";
                oauthVerifier = String.valueOf(editText.getText());

                try {
                    accessToken = service.getAccessToken(requestToken, oauthVerifier);


                    final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
                    request.addQuerystringParameter("method", "flickr.photos.search");
                    request.addQuerystringParameter("format", "json");
                    request.addQuerystringParameter("nojsoncallback", "1");


                    service.signRequest(accessToken, request);
                    Response response = null;
                    response = service.execute(request);
                    jsonObject = new JSONObject(response.getBody());

                    setPreference(getApplicationContext(),"json",jsonObject.toString());


                    Gson gson = new Gson();


                    PhotoData photoData = gson.fromJson(jsonObject.toString(), PhotoData.class);
                    Photos photos = photoData.getPhotos();
                    AllData.getInstance().setPhotoData(photos);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } catch (Exception e) {
                    webView.reload();
                    Toast.makeText(this, "Something went wrong.Try Again", Toast.LENGTH_LONG).show();
                }


                break;
        }
    }

    public static boolean setPreference(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getPreference(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, "");
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            WebSettings settings = view.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setAllowContentAccess(true);
            settings.setDomStorageEnabled(true);
            view.loadUrl(url);
            return true;
        }
    }
}

