package com.example.harpalsingh.fabgallery.models;

import java.util.ArrayList;

public class AllData {
    private static final AllData ourInstance = new AllData();
    private PhotoData photoData = new PhotoData();

    private AllData() { }

    public static AllData getInstance() {
        return ourInstance;
    }

    public PhotoData getPhotoData() {
        return photoData;
    }

    public void setPhotoData(PhotoData photoData) {
        this.photoData = photoData;
    }
}