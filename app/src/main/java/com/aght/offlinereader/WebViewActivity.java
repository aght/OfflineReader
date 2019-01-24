package com.aght.offlinereader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.aght.offlinereader.adblock.webview.AdBlockProvider;
import com.aght.offlinereader.adblock.webview.AdBlockWebView;
import com.aght.offlinereader.database.WebPage;
import com.aght.offlinereader.database.WebPageDatabase;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = WebViewActivity.class.getSimpleName();

    private AdBlockWebView adBlockWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getSupportActionBar().hide();

        Intent i = getIntent();
        String url = i.getStringExtra("url");
        WebPage page = WebPageDatabase.getInstance().access().findByUrl(url);

        adBlockWebView = findViewById(R.id.ad_block_webView);
        adBlockWebView.setWebViewClient(getWebViewClient());
        adBlockWebView.setWebChromeClient(getWebChromeClient());

        adBlockWebView.getSettings().setJavaScriptEnabled(true);

        String path = getFilesDir().getAbsolutePath() + File.separator + page.getFileName();
        Log.d(TAG, "Loading: " + path);

        adBlockWebView.loadUrl("file://" + path);  //Loads blanks
    }

    private WebViewClient getWebViewClient() {
        return new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String url) {
                Toast.makeText(WebViewActivity.this, "Finished Loading!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private WebChromeClient getWebChromeClient() {
        return new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                Log.d(TAG, "Progress: " + String.valueOf(newProgress));
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (adBlockWebView.canGoBack()) {
            adBlockWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public void onDestroy() {
        adBlockWebView.destroy();
        super.onDestroy();
    }
}
