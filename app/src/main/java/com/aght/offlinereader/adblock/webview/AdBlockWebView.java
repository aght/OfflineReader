package com.aght.offlinereader.adblock.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class AdBlockWebView extends WebView {

    private AdBlockWebViewClient adBlockWebViewClient;
    private AdBlockWebChromeClient adBlockWebChromeClient;
    private String currentUrl;

    public AdBlockWebView(Context context) {
        super(context);
        init();
    }

    public AdBlockWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdBlockWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void loadUrl(String url) {
        currentUrl = url;
        super.loadUrl(currentUrl);
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    private void init() {
        adBlockWebViewClient = new AdBlockWebViewClient();
        adBlockWebChromeClient = new AdBlockWebChromeClient();

        setWebViewClient(adBlockWebViewClient);
        setWebChromeClient(adBlockWebChromeClient);

        getSettings().setJavaScriptEnabled(true);
    }
}