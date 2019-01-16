package com.aght.offlinereader;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;

public class AdBlockWebView extends WebView {

    private AdBlockWebViewClient adBlockWebViewClient;
    private String currentUrl;
    private byte[] filterData;

    public AdBlockWebView(Context context) {
        super(context);
        initAdBlock();
    }

    public AdBlockWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAdBlock();
    }

    public AdBlockWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAdBlock();
    }

    @Override
    public void loadUrl(String url) {
        currentUrl = url;
        super.loadUrl(currentUrl);
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public byte[] getFilterData() {
        return filterData;
    }

    private void initAdBlock() {
        adBlockWebViewClient = new AdBlockWebViewClient();
        setWebViewClient(adBlockWebViewClient);

//        filterData = readFilterBytes();
    }
}