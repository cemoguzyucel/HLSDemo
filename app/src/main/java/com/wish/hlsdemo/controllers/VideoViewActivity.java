package com.wish.hlsdemo.controllers;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.wish.hlsdemo.MainActivity;
import com.wish.hlsdemo.R;


public class VideoViewActivity extends ActionBarActivity {

    private RelativeLayout videoViewLinearLayout;
    private VideoView videoView;
    private ProgressBar progressBarVideoView;

    private int lastOrientation = 0;
    private RunnableProgressBar progressRunnable;

    private TextView textViewProgressVideoView;
    private TextView textViewDuration;
    private TextView textViewVideoState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        videoViewLinearLayout = (RelativeLayout) findViewById(R.id.video_view_layout);
        videoView = (VideoView) findViewById(R.id.video_view);
        progressRunnable = new RunnableProgressBar();
        progressBarVideoView = (ProgressBar) findViewById(R.id.progressBarVideoView);
        textViewProgressVideoView = (TextView) findViewById(R.id.textViewProgressVideoView);
        textViewDuration = (TextView) findViewById(R.id.textViewDuration);
        textViewVideoState = (TextView) findViewById(R.id.textViewVideoState);

        textViewVideoState.setText(getString(R.string.label_playing));
        textViewVideoState.setTextColor(Color.GREEN);
        textViewProgressVideoView.setTextColor(Color.GREEN);
        textViewDuration.setTextColor(Color.GREEN);


        videoView.setVideoPath("http://content.jwplatform.com/manifests/vM7nH0Kl.m3u8");
/*        videoView.setVideoPath("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");*/

        Thread thread = new Thread(progressRunnable);
        thread.start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video_view, menu);
        return true;
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (lastOrientation != newConfig.orientation) {     //checks if orientation of the screen changed or not
            lastOrientation = newConfig.orientation;

            if(lastOrientation==2){     //horizontal
                getSupportActionBar().hide();
                videoViewLinearLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                        RelativeLayout.LayoutParams.FILL_PARENT));
                videoView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                        RelativeLayout.LayoutParams.FILL_PARENT));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)videoView.getLayoutParams();

                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                videoView.setLayoutParams(params);
            }

            else if(lastOrientation==1){    //vertical
                getSupportActionBar().show();
                videoViewLinearLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                        RelativeLayout.LayoutParams.FILL_PARENT));
                videoView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                        RelativeLayout.LayoutParams.FILL_PARENT));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)videoView.getLayoutParams();

                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                videoView.setLayoutParams(params);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(videoView!=null)
            videoView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(videoView!=null)
            videoView.start();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_main_menu:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.action_media_player:
                startActivity(new Intent(this, MediaPlayerActivity.class));
                return true;
            case R.id.action_webview:
                startActivity(new Intent(this, WebViewActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class RunnableProgressBar implements Runnable{          //observers and manipulates the state of progress bar

        @Override
        public void run() {
            while(true){
                if(videoView != null){
                    final int totalDuration = videoView.getDuration();
                    final int currentDuration = videoView.getCurrentPosition();
                    final int currentTime = (int)(100 * currentDuration/totalDuration);
                    progressBarVideoView.setProgress(currentTime);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewProgressVideoView.setText(currentTime + "%");
                            textViewDuration.setText((totalDuration-currentDuration)/1000+"secs");

                        }
                    });

                } else {
                    progressBarVideoView.setProgress(0);
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
        if(videoView.isPlaying()){
            videoView.pause();
            textViewVideoState.setText(getString(R.string.label_paused));
            textViewVideoState.setTextColor(Color.RED);
            textViewProgressVideoView.setTextColor(Color.RED);
            textViewDuration.setTextColor(Color.RED);
        }
        else{
            videoView.start();
            textViewVideoState.setText(getString(R.string.label_playing));
            textViewVideoState.setTextColor(Color.GREEN);
            textViewProgressVideoView.setTextColor(Color.GREEN);
            textViewDuration.setTextColor(Color.GREEN);
        }
    }
}
