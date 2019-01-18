package com.aght.offlinereader.adblock.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.aght.offlinereader.OfflineReader;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class AdBlockWebView extends WebView {

    private static final String TAG = "AdBlockWebView";

    private static final String AD_BLOCK_DATA_FILE = "filter.dat";

    private AdBlockWebViewClient adBlockWebViewClient;
    private AdBlockWebChromeClient adBlockWebChromeClient;
    private AdBlockProvider adBlockProvider;
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

    public void setAdBlockProvider(AdBlockProvider provider) {
        this.adBlockProvider.destroy();
        this.adBlockProvider = provider;
        adBlockWebViewClient = new AdBlockWebViewClient(adBlockProvider);
        setWebViewClient(adBlockWebViewClient);
    }

    private void init() {
        adBlockProvider = new AdBlockProvider(getFileBytes(AD_BLOCK_DATA_FILE));
        adBlockWebViewClient = new AdBlockWebViewClient(adBlockProvider);
        adBlockWebChromeClient = new AdBlockWebChromeClient();

        setWebViewClient(adBlockWebViewClient);
        setWebChromeClient(adBlockWebChromeClient);

        getSettings().setJavaScriptEnabled(true);
    }

    private byte[] getFileBytes(String filename) {
        try {
            InputStream dataStream = OfflineReader.getContext().getAssets().open(filename);
            return IOUtils.toByteArray(dataStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void destroy() {
        adBlockProvider.destroy();
        super.destroy();
    }
}