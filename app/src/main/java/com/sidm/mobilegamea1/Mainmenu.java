package com.sidm.mobilegamea1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.media.MediaPlayer;
import android.widget.TextView;

import java.util.Vector;

public class Mainmenu extends Activity implements OnClickListener{

    private Button btn_play;
    private Button btn_options;
    private Button btn_highscore;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(R.layout.mainmenu);

        Context context = getApplicationContext();
        AppPrefs appPrefs = new AppPrefs(context);

        appPrefs.CheckIfExist();

        mp = MediaPlayer.create(this, R.raw.menu_feedback);

        btn_play = (Button)findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this);

        btn_options = (Button)findViewById(R.id.btn_options);
        btn_options.setOnClickListener(this);

        btn_highscore = (Button)findViewById(R.id.btn_highscore);
        btn_highscore.setOnClickListener(this);
    }

    public void onClick(View v) {
        Intent intent = new Intent();

        mp.start();

        if(v == btn_play)
        {
            intent.setClass(this,Playpage.class);
        }
        else if( v == btn_options)
        {
            intent.setClass(this,Optionspage.class);
        }
        else if( v == btn_highscore)
        {
            intent.setClass(this,Highscorepage.class);
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
