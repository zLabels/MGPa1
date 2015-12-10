package com.sidm.mobilegamea1;

import android.graphics.Bitmap;

/**
 * Created by Princeton on 8/12/2015.
 */
public class InGameScreens {
    float posX = 0, posY = 0;   //Coordinates of button
    private Bitmap image;    //Image of screen

    public InGameScreens(float posX, float posY, Bitmap image) {
        this.posX = posX;
        this.posY = posY;
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }
}
