package com.movil.p.servicemp3;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

/**
 * Created by miguel on 7/11/16.
 */

public class ClaseGlobal extends Application {

    int [] songs = {R.raw.audio, R.raw.audio, R.raw.audio};
    int cursor = 0;
    boolean repetir = false;
    MediaPlayer mMediaPlayer;

    public ClaseGlobal() {
        //instanciamos el reproductor
        mMediaPlayer = MediaPlayer.create(this, songs[0]);
        cursor = 0;
    }

    public MediaPlayer getReproductor() {
        return mMediaPlayer;
    }

    public void setReproductor(int song){
        mMediaPlayer=null;
        mMediaPlayer = MediaPlayer.create(this, songs[song]);
    }

    public void avanzar(){
        cursor++;
        if(cursor==3){
            if(repetir) {
                cursor = 0;
                mMediaPlayer=null;
                mMediaPlayer = MediaPlayer.create(this, songs[cursor]);
            }
            else{

            }
        }

    }


}
