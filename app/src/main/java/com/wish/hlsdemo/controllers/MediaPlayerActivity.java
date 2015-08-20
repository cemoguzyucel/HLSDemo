package com.wish.hlsdemo.controllers;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wish.hlsdemo.MainActivity;
import com.wish.hlsdemo.R;

import java.io.IOException;


public class MediaPlayerActivity extends ActionBarActivity {


/*
    String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
*/
    String url = "http://content.jwplatform.com/manifests/vM7nH0Kl.m3u8";

    MediaPlayer mediaPlayer;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    ProgressBar progressBarMediaPlayer;

    int lastOrientation = 0;
    int screenHeight;       //check and delete if nes
    int screenWidth;
    double screenRatio;
    RunnableProgressBar progressRunnable;
    boolean isMediaPlayerPrepared=false;

    TextView textViewProgressMediaPlayer;         //fragmenta al
    TextView textViewDurationMediaPlayer;
    TextView textViewVideoStateMediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        obtainScreenSize();
        progressRunnable = new RunnableProgressBar();

        progressBarMediaPlayer = (ProgressBar) findViewById(R.id.progressBarMediaPlayer);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        textViewProgressMediaPlayer = (TextView) findViewById(R.id.textViewProgressMediaPlayer);
        textViewDurationMediaPlayer = (TextView) findViewById(R.id.textViewDurationMediaPlayer);
        textViewVideoStateMediaPlayer = (TextView) findViewById(R.id.textViewVideoStateMediaPlayer);

        textViewVideoStateMediaPlayer.setText(getString(R.string.label_playing));
        textViewVideoStateMediaPlayer.setTextColor(Color.GREEN);               //metotlaştır alttaki 3uyle birlikte
        textViewProgressMediaPlayer.setTextColor(Color.GREEN);
        textViewDurationMediaPlayer.setTextColor(Color.GREEN);

        surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                screenWidth / 2));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)surfaceView.getLayoutParams();

        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        surfaceView.setLayoutParams(params);
        surfaceHolder = surfaceView.getHolder();

        Thread thread = new Thread(progressRunnable);
        thread.start();

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDisplay(surfaceHolder);


                try {
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare();
                    isMediaPlayerPrepared=true;
                } catch (IOException e) {
                    e.printStackTrace();
                }


                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                    }
                });

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }


        });



    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (lastOrientation != newConfig.orientation) {
            lastOrientation = newConfig.orientation;
            if(lastOrientation==2){
                getSupportActionBar().hide();
                surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                        RelativeLayout.LayoutParams.FILL_PARENT));
            }

            if(lastOrientation==1){

                getSupportActionBar().show();

                surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                        screenWidth/2));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)surfaceView.getLayoutParams();

                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                surfaceView.setLayoutParams(params);

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mediaPlayer!=null)
            mediaPlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mediaPlayer!=null)
            mediaPlayer.start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_media_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_video_view:
                startActivity(new Intent(this, VideoViewActivity.class));
                return true;
            case R.id.action_webview:
                startActivity(new Intent(this, WebViewActivity.class));
                return true;
            case R.id.action_main_menu:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public  void obtainScreenSize(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        screenHeight= metrics.heightPixels;
        screenWidth= metrics.widthPixels;
        screenRatio = screenHeight/screenWidth;


    }

    private class RunnableProgressBar implements Runnable{

        @Override
        public void run() {
            while(true){
                if(mediaPlayer != null && isMediaPlayerPrepared){
                    final int totalDuration = mediaPlayer.getDuration();
                    final int currentDuration = mediaPlayer.getCurrentPosition();
                    final int currentTime = (int)(100 * currentDuration/totalDuration);

                    progressBarMediaPlayer.setProgress(currentTime);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewProgressMediaPlayer.setText(currentTime + "%");
                            textViewDurationMediaPlayer.setText((totalDuration - currentDuration) / 1000 + "secs");

                        }
                    });
                }else{
                    progressBarMediaPlayer.setProgress(0);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    public void togglePlay(View v){

        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            textViewVideoStateMediaPlayer.setText(getString(R.string.label_paused));
            textViewVideoStateMediaPlayer.setTextColor(Color.RED);
            textViewProgressMediaPlayer.setTextColor(Color.RED);
            textViewDurationMediaPlayer.setTextColor(Color.RED);
        }
        else{
            mediaPlayer.start();
            textViewVideoStateMediaPlayer.setText(getString(R.string.label_playing));
            textViewVideoStateMediaPlayer.setTextColor(Color.GREEN);
            textViewProgressMediaPlayer.setTextColor(Color.GREEN);
            textViewDurationMediaPlayer.setTextColor(Color.GREEN);
        }
    }
}
