package com.example.harpalsingh.fabgallery.activities;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harpalsingh.fabgallery.R;
import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String PROTECTED_RESOURCE_URL = "http://api.flickr.com/services/rest/";
    TextView textView;
    Button button;
    OAuth10aService service;
    private OAuth1RequestToken requestToken;
    String authUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        textView = (TextView) findViewById(R.id.url);
        button = (Button) findViewById(R.id.dorequest);
        button.setOnClickListener(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        service = new ServiceBuilder("b3b9dae4c7585158b2753adb7268643c")
                .apiSecret("3f205a38db47bc77")
                .build(FlickrApi.instance());


        try {
            requestToken   = service.getRequestToken();
            authUrl  = service.getAuthorizationUrl(requestToken);
            textView.setText(requestToken.getToken());

        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dorequest:
                try {
                String verifier = textView.getText().toString();


                final OAuth1AccessToken accessToken = service.getAccessToken(requestToken, verifier);
                final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
                request.addQuerystringParameter("method", "flickr.test.login");
                service.signRequest(accessToken, request);
                final Response response = service.execute(request);
                Toast.makeText(getApplicationContext(), "" + response.getBody(), Toast.LENGTH_LONG).show();
                } catch (IOException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}

