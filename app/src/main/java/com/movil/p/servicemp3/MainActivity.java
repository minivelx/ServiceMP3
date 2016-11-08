package com.movil.p.servicemp3;

import android.os.Handler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private static final String ACTION_PLAY = "com.movil.p.servicemp3.action.PLAY";
    private static final String ACTION_PAUSE = "com.movil.p.servicemp3.action.PAUSE";

    private double startTime = 0;

    TextView tx1;
    SeekBar seekbar;
    private Handler myHandler = new Handler();
    public static int oneTimeOnly = 0;
    boolean touchSeekbar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tx1 = (TextView)findViewById(R.id.timer);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i("Oe1","aca");
                touchSeekbar = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i("Oe2","aca");
                touchSeekbar = true;

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(touchSeekbar){
                    ServicioMusica.getMediaPlayer().seekTo(progress);
                    seekbar.setProgress(progress);
                    touchSeekbar=false;
                }

            }
        });

        Button arrancar = (Button) findViewById(R.id.boton_arrancar);
        arrancar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {


                tx1.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        startTime)))
                );

                Intent intent = new Intent(MainActivity.this, ServicioMusica.class);
                intent.setAction(ACTION_PLAY);
                startService(intent);
                //bindService(new Intent(MainActivity.this, ServicioMusica.class), mServerConn, Context.BIND_AUTO_CREATE);

                seekbar.setProgress((int)startTime);
                myHandler.postDelayed(UpdateSongTime,100);

            }

        });

        Button detener = (Button) findViewById(R.id.boton_detener);

        detener.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                stopService(new Intent(MainActivity.this, ServicioMusica.class));

            }

        });

        Button pausar = (Button) findViewById(R.id.boton_pausar);

        pausar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ServicioMusica.class);
                intent.setAction(ACTION_PAUSE);
                startService(intent);

            }

        });

    }


    protected ServiceConnection mServerConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d("hola", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("hola", "onServiceDisconnected");
        }
    };

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {

            if (oneTimeOnly == 0) {
                seekbar.setMax((int) ServicioMusica.getMediaPlayer().getDuration());
                oneTimeOnly = 1;
            }
            startTime = ServicioMusica.getMediaPlayer().getCurrentPosition();
            tx1.setText(String.format("%d: %d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

}
