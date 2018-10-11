package com.example.harpalsingh.fabgallery.APILayer;

import com.example.harpalsingh.fabgallery.interfaces.KeyConfig;
import com.example.harpalsingh.fabgallery.interfaces.PhotosInterface;
import com.example.harpalsingh.fabgallery.models.ImageSizeData;
import com.example.harpalsingh.fabgallery.models.PhotoData;

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
        return RetrofitAPIClient.APIClient().create(PhotosInterface.class).getPhotos(KeyConfig.searchMethod,
                KeyConfig.api_key, KeyConfig.format, KeyConfig.nojsoncallback, KeyConfig.auth_token, KeyConfig.api_sig);
    }

    public Call<ImageSizeData>getImageSizeData() {
        return RetrofitAPIClient.APIClient().create(PhotosInterface.class).getImageSizeData(KeyConfig.sizehMethod,
                "d056cbfd26e82eefa31f157276a217eb","43405960150", KeyConfig.format, KeyConfig.nojsoncallback, "72157696453945240-8138b97be46f0b22", "a55316841c37c2f9b575f073529fb9fd");

    }
}
