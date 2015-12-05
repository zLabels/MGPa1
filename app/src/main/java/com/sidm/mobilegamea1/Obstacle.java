package com.sidm.mobilegamea1;

import android.graphics.Bitmap;

/**
 * Created by Princeton on 5/12/2015.
 */
public class Obstacle {

    float posX = 0, posY = 0;   //Coordinates of obstacle
    private Bitmap obstacle;    //Image of obstacle
    int type = 1; //Type of obstacle
    int health = 10;   //Health of obstacle if required
    boolean active = false; //Active status of obstacle
    int imgWidth;   //Width of image
    int imgHeight;  //Height of image

    public Obstacle() {
        this.posX = 0;
        this.posY = 0;
        this.type = 1;
        this.health = 10;
        this.active = false;
        this.imgHeight = 0;
        this.imgWidth = 0;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public void SetAllData(float posX,float posY, Bitmap img, int type, int health, boolean active){
        this.posX = posX;
        this.posY = posY;
        this.obstacle = img;
        this.type = type;
        this.health = health;
        this.active = active;
        this.imgHeight = this.obstacle.getHeight();
        this.imgWidth = this.obstacle.getWidth();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public Bitmap getObstacle() {
        return obstacle;
    }

    public void setObstacle(Bitmap obstacle) {
        this.obstacle = obstacle;
        this.imgHeight = this.obstacle.getHeight();
        this.imgWidth = this.obstacle.getWidth();
    }

    public int getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(short health) {
        this.health = health;
    }
}
