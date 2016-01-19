package com.sidm.mobilegamea1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.SoundPool;
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
    SoundManager soundManager;
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

        soundManager = new SoundManager();

        if(!soundManager.IsInited())
        {
            soundManager.InitSoundPool(context, appPrefs);
            soundManager.PlayBGM();
        }

        btn_play = (Button)findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this);

        btn_options = (Button)findViewById(R.id.btn_options);
        btn_options.setOnClickListener(this);

        btn_highscore = (Button)findViewById(R.id.btn_highscore);
        btn_highscore.setOnClickListener(this);
    }

    public void onClick(View v) {
        Intent intent = new Intent();

        soundManager.PlaySFX();

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
        //soundManager.PauseBGM();
        super.onPause();
    }

    protected void onResume(){
        //soundManager.UnPauseBGM();
        super.onResume();
    }

    protected void onStop(){
        super.onStop();
    }

    protected void onDestroy(){
        super.onDestroy();
    }
}
