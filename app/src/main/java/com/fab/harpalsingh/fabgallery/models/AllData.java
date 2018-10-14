package com.fab.harpalsingh.fabgallery.models;

public class AllData {
    private static final AllData ourInstance = new AllData();
    private static Photos photoData = new Photos();
    private AllData() { }

    public static AllData getInstance() {
        return ourInstance;
    }

    public Photos getPhotoData() {
        return photoData;
    }

    public void setPhotoData(Photos photoData) {
        AllData.photoData = photoData;
    }
}