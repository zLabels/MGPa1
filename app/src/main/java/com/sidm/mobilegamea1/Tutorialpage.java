package com.sidm.mobilegamea1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.media.MediaPlayer;

public class Tutorialpage extends Activity implements OnClickListener{

    //Buttons
    private Button btn_tutorialback;

    //Media Player
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(R.layout.tutorialpage);

        btn_tutorialback = (Button)findViewById(R.id.btn_tutorialback);
        btn_tutorialback.setOnClickListener(this);

        mp = MediaPlayer.create(this, R.raw.menu_feedback);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        mp.start();

        if(v == btn_tutorialback)
        {
            intent.setClass(this,Playpage.class);
        }

        startActivity(intent);
    }

    protected void onPause(){
        super.onPause();
    }

    protected void onStop(){
        super.onStop();
    }

    protected void onDestroy(){
        super.onDestroy();
    }
}
