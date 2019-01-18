package com.aght.offlinereader.adblock.webview;

import android.support.annotation.NonNull;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aght.offlinereader.OfflineReader;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class AdBlockWebViewClient extends WebViewClient {

    private static final String TAG = "AdBlockWebViewClient";

    private AdBlockProvider adBlockProvider;

    public AdBlockWebViewClient(AdBlockProvider provider) {
        this.adBlockProvider = provider;
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

    private WebResourceResponse createEmptyResponse() {
        return new WebResourceResponse("text/plain", "UTF-8", null);
    }
}
