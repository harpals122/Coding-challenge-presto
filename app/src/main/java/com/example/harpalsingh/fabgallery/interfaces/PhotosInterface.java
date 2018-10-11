package com.example.harpalsingh.fabgallery.interfaces;

import com.example.harpalsingh.fabgallery.models.PhotoData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import static com.example.harpalsingh.fabgallery.constants.Constants.SEARCH_PHOTO;

public interface PhotosInterface {
    @GET(SEARCH_PHOTO)
    Call<PhotoData> getPhotos(@Query("method") String method,
                              @Query("api_key") String api_key,
                              @Query("format") String format,
                              @Query("nojsoncallback") int nojsoncallback,
                              @Query("auth_token") String page_number,
                              @Query("api_sig") String client_id);
    }

