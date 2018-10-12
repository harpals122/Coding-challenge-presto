package com.fab.harpalsingh.fabgallery.interfaces;

import com.fab.harpalsingh.fabgallery.models.ImageSizeData;
import com.fab.harpalsingh.fabgallery.models.PhotoData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import static com.fab.harpalsingh.fabgallery.constants.Constants.SEARCH_PHOTO;

public interface PhotosInterface {

    @GET(SEARCH_PHOTO)
    Call<ImageSizeData> getImageSizeData(@Query("method") String method,
                                         @Query("api_key") String api_key,
                                         @Query("photo_id") String photo_id,
                                         @Query("format") String format,
                                         @Query("nojsoncallback") String nojsoncallback);
}

