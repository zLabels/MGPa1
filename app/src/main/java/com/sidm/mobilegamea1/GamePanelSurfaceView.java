package com.sidm.mobilegamea1;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.media.MediaPlayer;

import java.util.Random;

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    // Implement this interface to receive information about changes to the surface.
    private GameThread myThread = null; // Thread to control the rendering

    private Bitmap bg, scaledbg;    //Used for background
    int ScreenWidth, ScreenHeight;  //Define Screen width and Screen height
    private short bgX = 0, bgY = 0;  //Variables for defining background start and end point

    // Variables for FPS
    public float FPS = 0.f;
    Paint paint = new Paint(); //Used for text rendering
    MediaPlayer mp; //Button Feedback

    private SpriteAnimation stickman_anim;
    Random r = new Random();

    private short GameState;    // Variable for Game State check

    // Variables for swiping
    Vector2 InitialPos = new Vector2(0,0);
    Vector2 LastPos = new Vector2(0,0);
    Vector2 DirectionVector = new Vector2(0,0);
    boolean Tapped = false;
    boolean FingerDown = false;

    //Game elements
    private Obstacle[] obstacleList = new Obstacle[20]; //List of all obstacles
    float SpawnRate = 0.5f; //Rate for each obstacle to spawn
    float SpawnTimer = 0.f; //track time to spawn
    private Obstacle nearestObstacle;
    short ScrollSpeed = 500;    //Speed of background scrolling
    float timer = 0.f;  //Timer to increase speed
    int score = 0;  //Play score

    private boolean GameActive = true;

    //In game buttons
    private InGameButton Restart_button = new InGameButton(500,650,
            BitmapFactory.decodeResource(getResources(),R.drawable.restart_ingamebutton),false);
    private InGameButton Mainmenu_button = new InGameButton(1150,650,
            BitmapFactory.decodeResource(getResources(),R.drawable.mainmenu_ingamebutton),false);
    //In game screen
    private InGameScreens Gameover_screen = new InGameScreens(400,200,
            BitmapFactory.decodeResource(getResources(),R.drawable.gameover_screen));;

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

        //Media Players
        mp = MediaPlayer.create(getContext(), R.raw.menu_feedback);

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

        if(GameActive)
        {
            //Score
            canvas.drawText("Score:" + score, 800, 75, paint);
            stickman_anim.draw(canvas);
        }

        for (int i = 0; i < obstacleList.length; ++i) {
            //Only draw if active
            if (obstacleList[i].isActive()) {
                canvas.drawBitmap(obstacleList[i].getObstacle(), obstacleList[i].getPosX(), obstacleList[i].getPosY(), null);
            }
        }

        //Game is lost
        if(GameActive == false){
            canvas.drawBitmap(Gameover_screen.getImage(),Gameover_screen.getPosX(),Gameover_screen.getPosY(),null);
            canvas.drawBitmap(Restart_button.getImage(),Restart_button.getPosX(),Restart_button.getPosY(),null);
            canvas.drawBitmap(Mainmenu_button.getImage(),Mainmenu_button.getPosX(),Mainmenu_button.getPosY(),null);
            //Score
            canvas.drawText("Score:" + score, 800, 500, paint);
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

                if(GameActive) {
                    SpawnTimer += dt;
                    timer += dt;

                    if (timer > 5.f) {
                        ScrollSpeed += 100;
                        timer = 0;
                    }
                    if (SpawnTimer > SpawnRate) {
                        FetchObstacle();
                        SpawnTimer = 0.f;
                    }
                    stickman_anim.update(System.currentTimeMillis());
                }

                if(nearestObstacle.isActive())
                {
                    // Detecting user tap for tapping obstacle
                    if (nearestObstacle.getType() == Obstacle.TYPE.T_TAP && Tapped == true) {
                        score += 10;
                        nearestObstacle.setActive(false);
                        DirectionVector.SetZero();
                        Tapped = false;
                    }
                    // Detecting user swipe direction for direction obstacle
                    else if (DirectionVector.IsZero() == false && Obstacle.fromInteger(ProcessSwipe(DirectionVector)) == nearestObstacle.getType()) {
                        score += 10;
                        nearestObstacle.setActive(false);
                        DirectionVector.SetZero();
                    }

                    DirectionVector.SetZero();
                }


                //Updating game elements
                for(int i = 0; i < obstacleList.length; ++i)
                {
                    if(obstacleList[i].isActive())
                    {
                        obstacleList[i].setPosX(obstacleList[i].getPosX() - ScrollSpeed * dt);
                        //if out of screen
                        if(obstacleList[i].getPosX() < 0){
                            obstacleList[i].setActive(false);
                        }
                        //Only if game is active we check these collisions
                        if(GameActive == true) {
                            //Player collision against obstacles
                            if (CheckCollision(stickman_anim.getX(), stickman_anim.getY(),
                                    stickman_anim.getSpriteWidth(), stickman_anim.getSpriteHeight(),
                                    (int) obstacleList[i].getPosX(), (int) obstacleList[i].getPosY(),
                                    obstacleList[i].getImgWidth(), obstacleList[i].getImgHeight())) {
                                obstacleList[i].setActive(false);
                                GameActive = false; //Game status set to false
                                //Enable buttons
                                Restart_button.setActive(true);
                                Mainmenu_button.setActive(true);
                            }
                            //Get nearest obstacle
                            if (nearestObstacle.isActive() == false) {
                                nearestObstacle = obstacleList[i];
                            } else if (obstacleList[i].getPosX() < nearestObstacle.getPosX()) {
                                nearestObstacle = obstacleList[i];
                            }
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

    public boolean CheckTouch(float touch_x,float touch_y, float min_x,float min_y, int max_x,int max_y)
    {
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
                    obstacleList[i].SetAllData(ScreenWidth, 525, BitmapFactory.decodeResource(getResources(),
                            R.drawable.tap_obstacle2), Obstacle.TYPE.T_TAP, 10, true);
                    break;
                }
                else if(result >=60 && result < 80){
                    obstacleList[i].SetAllData(ScreenWidth, 525, BitmapFactory.decodeResource(getResources(),
                            R.drawable.left_obstacle2), Obstacle.TYPE.T_LEFT, 10, true);
                    break;
                }
                else if(result >=40 && result < 60){
                    obstacleList[i].SetAllData(ScreenWidth, 525, BitmapFactory.decodeResource(getResources(),
                            R.drawable.up_obstacle2), Obstacle.TYPE.T_UP, 10, true);
                    break;
                }
                else if(result >=20 && result < 40){
                    obstacleList[i].SetAllData(ScreenWidth, 525, BitmapFactory.decodeResource(getResources(),
                            R.drawable.down_obstacle2), Obstacle.TYPE.T_DOWN, 10, true);
                    break;
                }
                else if(result >=0 && result < 20){
                    obstacleList[i].SetAllData(ScreenWidth, 525, BitmapFactory.decodeResource(getResources(),
                            R.drawable.right_obstacle2), Obstacle.TYPE.T_RIGHT, 10, true);
                    break;
                }
                else{
                    obstacleList[i].SetAllData(ScreenWidth, 525, BitmapFactory.decodeResource(getResources(),
                            R.drawable.tap_obstacle2), Obstacle.TYPE.T_TAP, 10, true);
                    break;
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        //Only process if game is active
        if(GameActive)
        {
            // If the next obstacle is not a tap type check for swipe
            if (nearestObstacle.getType() != Obstacle.TYPE.T_TAP && FingerDown == false) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        InitialPos.Set(event.getX(), event.getY());
                    }
                    break;
                    case MotionEvent.ACTION_MOVE:
                        LastPos.Set(event.getX(), event.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        DirectionVector.Set(LastPos.operatorMinus(InitialPos));
                        break;
                }
            }
            // else check for tap
            else {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    FingerDown = true;
                    Tapped = true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    FingerDown = false;
                }
            }
            return true;
        }
        //To process other taps while game is not active
        else
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //If touch restart button
                if(CheckTouch(event.getX(),event.getY(),Restart_button.getPosX(),Restart_button.getPosY(),
                        (int)Restart_button.getPosX() + Restart_button.getImgWidth(), (int)Restart_button.getPosY() + Restart_button.getImgHeight()))
                {
                    //Restart the game
                    mp.start();
                    Reset();
                }
                //If touch mainmenu button
                else if(CheckTouch(event.getX(),event.getY(),Mainmenu_button.getPosX(),Mainmenu_button.getPosY(),
                        (int)Mainmenu_button.getPosX() + Mainmenu_button.getImgWidth(), (int)Mainmenu_button.getPosY() + Mainmenu_button.getImgHeight()))
                {
                    mp.start();
                    Intent intent = new Intent();
                    intent.setClass(getContext(),Mainmenu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getContext().startActivity(intent);
                }
            }
            return true;
        }
    }

    public int ProcessSwipe(Vector2 SwipeDirection) {
        float x = SwipeDirection.x;
        float y = SwipeDirection.y;

        // x more than 0
        if (0 < x) {
            // y more than 0
            if (0 < y) {
                // Since x & y positive check which bigger
                // x more than y hence direction right
                if (x > y) {
                    return 2;
                }
                // y more than x hence direction down
                else {
                    return 4;
                }
            }
            // y less than 0
            else
            {
                // Check x or y(converted to positive) which is bigger
                // x bigger than y when positive hence direction right
                if(x > (-1 * y))
                {
                    return 2;
                }
                // y when positive is bigger than x hence direction up
                else
                {
                    return 3;
                }
            }
        }
        // x less than 0
        else {
            // y more than 0
            if (0 < y) {
                // Since x & y positive check which bigger
                // x when positive more than y hence direction left
                if ((-1 * x) > y) {
                    return 1;
                }
                // y more than x when positive hence direction down
                else {
                    return 4;
                }
            }
            // y less than 0
            else {
                // Check x or y(converted to positive) which is bigger
                // x when positive bigger than y when positive hence direction left
                if ((-1 * x) > (-1 * y)) {
                    return 1;
                }
                // y when positive is bigger than x hence direction up
                else {
                    return 3;
                }
            }
        }
    }

    //Restart game variables
    public void Reset()
    {
        //Reset all game elements
        for(int i = 0; i < obstacleList.length; ++i)
        {
            if(obstacleList[i].isActive()) {
                obstacleList[i].setActive(false);
            }
        }
        nearestObstacle = obstacleList[0];
        SpawnRate = 0.5f;
        SpawnTimer = 0.f;
        ScrollSpeed = 500;
        timer = 0.f;
        score = 0;
        Restart_button.setActive(false);
        Mainmenu_button.setActive(false);
        Tapped = false;
        FingerDown = false;
        //Reset everything first before we set game active back to true
        GameActive = true;
    }
}
