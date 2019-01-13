package com.aght.offlinereader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        AdBlockWebView webView = (AdBlockWebView) findViewById(R.id.ad_block_webView);

        webView.loadUrl("https://xantandminions.wordpress.com/kuma-kuma-kuma-bear/the-bears-bear-a-bare-kuma-chapter-216/");
//        webView.saveWebArchive("");
    }
}
