package com.sidm.mobilegamea1;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Princeton on 3/12/2015.
 */
public class SpriteAnimation {
    private Bitmap bitmap;  //Animation sequence
    private Rect sourceRect;    //Rect to be drawn from animation
    private int frame;  //Number of frames
    private int currentFrame;   //Current frame
    private long frameTicker;   //Time of last updated frame
    private int framePeroid;    //Milliseconds between frame

    private int spriteWidth;    //Width of sprite to be calculated
    private int spriteHeight;   //Height of sprite

    private int x;  //X coord of object
    private int y;  //Y coord of object

    public SpriteAnimation(Bitmap bitmap, int x , int y, int fps, int frameCount){
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;

        currentFrame = 0;
        frame = frameCount;

        spriteWidth = bitmap.getWidth() / frameCount;
        spriteHeight = bitmap.getHeight();

        sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);

        framePeroid = 1000 / fps;
        frameTicker = 01;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Rect getSourceRect() {
        return sourceRect;
    }

    public void setSourceRect(Rect sourceRect) {
        this.sourceRect = sourceRect;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public long getFrameTicker() {
        return frameTicker;
    }

    public void setFrameTicker(long frameTicker) {
        this.frameTicker = frameTicker;
    }

    public int getFramePeroid() {
        return framePeroid;
    }

    public void setFramePeroid(int framePeroid) {
        this.framePeroid = framePeroid;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public void setSpriteWidth(int spriteWidth) {
        this.spriteWidth = spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public void setSpriteHeight(int spriteHeight) {
        this.spriteHeight = spriteHeight;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void update(long gameTime)
    {
        if(gameTime > frameTicker + framePeroid)
        {
            frameTicker = gameTime;
            currentFrame++; //Increment the frame

            if(currentFrame >= frame)   //Frame = total no. of frames
            {
                currentFrame = 0;   //Reached end of frame, reset to 0
            }
        }

        this.sourceRect.left = currentFrame * spriteWidth;
        this.sourceRect.right = this.sourceRect.left + spriteWidth;
    }

    public void draw(Canvas canvas){
        Rect destRect = new Rect(getX(),getY(), getX() + spriteWidth, getY() + spriteHeight);
        canvas.drawBitmap(bitmap,sourceRect, destRect, null);
    }
}
