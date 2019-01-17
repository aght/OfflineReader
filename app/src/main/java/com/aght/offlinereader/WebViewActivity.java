package com.aght.offlinereader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aght.offlinereader.adblock.webview.AdBlockWebView;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        getSupportActionBar().hide();

        AdBlockWebView webView = findViewById(R.id.ad_block_webView);

        webView.loadUrl("https://ads-blocker.com/testing/");
//        webView.saveWebArchive("");
    }
}
