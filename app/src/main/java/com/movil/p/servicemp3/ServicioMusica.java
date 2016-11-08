package com.movil.p.servicemp3;

/**
 * Created by miguel on 5/11/16.
 */


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class ServicioMusica extends Service {

    static MediaPlayer mMediaPlayer = null;
    private Thread workerThread = null;
    private static final int REQUEST_CODE_PLAY = 0;
    private static final int REQUEST_CODE_PAUSE = 1;
    private static final String ACTION_PLAY = "com.movil.p.servicemp3.action.PLAY";
    private static final String ACTION_PAUSE = "com.movil.p.servicemp3.action.PAUSE";
    private static final String EXTRA_URI = "com.movil.p.servicemp3.extras.URI";

    static public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    @Override
    public void onCreate() {
        Log.i("Mas trabajo","me llamaron");
/*
        // Crea un PendingIntent para iniciar la actividad ReproductorActivity
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), MainActivity.class),
                PendingIntent.FLAG_NO_CREATE);

// Creamos una notificación que lance el PendingIntentAnterior
        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("Reproductor de musica")
                .setContentText("Reproduciendo " + "Linkin Park")
                .setSmallIcon(R.drawable.icono_play)
                .setTicker("Reproduccion en curso")
                .setOngoing(true)
                .addAction(R.drawable.icono_play, "Reproductor de musica", pi)
                .build();


*/
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notificacion_audio);
        Intent intent;
        PendingIntent pendingIntent;
        intent = new Intent(ACTION_PLAY);
        pendingIntent = PendingIntent.getService(getApplicationContext(),
                REQUEST_CODE_PLAY, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.boton_play,
                pendingIntent);

        intent = new Intent(ACTION_PAUSE);
        pendingIntent = PendingIntent.getService(getApplicationContext(),
                REQUEST_CODE_PAUSE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.boton_pause,
                pendingIntent);

        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.icono_play)
                .setOngoing(true)
                .setContentTitle("Reproductor de musica")
                .setWhen(System.currentTimeMillis())
                .setContent(remoteViews)

                .build();
        startForeground(10, notification);
        mMediaPlayer = MediaPlayer.create(this, R.raw.audio);
    }
/*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //mMediaPlayer.setOnPreparedListener(this);
        //mMediaPlayer.prepareAsync();
        mMediaPlayer.start();
        return START_STICKY;

    }
*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.i("Mas trabajo","me llamaron");
        if (intent.getAction().equals(ACTION_PLAY)) {
            if (workerThread == null || !workerThread.isAlive()) {
                workerThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mMediaPlayer.start();
                    }
                });

                workerThread.start();
            }
        }
        if (intent.getAction().equals(ACTION_PAUSE)) {
            Toast.makeText(this,"Aqui estoy",
                    Toast.LENGTH_SHORT).show();


            if(mMediaPlayer != null) {
                mMediaPlayer.pause();
            }
        }


        return START_STICKY;
    }



            @Override
    public void onDestroy() {

        Toast.makeText(this,"Servicio detenido",
                Toast.LENGTH_SHORT).show();

        mMediaPlayer.stop();

    }

    @Override
    public IBinder onBind(Intent intencion) {/*
        Toast.makeText(this,"Servicio onBind",
                Toast.LENGTH_SHORT).show();

        if(workerThread == null || !workerThread.isAlive()){
            workerThread = new Thread(new Runnable(){
                @Override
                public void run() {
                    mMediaPlayer.start();
                }
            } );

            workerThread.start();
        }*/
        return null;
    }


}