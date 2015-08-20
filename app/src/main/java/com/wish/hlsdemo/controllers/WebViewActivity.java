package com.wish.hlsdemo.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.wish.hlsdemo.MainActivity;
import com.wish.hlsdemo.R;

import java.lang.reflect.InvocationTargetException;


public class WebViewActivity extends ActionBarActivity {

    private WebView webView;
    int lastOrientation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setInitialScale(1);
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);

        String customHtml = "<html><head><title>Sample</title></head><body><meta name=\"viewport\" content=\"user-scalable=yes\"/><video controls><source src='http://content.jwplatform.com/manifests/vM7nH0Kl.m3u8'></video></body></html>";
/*
        String customHtml = "<html><head><title>Sample</title></head><body><div><video controls><source src='http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4'></video></div></body></html>";
*/

        webView.loadData(customHtml, "text/html", "UTF-8");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_view, menu);
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
            case R.id.action_main_menu:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (lastOrientation != newConfig.orientation) {
            lastOrientation = newConfig.orientation;


            //full-screen için uyarı ver

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
