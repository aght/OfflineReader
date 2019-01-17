package com.aght.offlinereader.adblock.webview;

import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aght.offlinereader.OfflineReader;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class AdBlockWebViewClient extends WebViewClient {

    static {
        System.loadLibrary("ad-block-lib");
        initAdBlockClient();
    }

    private static final String TAG = "AdBlockWebViewClient";

    // Native code requires this object to persist
    private static byte[] filterData;

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

    private static void initAdBlockClient() {
        try {
            filterData = IOUtils.toByteArray(OfflineReader
                    .getContext()
                    .getAssets()
                    .open("filter.dat"));

            initAdBlockClient(filterData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static native boolean initAdBlockClient(@NonNull byte[] filterBytes);

    private native boolean shouldBlockUrl(@NonNull String currentPageDomain, @NonNull String urlToCheck);
}
