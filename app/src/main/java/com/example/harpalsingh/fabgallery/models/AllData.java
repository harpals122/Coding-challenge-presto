package com.example.harpalsingh.fabgallery.models;

import java.util.ArrayList;

public class AllData {
    private static final AllData ourInstance = new AllData();
    private PhotoData photoData = new PhotoData();
    private ImageSizeData imageSizeData = new ImageSizeData();

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

    public ImageSizeData getImageSizeData() {
        return imageSizeData;
    }

    public void setImageSizeData(ImageSizeData imageSizeData) {
        this.imageSizeData = imageSizeData;
    }
}