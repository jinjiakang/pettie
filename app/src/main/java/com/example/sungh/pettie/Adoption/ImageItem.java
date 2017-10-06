package com.example.sungh.pettie.Adoption;

// This class must has a bitmap and string

import android.graphics.Bitmap;

public class ImageItem {
    private Bitmap image;
    private String title;

    public ImageItem(Bitmap bitmap, String title){
        super();
        this.image = bitmap;
        this.title = title;
    }

    public Bitmap getImage(){ return image; }

    public void setImage(Bitmap image) { this.image = image; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }
}
