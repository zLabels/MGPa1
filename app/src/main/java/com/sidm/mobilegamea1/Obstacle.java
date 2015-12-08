package com.sidm.mobilegamea1;

import android.graphics.Bitmap;

/**
 * Created by Princeton on 5/12/2015.
 */
public class Obstacle {

    public enum TYPE{
        T_TAP,
        T_LEFT,
        T_RIGHT,
        T_UP,
        T_DOWN
    };

    float posX = 0, posY = 0;   //Coordinates of obstacle
    private Bitmap obstacle;    //Image of obstacle
    TYPE type = TYPE.T_TAP; //Type of obstacle
    int health = 10;   //Health of obstacle if required
    boolean active = false; //Active status of obstacle
    int imgWidth;   //Width of image
    int imgHeight;  //Height of image

    public Obstacle() {
        this.posX = 0;
        this.posY = 0;
        this.type = TYPE.T_TAP;
        this.health = 10;
        this.active = false;
        this.imgHeight = 0;
        this.imgWidth = 0;
    }

    public static TYPE fromInteger(int x)
    {
        switch(x) {
            case 0: return TYPE.T_TAP;
            case 1: return TYPE.T_LEFT;
            case 2: return TYPE.T_RIGHT;
            case 3: return TYPE.T_UP;
            case 4: return TYPE.T_DOWN;
        }
        return null;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public void SetAllData(float posX,float posY, Bitmap img, TYPE type, int health, boolean active){
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

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(short health) {
        this.health = health;
    }
}
