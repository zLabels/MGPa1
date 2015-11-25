package com.sidm.mobilegamea1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Playpage extends Activity implements OnClickListener{

    private Button btn_playback;
    private Button btn_adventure;
    private Button btn_endless;
    private Button btn_tutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(R.layout.playpage);

        btn_playback = (Button)findViewById(R.id.btn_playback);
        btn_playback.setOnClickListener(this);

        btn_adventure = (Button)findViewById(R.id.btn_adventure);
        btn_adventure.setOnClickListener(this);

        btn_endless = (Button)findViewById(R.id.btn_endless);
        btn_endless.setOnClickListener(this);

        btn_tutorial = (Button)findViewById(R.id.btn_tutorial);
        btn_tutorial.setOnClickListener(this);

    }

    public void onClick(View v) {
        Intent intent = new Intent();

        if(v == btn_playback)
        {
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
