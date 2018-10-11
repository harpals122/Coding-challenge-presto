package com.example.harpalsingh.fabgallery.APILayer;

import com.example.harpalsingh.fabgallery.interfaces.KeyConfig;
import com.example.harpalsingh.fabgallery.interfaces.PhotosInterface;
import com.example.harpalsingh.fabgallery.models.PhotoData;

import java.util.List;

import retrofit2.Call;

public class RetrofitServices {

    private static RetrofitServices SERVICE_INSTANCE = null;

    private RetrofitServices() {
    }

    public static RetrofitServices getNYServiceInstance() {
        if (SERVICE_INSTANCE == null) {
            SERVICE_INSTANCE = new RetrofitServices();
        }
        return SERVICE_INSTANCE;
    }

    public Call<PhotoData> getPhotos() {
        return RetrofitAPIClient.APIClient().create(PhotosInterface.class).getPhotos(KeyConfig.method,
                KeyConfig.api_key,KeyConfig.format,KeyConfig.nojsoncallback,KeyConfig.auth_token,KeyConfig.api_sig);
    }
}