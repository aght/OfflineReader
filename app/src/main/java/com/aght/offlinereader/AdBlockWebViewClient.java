package com.aght.offlinereader;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AdBlockWebViewClient extends WebViewClient {

    static {
        System.loadLibrary("ad-block-lib");
    }

    private final String TAG = "AdBlockWebViewClient";

    private native boolean shouldBlockUrl(String domain, String url, byte[] bytes);

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        AdBlockWebView adblockWebView = (AdBlockWebView) view;
        if (shouldBlockUrl(adblockWebView.getCurrentUrl(), request.getUrl().toString(), adblockWebView.getFilterData())) {
            return createEmptyResponse();
        }

        return super.shouldInterceptRequest(view, request);
    }

    private WebResourceResponse createEmptyResponse() {
        return new WebResourceResponse("text/plain", "UTF-8", null);
    }
}
