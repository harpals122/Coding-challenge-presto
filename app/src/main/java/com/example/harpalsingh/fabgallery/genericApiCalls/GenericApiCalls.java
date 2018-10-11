package com.example.harpalsingh.fabgallery.genericApiCalls;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.example.harpalsingh.fabgallery.APILayer.RetrofitServices;
import com.example.harpalsingh.fabgallery.genericEventBus.GenericEventBus;
import com.example.harpalsingh.fabgallery.interfaces.KeyConfig;
import com.example.harpalsingh.fabgallery.models.PhotoData;
import com.example.harpalsingh.fabgallery.utilities.Utilities;

import org.greenrobot.eventbus.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenericApiCalls {

    private static PhotoData PHOTO_DATA = new PhotoData();

    public static void doServerRequest(@Nullable final Context context, final View snackBarParentView) {

        Call<PhotoData> callPlaces = RetrofitServices.getNYServiceInstance().getPhotos();
        callPlaces.enqueue(new Callback<PhotoData>() {
            @Override
            public void onResponse(@NonNull Call<PhotoData> call, @NonNull Response<PhotoData> response) {
                if (response.code() == 200 & response.body().getPhotos() !=null) {

                    Toast.makeText(context,""+response.body(),Toast.LENGTH_LONG).show();
                    EventBus.getDefault().post(new GenericEventBus(PHOTO_DATA));
                } else {
                    Utilities.showSnackBar(KeyConfig.networkCallFailMessage, snackBarParentView);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PhotoData> call, @NonNull Throwable t) {
                Utilities.showSnackBar(KeyConfig.networkCallFailMessage, snackBarParentView);
            }
        });
    }
}