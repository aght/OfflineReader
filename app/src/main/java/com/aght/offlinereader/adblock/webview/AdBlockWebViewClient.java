package com.aght.offlinereader.adblock.webview;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AdBlockWebViewClient extends WebViewClient {

    private static final String TAG = "AdBlockWebViewClient";

    private AdBlockProvider adBlockProvider;

    public AdBlockWebViewClient() {
        this.adBlockProvider = AdBlockProvider.newInstance();
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        AdBlockWebView adBlockWebView = (AdBlockWebView) view;

        String currentPageDomain = adBlockWebView.getCurrentUrl();
        String urlToCheck = request.getUrl().toString();

        if (adBlockProvider.shouldBlockUrl(currentPageDomain, urlToCheck)) {
            return createEmptyResponse();
        }

        return super.shouldInterceptRequest(view, request);
    }

    public void destroy() {
        adBlockProvider.destroy();
    }

    private WebResourceResponse createEmptyResponse() {
        return new WebResourceResponse("text/plain", "UTF-8", null);
    }
}
