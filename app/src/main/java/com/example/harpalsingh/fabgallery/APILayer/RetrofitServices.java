package com.example.harpalsingh.fabgallery.APILayer;

import android.os.StrictMode;

import com.example.harpalsingh.fabgallery.interfaces.KeyConfig;
import com.example.harpalsingh.fabgallery.interfaces.PhotosInterface;
import com.example.harpalsingh.fabgallery.models.ImageSizeData;
import com.example.harpalsingh.fabgallery.models.PhotoData;

import java.security.Key;

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

    public Call<ImageSizeData>getImageSizeData(String photoId) {
        return RetrofitAPIClient.APIClient().create(PhotosInterface.class).getImageSizeData(KeyConfig.sizehMethod,
                KeyConfig.api_key_get_size,photoId, KeyConfig.format, KeyConfig.nojsoncallback);

    }
}
