package com.aght.offlinereader;

import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AdBlockWebViewClient extends WebViewClient {

    static {
        System.loadLibrary("ad-block-lib");
    }

    private static final String TAG = "AdBlockWebViewClient";

    private byte[] filterData;

    public AdBlockWebViewClient(byte[] filterData) {
        if (filterData == null) {
            filterData = new byte[]{0};
        }

        this.filterData = filterData;

        initAdBlocker(this.filterData);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        AdBlockWebView adBlockWebView = (AdBlockWebView) view;

        String currentPageDomain = adBlockWebView.getCurrentUrl();
        String urlToCheck = request.getUrl().toString();

        if (shouldBlockUrl(currentPageDomain, urlToCheck)) {
            return createEmptyResponse();
        }

        return super.shouldInterceptRequest(view, request);
    }

    private WebResourceResponse createEmptyResponse() {
        return new WebResourceResponse("text/plain", "UTF-8", null);
    }

    private void testAdBlocker() {
        String domain = "https://stackoverflow.com/questions/10972577/c-cmake-add-non-built-files";
        String url = "https://secure.quantserve.com/quant.js";
        Log.e(TAG, String.valueOf(shouldBlockUrl(domain, url)));
    }

    private native boolean initAdBlocker(@NonNull byte[] filterBytes);

    private native boolean shouldBlockUrl(@NonNull String currentPageDomain, @NonNull String urlToCheck);
}
