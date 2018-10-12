package com.example.harpalsingh.fabgallery.models;

import java.util.ArrayList;

public class AllData {
    private static final AllData ourInstance = new AllData();
    private static Photos photoData = new Photos();
    private ImageSizeData imageSizeData = new ImageSizeData();
    private String token = "";
    private AllData() { }

    public static AllData getInstance() {
        return ourInstance;
    }

    public Photos getPhotoData() {
        return photoData;
    }

    public void setPhotoData(Photos photoData) {
        this.photoData = photoData;
    }

    public ImageSizeData getImageSizeData() {
        return imageSizeData;
    }

    public void setImageSizeData(ImageSizeData imageSizeData) {
        this.imageSizeData = imageSizeData;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}