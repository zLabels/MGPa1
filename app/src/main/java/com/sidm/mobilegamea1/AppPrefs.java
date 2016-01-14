package com.sidm.mobilegamea1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Vector;

/**
 * Created by Malcolm on 31/12/2015.
 */
public class AppPrefs {
    private static final String Highscore = "Highscores";
    private SharedPreferences highscore_appSharedPrefs;
    private SharedPreferences.Editor highscore_prefsEditor;
    Vector<String> scoreNames;

    private static final String Sound = "Sound";
    private SharedPreferences sound_appSharedPrefs;
    private SharedPreferences.Editor sound_prefsEditor;
    Vector<String> soundNames;

    public AppPrefs(Context context)
    {
        this.highscore_appSharedPrefs = context.getSharedPreferences(Highscore, Activity.MODE_PRIVATE);
        this.highscore_prefsEditor = highscore_appSharedPrefs.edit();
        this.scoreNames = new Vector<String>();
        scoreNames.addElement("Score1");
        scoreNames.addElement("Score2");
        scoreNames.addElement("Score3");
        scoreNames.addElement("Score4");
        scoreNames.addElement("Score5");

        this.sound_appSharedPrefs = context.getSharedPreferences(Sound, Activity.MODE_PRIVATE);
        this.sound_prefsEditor = sound_appSharedPrefs.edit();
        this.soundNames = new Vector<String>();
        soundNames.addElement("BackgroundVolume");
        soundNames.addElement("SFXVolume");
    }

    public void CheckIfExist()
    {
        if(!highscore_appSharedPrefs.contains(scoreNames.get(4)))
        {
            this.InitHighscore();
        }

        if(!sound_appSharedPrefs.contains(soundNames.get(1)))
        {
            this.InitSound();
        }
    }

    public void InitHighscore()
    {
        for(int i = 0; i < scoreNames.size(); ++i)
        {
            highscore_prefsEditor.putInt(scoreNames.get(i), 0).commit();
        }
    }

    public void InitSound()
    {
        for(int i = 0; i < soundNames.size(); ++i)
        {
            sound_prefsEditor.putInt(soundNames.get(i), 100).commit();
        }
    }

    public Vector<Integer> getHighscore()
    {
        Vector<Integer> Highscore = new Vector<Integer>();

        for(int i = 0; i < scoreNames.size(); ++i)
        {
            Highscore.addElement(highscore_appSharedPrefs.getInt(scoreNames.get(i), 0));
        }
        return Highscore;
    }

    public Vector<Integer> getVolume()
    {
        Vector<Integer> Volume = new Vector<Integer>();

        for(int i = 0; i < soundNames.size(); ++i)
        {
            Volume.addElement(sound_appSharedPrefs.getInt(soundNames.get(i), 0));
        }
        return Volume;
    }

    public void setHighscore(int index, int value)
    {
        highscore_prefsEditor.putInt(scoreNames.get(index), value).commit();
    }

    public void setVolume(int index, int value)
    {
        sound_prefsEditor.putInt(soundNames.get(index), value).commit();
    }
}
