package com.example.harpalsingh.fabgallery.APILayer;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.harpalsingh.fabgallery.constants.Constants.ROOT_URL;

class RetrofitAPIClient {

    public static Retrofit APIClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }
}