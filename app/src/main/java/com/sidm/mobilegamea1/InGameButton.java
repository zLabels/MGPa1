package com.sidm.mobilegamea1;

import android.graphics.Bitmap;

/**
 * Created by Princeton on 8/12/2015.
 */
public class InGameButton {
    float posX = 0, posY = 0;   //Coordinates of button
    private Bitmap image;    //Image of button
    int imgWidth;   //Width of image
    int imgHeight;  //Height of image
    boolean active; //active status of button

    public InGameButton(float posX, float posY, Bitmap image,boolean active) {
        this.posX = posX;
        this.posY = posY;
        this.image = image;
        this.imgWidth = image.getWidth();
        this.imgHeight = image.getHeight();
        this.active = active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }


    public int getImgHeight() {
        return imgHeight;
    }

    public int getImgWidth() {
        return imgWidth;
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
