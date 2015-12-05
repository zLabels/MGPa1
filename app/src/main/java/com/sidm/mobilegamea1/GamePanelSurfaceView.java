package com.sidm.mobilegamea1;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    // Implement this interface to receive information about changes to the surface.

    private GameThread myThread = null; // Thread to control the rendering

    private Bitmap bg, scaledbg;    //Used for background
    int ScreenWidth, ScreenHeight;  //Define Screen width and Screen height
    private short bgX = 0, bgY = 0;  //Variables for defining background start and end point
    short ScrollSpeed = 500;    //Speed of background scrolling

    private Bitmap[] ship = new Bitmap[4];  // 4a) bitmap array to stores 4 images of the spaceship
    private short shipindex;    //Index to track spaceship image

    // Variables for FPS
    public float FPS = 0;
    float deltaTime;
    long dt;
    Paint paint = new Paint(); //Used for text rendering

    private short mX = 0, mY = 0;   //Ship position

    //Sprite animation
    private SpriteAnimation stone_anim;
    Random r = new Random();

    private short GameState;    // Variable for Game State check

    //Game elements
    private Obstacle[] obstacleList = new Obstacle[20];
    float SpawnRate = 4.f;
    float SpawnTimer = 0.f;

    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView(Context context) {

        // Context is the current state of the application/object
        super(context);

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        //Set information to get screen size
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;

        //Loading images when created
        bg = BitmapFactory.decodeResource(getResources(),
                R.drawable.game_background);
        scaledbg = Bitmap.createScaledBitmap(bg, ScreenWidth, ScreenHeight, true);
        ship[0] = BitmapFactory.decodeResource(getResources(),
                R.drawable.ship2_1);
        ship[1] = BitmapFactory.decodeResource(getResources(),
                R.drawable.ship2_2);
        ship[2] = BitmapFactory.decodeResource(getResources(),
                R.drawable.ship2_3);
        ship[3] = BitmapFactory.decodeResource(getResources(),
                R.drawable.ship2_4);

        //Text rendering values
        paint.setARGB(255, 0, 0, 0);
        paint.setStrokeWidth(100);
        paint.setTextSize(30);

        //Sprite animation init
        stone_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.flystone), 320, 64, 5, 5);
        stone_anim.setX(r.nextInt((ScreenWidth - 0) + 1) + 0);
        stone_anim.setY(r.nextInt((ScreenHeight - 0) + 1) + 0);

        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);

        for (int i = 0; i < obstacleList.length; ++i) {
            obstacleList[i] = new Obstacle();
        }
    }

    //must implement inherited abstract methods
    public void surfaceCreated(SurfaceHolder holder) {
        // Create the thread
        if (!myThread.isAlive()) {
            myThread = new GameThread(getHolder(), this);
            myThread.startRun(true);
            myThread.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Destroy the thread
        if (myThread.isAlive()) {
            myThread.startRun(false);


        }
        boolean retry = true;
        while (retry) {
            try {
                myThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public void RenderGameplay(Canvas canvas) {
        // 2) Re-draw 2nd image after the 1st image ends
        if (canvas == null) {
            return;
        }
        canvas.drawBitmap(scaledbg, bgX, bgY, null);
        canvas.drawBitmap(scaledbg, bgX + ScreenWidth, bgY, null);
        // 4d) Draw the spaceships
        canvas.drawBitmap(ship[shipindex], mX, mY, null);

        //FPS
        canvas.drawText("FPS:" + FPS, 130, 75, paint);
        canvas.drawText("ScreenHeight:" + ScreenHeight, 130, 100, paint);
        canvas.drawText("ScreenWidth:" + ScreenWidth, 130, 125, paint);

        stone_anim.draw(canvas);

        for (int i = 0; i < obstacleList.length; ++i) {
            //Only draw if active
            if (obstacleList[i].isActive()) {
                canvas.drawBitmap(obstacleList[i].getObstacle(), obstacleList[i].getPosX(), obstacleList[i].getPosY(), null);
            }
        }
    }

    //Update method to update the game play
    public void update(float dt, float fps){
        FPS = fps;

        switch (GameState) {
            case 0: {
                bgX -= ScrollSpeed * dt; //Speed of background scrolling
                if (bgX < -ScreenWidth){
                    bgX = 0;
                }

                shipindex++;
                shipindex%=4;
                SpawnTimer += dt;

                if(SpawnTimer > SpawnRate)
                {
                    FetchObstacle();
                    SpawnTimer = 0.f;
                }

                stone_anim.update(System.currentTimeMillis());

                if(CheckCollision(mX,mY,ship[shipindex].getWidth(),  ship[shipindex].getHeight(),
                        stone_anim.getX(),stone_anim.getY(),stone_anim.getSpriteWidth(),stone_anim.getSpriteHeight()))
                {
                    stone_anim.setX(r.nextInt((ScreenWidth - 0) + 1) + 0);
                    stone_anim.setY(r.nextInt((ScreenHeight - 0) + 1) + 0);
                }

                //Updating game elements
                for(int i = 0; i < obstacleList.length; ++i){
                    if(obstacleList[i].isActive()){
                        obstacleList[i].setPosX(obstacleList[i].getPosX() - ScrollSpeed * dt);
                        //if out of screen
                        if(obstacleList[i].getPosX() < 0){
                            obstacleList[i].setActive(false);
                        }
                    }
                }

            }
            break;
        }
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas){
        switch (GameState)
        {
            case 0:
                RenderGameplay(canvas);
                break;
        }
    }

    public boolean CheckCollision(int x1,int y1,int w1,int h1, int x2, int y2, int w2, int h2)
    {
        if(x2>=x1 && x2<=x1 + w1){  //start detect collision of top left
            if(y2>= y1 & y2<= y1 + h1){
                return true;
            }
        }
        if(x2+w2>=x1 && x2+w2<=x1+w1){  //Top right
            if(y2>=y1 && y2<=y1+h1){
                return true;
            }
        }
        if(x2>=x1 && x2<= x1+w1){  //Btm Left
            if(y2+h2>=y1 && y2+h2<=y1+h1){
                return true;
            }
        }
        if(x2+w2>=x1 && x2+w2<=x1+w1){  //Btm Right
            if(y2+h2>=y1 && y2+h2<=y1+h1){
                return true;
            }
        }
        return false;
    }

    public boolean CheckTouch(int touch_x,int touch_y, float min_x,float min_y, int max_x,int max_y){
        if(touch_x >= min_x && touch_x <= max_x && touch_y >= min_y && touch_y <= max_y){
            return true;
        }
        return false;
    }

    public void FetchObstacle()
    {
        for(int i = 0; i < obstacleList.length; ++i){
            if(obstacleList[i].isActive() == false){
                obstacleList[i].SetAllData(ScreenWidth,425,BitmapFactory.decodeResource(getResources(),
                        R.drawable.tap_obstacle),1,10,true);
                break;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        short X = (short) event.getX();
        short Y = (short) event.getY();

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            //New Location
            mX = (short)(X - ship[shipindex].getWidth()/2);
            mY = (short)(Y - ship[shipindex].getHeight()/2);

            for(int i = 0; i < obstacleList.length; ++i) {
                //Only check against active objects
                if(obstacleList[i].isActive()) {
                    if (CheckTouch(X, Y, obstacleList[i].getPosX(), obstacleList[i].getPosY(),
                            (int)obstacleList[i].getPosX() + obstacleList[i].getImgWidth(),(int)obstacleList[i].getPosY() + obstacleList[i].getImgHeight())) {
                        obstacleList[i].setActive(false);
                    }
                }
            }

        }
        return super.onTouchEvent(event);
    }
}
