package com.aght.offlinereader;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.webkit.WebView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class AdBlockWebView extends WebView {

    private AdBlockWebViewClient adBlockWebViewClient;
    private String currentUrl;
    private Context context;

    public AdBlockWebView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AdBlockWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public AdBlockWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
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
        adBlockWebViewClient = new AdBlockWebViewClient(getFilterData());
        setWebViewClient(adBlockWebViewClient);
        getSettings().setJavaScriptEnabled(true);
    }

    private byte[] getFilterData() {
        try {
            return IOUtils.toByteArray(context.getAssets().open("filter.dat"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}