package com.example.admin.keeper;


import android.graphics.Bitmap;

public class Img extends ListItems {
    private Bitmap mImage;

    public Img(int id, String name, Bitmap image) {
        super(id, name, ListItems.TYPE_ITEM_IMAGE);
        mImage = image;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap image) {
        mImage = image;
    }
}
