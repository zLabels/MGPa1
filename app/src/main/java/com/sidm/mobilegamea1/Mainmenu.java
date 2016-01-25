package com.sidm.mobilegamea1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.media.MediaPlayer;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class Mainmenu extends Activity implements OnClickListener{

    //Buttons
    private Button btn_play;
    private Button btn_options;
    private Button btn_highscore;

    //Sound manager
    SoundManager soundManager;

    //Facebook
    TextView userName;
    private LoginButton loginBtn;

    //Share prefs
    AppPrefs appPrefs;

    //Managers
    private CallbackManager callbackManager;
    private LoginManager loginManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        Context context = getApplicationContext();
        //Initializing facebook
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        FacebookSdk.setApplicationId(getResources().getString(R.string.app_id));
        setContentView(R.layout.mainmenu);

        callbackManager = CallbackManager.Factory.create();

        List<String> PERMISSIONS = Arrays.asList("publish_actions");

        userName = (TextView) findViewById(R.id.user_name);
        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        loginManager = LoginManager.getInstance();
        loginManager.logInWithPublishPermissions(this, PERMISSIONS);
        loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                sharePhotoToFacebook();
            }

            @Override
            public void onCancel() {
                userName.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                userName.setText("Login attempt failed.");
            }
        });


        //Context context = getApplicationContext();
        appPrefs = new AppPrefs(context);

        appPrefs.CheckIfExist();

        soundManager = new SoundManager();

        //Checking if sound manager is made
        if(!soundManager.IsInited())
        {
            soundManager.InitSoundPool(context, appPrefs);
            soundManager.PlayBGM();
        }

        //Initializing buttons
        btn_play = (Button)findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this);

        btn_options = (Button)findViewById(R.id.btn_options);
        btn_options.setOnClickListener(this);

        btn_highscore = (Button)findViewById(R.id.btn_highscore);
        btn_highscore.setOnClickListener(this);
    }

    //Sharing photo to facebook
    private void sharePhotoToFacebook(){
        int highscore = 0;

        //Getting highest score
        highscore = appPrefs.getHighscore().get(0);

        //Setting image as launcher icon
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption("I'm playing Doodle Run! My highest score is " + highscore + ".")
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //On tapping one of the mainmenu buttons
    public void onClick(View v) {
        Intent intent = new Intent();

        soundManager.PlaySFX();

        //Setting intent based on button input
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

        //Start the new activity
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
