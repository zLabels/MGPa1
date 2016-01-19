package com.sidm.mobilegamea1;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * Created by Malcolm on 17/1/2016.
 */
public class SoundManager {
    final static SoundPool soundpool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    static MediaPlayer mediaPlayer;
    static int SFX;
    static float BGMVolume, SFXVolume;
    static boolean Inited = false;

    void InitSoundPool(Context context, AppPrefs appPrefs){
        mediaPlayer = MediaPlayer.create(context, R.raw.backgroundmusic);
        SFX = soundpool.load(context,R.raw.menu_feedback,1);
        BGMVolume = ((float)appPrefs.getVolume().get(0))/100;
        SFXVolume = ((float)appPrefs.getVolume().get(1))/100;
        mediaPlayer.setVolume(BGMVolume, BGMVolume);
        soundpool.setVolume(SFX, SFXVolume, SFXVolume);
        Inited = true;
    }

    void PlayBGM() {
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    void PlaySFX() {
        soundpool.play(SFX, SFXVolume, SFXVolume, 1, 0, 1);
    }

    void SetBGMVolume(int newVolume){
        BGMVolume = ((float)newVolume) / 100;
        mediaPlayer.setVolume(BGMVolume, BGMVolume);
    }

    void SetSFXVolume(int newVolume){
        SFXVolume = ((float)newVolume) / 100;
        soundpool.setVolume(SFX, SFXVolume, SFXVolume);
    }

    void PauseBGM(){
        mediaPlayer.pause();
    }

    void UnPauseBGM(){
        mediaPlayer.start();
    }

    boolean IsInited(){
        return Inited;
    }
}
