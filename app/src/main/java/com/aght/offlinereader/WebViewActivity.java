package com.aght.offlinereader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aght.offlinereader.adblock.webview.AdBlockProvider;
import com.aght.offlinereader.adblock.webview.AdBlockWebView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class WebViewActivity extends AppCompatActivity {

    private AdBlockWebView adBlockWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getSupportActionBar().hide();

        adBlockWebView = findViewById(R.id.ad_block_webView);
        adBlockWebView.loadUrl("https://ads-blocker.com/testing/");
//        webView.saveWebArchive("");
    }


    @Override
    public void onDestroy() {
        adBlockWebView.destroy();
        super.onDestroy();
    }
}
