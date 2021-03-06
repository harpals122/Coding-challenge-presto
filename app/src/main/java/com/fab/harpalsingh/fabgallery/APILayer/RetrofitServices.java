package com.fab.harpalsingh.fabgallery.APILayer;

import com.fab.harpalsingh.fabgallery.interfaces.KeyConfig;
import com.fab.harpalsingh.fabgallery.interfaces.PhotosInterface;
import com.fab.harpalsingh.fabgallery.models.ImageSizeData;

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
        return RetrofitAPIClient.APIClient().create(PhotosInterface.class).getImageSizeData(KeyConfig.FLICKR_PHOTOS_GET_SIZES,
                KeyConfig.API_KEY,photoId, KeyConfig.FORMAT, KeyConfig.NOJSONCALLBACK);
    }
}
