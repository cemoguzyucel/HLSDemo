package com.wish.hlsdemo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.wish.hlsdemo.controllers.MediaPlayerActivity;
import com.wish.hlsdemo.controllers.VideoViewActivity;
import com.wish.hlsdemo.controllers.WebViewActivity;


public class MainActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_video_view:
                startActivity(new Intent(this, VideoViewActivity.class));
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




}
