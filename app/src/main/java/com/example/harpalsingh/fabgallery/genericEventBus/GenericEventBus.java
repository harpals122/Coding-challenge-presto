package com.example.harpalsingh.fabgallery.genericEventBus;

import com.example.harpalsingh.fabgallery.models.PhotoData;

public class GenericEventBus {
    private PhotoData photoData = null ;

    public GenericEventBus(PhotoData photoData) {
        this.photoData = photoData;
    }
    public PhotoData getPhotoData() {
        return photoData;
    }
}