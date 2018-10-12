package com.example.harpalsingh.fabgallery.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.harpalsingh.fabgallery.R;
import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.harpalsingh.fabgallery.utilities.Utilities.isDataSaved;
import static com.example.harpalsingh.fabgallery.utilities.Utilities.makeNetworkCall;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.simpleWebView)
    WebView webView;
    @BindView(R.id.logo)
    ImageView imageView;
    @BindView(R.id.auth_code)
    EditText editText;
    @BindView(R.id.login_layout)
    LinearLayout loginLinearLayout;
    @BindView(R.id.continue_layout)
    LinearLayout conitnueLinearLayout;

    private OAuth1RequestToken requestToken;
    private String authorizationUrl;
    private OAuth10aService service;
    private final OAuth1AccessToken accessToken = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        isDataSaved(this, MainActivity.class);
        setupWebView();
        getAuthToken();
    }

    private void setupWebView() {
        webView.setWebViewClient(new MyBrowser());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
    }

    private void getAuthToken() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String apiKey = "7486602c1ebc89c43070445b4efa85b5";
                    final String apiSecret = "a3f437985c1305ba";
                    service = new ServiceBuilder(apiKey)
                            .apiSecret(apiSecret)
                            .build(FlickrApi.instance(FlickrApi.FlickrPerm.DELETE));
                    requestToken = service.getRequestToken();
                    authorizationUrl = service.getAuthorizationUrl(requestToken);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @OnClick(R.id.yahoo_login)
    public void loginContinue() {
        continueLogin();
    }

    @OnClick(R.id.login)
    public void login() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                makeNetworkCall(LoginActivity.this, editText, service, requestToken, MainActivity.class);
            }
        });

        thread.start();

    }

    private void continueLogin() {
        imageView.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        webView.loadUrl(authorizationUrl);
        loginLinearLayout.setVisibility(View.GONE);
        conitnueLinearLayout.setVisibility(View.VISIBLE);
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

