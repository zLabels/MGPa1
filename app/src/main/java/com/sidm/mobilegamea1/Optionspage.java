package com.sidm.mobilegamea1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.Vector;

public class Optionspage extends Activity implements OnClickListener{

    private Button btn_optionsback;
    private SeekBar sb_backgroundMusic, sb_sfxSound;
    private TextView tv_backgroundMusic, tv_sfxSound;
    private String backgroundMusic, sfxSound;
    AppPrefs appPrefs;
    SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(R.layout.optionspage);

        btn_optionsback = (Button)findViewById(R.id.btn_optionsback);
        btn_optionsback.setOnClickListener(this);

       Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/LoveSweets.ttf");

        backgroundMusic = "Background Music";
        sfxSound = "SFX Sound";

        tv_backgroundMusic = (TextView)findViewById(R.id.tv_backgroundMusic);
        tv_backgroundMusic.setText(backgroundMusic);
        tv_backgroundMusic.setTypeface(typeface);

        tv_sfxSound = (TextView)findViewById(R.id.tv_sfxSound);
        tv_sfxSound.setText(sfxSound);
        tv_sfxSound.setTypeface(typeface);

        sb_backgroundMusic = (SeekBar) findViewById(R.id.sb_backgroundMusicSeekbar);
        sb_backgroundMusic.setOnSeekBarChangeListener(customSeekBarlistener);

        sb_sfxSound = (SeekBar) findViewById(R.id.sb_sfxSoundSeekbar);
        sb_sfxSound.setOnSeekBarChangeListener(customSeekBarlistener);

        Context context = getApplicationContext();
        appPrefs = new AppPrefs(context);

        Vector<Integer> Volumes = new Vector<Integer>();

        Volumes = appPrefs.getVolume();

        sb_backgroundMusic.setProgress(Volumes.get(0));

        sb_sfxSound.setProgress(Volumes.get(1));

        soundManager = new SoundManager();
    }

    private SeekBar.OnSeekBarChangeListener customSeekBarlistener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            if(seekBar == sb_backgroundMusic)
            {
                appPrefs.setVolume(0, progress);
                //
            }
            else if(seekBar == sb_sfxSound)
            {
                appPrefs.setVolume(1, progress);
                //
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        soundManager.PlaySFX();

        if(v == btn_optionsback)
        {
            soundManager.SetBGMVolume(appPrefs.getVolume().get(0));
            soundManager.SetSFXVolume(appPrefs.getVolume().get(1));
            intent.setClass(this,Mainmenu.class);
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
