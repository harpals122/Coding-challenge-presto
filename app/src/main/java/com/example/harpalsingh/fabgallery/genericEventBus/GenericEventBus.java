package com.example.harpalsingh.fabgallery.genericEventBus;

import com.example.harpalsingh.fabgallery.models.PhotoData;

import java.util.ArrayList;

public class GenericEventBus {
    private final PhotoData photoData;

    public GenericEventBus(PhotoData photoData) {
        this.photoData = photoData;
    }

    public PhotoData getPhotoData() {
        return photoData;
    }
}