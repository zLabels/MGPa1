package com.sidm.mobilegamea1;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Random;

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    // Implement this interface to receive information about changes to the surface.
    private GameThread myThread = null; // Thread to control the rendering

    private Bitmap bg, scaledbg;    //Used for background
    int ScreenWidth, ScreenHeight;  //Define Screen width and Screen height
    private short bgX = 0, bgY = 0;  //Variables for defining background start and end point
    short ScrollSpeed = 500;    //Speed of background scrolling
    float timer = 0.f;
    int score = 0;

    // Variables for FPS
    public float FPS = 0;
    float deltaTime;
    long dt;
    Paint paint = new Paint(); //Used for text rendering

    private SpriteAnimation stickman_anim;
    Random r = new Random();

    private short GameState;    // Variable for Game State check

    //Game elements
    private Obstacle[] obstacleList = new Obstacle[20];
    float SpawnRate = 0.5f;
    float SpawnTimer = 0.f;
    private Obstacle nearestObstacle;

    private boolean GameActive = true;

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

        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.setOnTouchListener(new OnSwipeTouchListener(context) {
            public boolean onSwipeRight() {
                if(nearestObstacle.getType() == Obstacle.TYPE.T_RIGHT)
                {
                    nearestObstacle.setActive(false);
                    score += 10;
                }
                return true;
            }

            public boolean onSwipeLeft() {
                if(nearestObstacle.getType() == Obstacle.TYPE.T_LEFT)
                {
                    nearestObstacle.setActive(false);
                    score += 10;
                }
                return true;
            }

            public boolean onSwipeTop() {
                if(nearestObstacle.getType() == Obstacle.TYPE.T_UP)
                {
                    nearestObstacle.setActive(false);
                    score += 10;
                }
                return true;
            }

            public boolean onSwipeBottom() {
                if(nearestObstacle.getType() == Obstacle.TYPE.T_DOWN)
                {
                    nearestObstacle.setActive(false);
                    score += 10;
                }
                return true;
            }
            public void onClick(int posX, int posY){
                ScreenTap(posX,posY);
            }
        });

        //Loading images when created
        bg = BitmapFactory.decodeResource(getResources(),
                R.drawable.game_background);
        scaledbg = Bitmap.createScaledBitmap(bg, ScreenWidth, ScreenHeight, true);

        //Text rendering values
        paint.setARGB(255, 0, 0, 0);
        paint.setStrokeWidth(100);
        paint.setTextSize(30);

        //Sprite animation init
        stickman_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(),R.drawable.stickman_sprite),50,480,32,32);

        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);

        for (int i = 0; i < obstacleList.length; ++i) {
            obstacleList[i] = new Obstacle();
        }
        nearestObstacle = obstacleList[0];
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

        //FPS
        canvas.drawText("FPS:" + FPS, 130, 75, paint);

        //Score
        canvas.drawText("Score:" + score, 800, 75, paint);

        if(GameActive == false){
            canvas.drawText("Game Over", 800, 500, paint);
        }
        stickman_anim.draw(canvas);

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

                SpawnTimer += dt;
                timer += dt;

                if(timer > 5.f)
                {
                    ScrollSpeed += 100;
                    timer = 0;
                }
                if(SpawnTimer > SpawnRate)
                {
                    FetchObstacle();
                    SpawnTimer = 0.f;
                }

                stickman_anim.update(System.currentTimeMillis());

                //Updating game elements
                for(int i = 0; i < obstacleList.length; ++i){
                    if(obstacleList[i].isActive()){
                        obstacleList[i].setPosX(obstacleList[i].getPosX() - ScrollSpeed * dt);

                        if(CheckCollision(stickman_anim.getX(), stickman_anim.getY(),
                                stickman_anim.getSpriteWidth(),stickman_anim.getSpriteHeight(),
                                (int)obstacleList[i].getPosX(),(int)obstacleList[i].getPosY(),
                                obstacleList[i].getImgWidth(),obstacleList[i].getImgHeight()))
                        {
                            obstacleList[i].setActive(false);
                            GameActive = false;
                        }
                        //Get nearest non tap obstacle
                        if(obstacleList[i].getType() != Obstacle.TYPE.T_TAP) {
                            if (nearestObstacle.isActive() == false) {
                                nearestObstacle = obstacleList[i];
                            } else if (obstacleList[i].getPosX() < nearestObstacle.getPosX()) {
                                nearestObstacle = obstacleList[i];
                            }
                        }
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
                int result = r.nextInt((100 - 0) + 1) + 0;
                if(result >= 80) {
                    obstacleList[i].SetAllData(ScreenWidth, 425, BitmapFactory.decodeResource(getResources(),
                            R.drawable.tap_obstacle), Obstacle.TYPE.T_TAP, 10, true);
                    break;
                }
                else if(result >=60 && result < 80){
                    obstacleList[i].SetAllData(ScreenWidth, 425, BitmapFactory.decodeResource(getResources(),
                            R.drawable.left_obstacle), Obstacle.TYPE.T_LEFT, 10, true);
                    break;
                }
                else if(result >=40 && result < 60){
                    obstacleList[i].SetAllData(ScreenWidth, 425, BitmapFactory.decodeResource(getResources(),
                            R.drawable.up_obstacle), Obstacle.TYPE.T_UP, 10, true);
                    break;
                }
                else if(result >=20 && result < 40){
                    obstacleList[i].SetAllData(ScreenWidth, 425, BitmapFactory.decodeResource(getResources(),
                            R.drawable.down_obstacle), Obstacle.TYPE.T_DOWN, 10, true);
                    break;
                }
                else if(result >=0 && result < 20){
                    obstacleList[i].SetAllData(ScreenWidth, 425, BitmapFactory.decodeResource(getResources(),
                            R.drawable.right_obstacle), Obstacle.TYPE.T_RIGHT, 10, true);
                    break;
                }
                else{
                    obstacleList[i].SetAllData(ScreenWidth, 425, BitmapFactory.decodeResource(getResources(),
                            R.drawable.tap_obstacle), Obstacle.TYPE.T_TAP, 10, true);
                    break;
                }
            }
        }
    }

    public void ScreenTap(int X, int Y){

        for(int i = 0; i < obstacleList.length; ++i) {
            //Only check against active objects
            if(obstacleList[i].isActive() && obstacleList[i].getType() == Obstacle.TYPE.T_TAP)
            {
                obstacleList[i].setActive(false);
                score += 10;
            }
        }
    }

}
