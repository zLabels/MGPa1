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

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    // Implement this interface to receive information about changes to the surface.

    private GameThread myThread = null; // Thread to control the rendering

    // 1a) Variables used for background rendering
    private Bitmap bg, scaledbg;
    // 1b) Define Screen width and Screen height as integer
    int ScreenWidth, ScreenHeight;
    // 1c) Variables for defining background start and end point
    private short bgX = 0, bgY= 0;
    // 4a) bitmap array to stores 4 images of the spaceship
    private Bitmap[] ship = new Bitmap[4];
    // 4b) Variable as an index to keep track of the spaceship images
    private short shipindex;
    // Variables for FPS
    public float FPS;
    float deltaTime;
    long dt;
    Paint paint = new Paint();

    // Variable for touch
    private short mX = 0, mY = 0;

    // Stone animation
    SpriteAnimation stone_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.flystone), 320, 64, 5, 5);

    // Variable for Game State check
    private short GameState;

    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView (Context context){

        // Context is the current state of the application/object
        super(context);

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // 1d) Set information to get screen size
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;
        // 1e)load the image when this class is being instantiated
        bg = BitmapFactory.decodeResource(getResources(),
                R.drawable.gamescene);
        scaledbg = Bitmap.createScaledBitmap(bg, ScreenWidth,ScreenHeight,true);
        // 4c) Load the images of the spaceships
        ship[0] = BitmapFactory.decodeResource(getResources(),
                R.drawable.ship2_1);
        ship[1] = BitmapFactory.decodeResource(getResources(),
                R.drawable.ship2_2);
        ship[2] = BitmapFactory.decodeResource(getResources(),
                R.drawable.ship2_3);
        ship[3] = BitmapFactory.decodeResource(getResources(),
                R.drawable.ship2_4);


        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);

        stone_anim.setY(600);
    }

    public boolean CheckCollision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2)
    {
        if(x2>=x1 && x2<=x1+w1) // Start to detect collision of the top left corner
        {
            if(y2>=y1 && y2<=y1+h1) // Comparing yellow box to blue box
            {
                return true;
            }
        }

        if(x2+w2>=x1 && x2+w2<=x1+w1) // Top right corner
        {
            if(y2>=y1 && y2<=y1+h1)
            {
                return true;
            }
        }

        if(x2>=x1 && x2<=x1+w1) // Start to detect collision of the bottom left corner
        {
            if(y2>=y1 && y2<=y1) // Comparing yellow box to blue box
            {
                return true;
            }
        }

        if(x2+w2>=x1 && x2+w2<=x1+w1) // Bottom right corner
        {
            if(y2>=y1 && y2<=y1)
            {
                return true;
            }
        }

        return false;
    }

    //must implement inherited abstract methods
    public void surfaceCreated(SurfaceHolder holder){
        // Create the thread
        if (!myThread.isAlive()){
            myThread = new GameThread(getHolder(), this);
            myThread.startRun(true);
            myThread.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder){
        // Destroy the thread
        if (myThread.isAlive()){
            myThread.startRun(false);


        }
        boolean retry = true;
        while (retry) {
            try {
                myThread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    public void RenderGameplay(Canvas canvas) {
        // 2) Re-draw 2nd image after the 1st image ends
        if(canvas == null)
        {
            return;
        }
        canvas.drawBitmap(scaledbg, bgX, bgY,null);
        canvas.drawBitmap(scaledbg, bgX + ScreenWidth, bgY,null);
        // 4d) Draw the spaceships
        canvas.drawBitmap(ship[shipindex],mX,mY,null);

        stone_anim.draw(canvas);

        // Bonus) To print FPS on the screen
        Paint paint = new Paint();
        paint.setARGB(255,0,0,0);
        paint.setStrokeWidth(100);
        paint.setTextSize(30);
        canvas.drawText("FPS: " + FPS, 130, 75, paint);
    }

    //Update method to update the game play
    public void update(float dt, float fps){
        FPS = fps;

        switch (GameState) {
            case 0: {
                // 3) Update the background to allow panning effect
                bgX -= 500 * dt; //Change speed of the panning effect
                if (bgX < -ScreenWidth){
                    bgX = 0;
                }

                // 4e) Update the spaceship images / shipIndex so that the animation will occur.
                shipindex++;
                shipindex%=4;

                stone_anim.update(System.currentTimeMillis());
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

    @Override
    public boolean onTouchEvent(MotionEvent event){

        // 5) In event of touch on screen, the spaceship will relocate to the point of touch
        short X = (short) event.getX();
        short Y = (short) event.getY();

        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            // New location where the image to land on
            mX = (short)(X - ship[shipindex].getWidth()/2);
            mY = (short)(Y - ship[shipindex].getHeight()/2);
        }

        return super.onTouchEvent(event);
    }
}
