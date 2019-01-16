package com.aght.offlinereader;

import android.content.res.Resources;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class AdBlockWebViewClient extends WebViewClient {

    static {
        System.loadLibrary("ad-block-lib");
        initAdBlocker();
    }

    private static final String TAG = "AdBlockWebViewClient";

    private static final String FILTER_FILE_NAME = "filter.dat";

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        AdBlockWebView adBlockWebView = (AdBlockWebView) view;

        if (shouldBlockUrl(adBlockWebView.getCurrentUrl(), request.getUrl().toString())) {
            return createEmptyResponse();
        }

        return super.shouldInterceptRequest(view, request);
    }

    private WebResourceResponse createEmptyResponse() {
        return new WebResourceResponse("text/plain", "UTF-8", null);
    }

    private static void initAdBlocker() {
        try {
            InputStream filterStream = Resources.getSystem().getAssets().open(FILTER_FILE_NAME);
            byte[] filterBytes = IOUtils.toByteArray(filterStream);

            if (!initAdBlocker(filterBytes)) {
                throw new RuntimeException("Unable to initialize ad blocker. Filter data may be invalid.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testAdBlocker() {
        String currentPageDomain = "https://stackoverflow.com/questions/10972577/c-cmake-add-non-built-files";
        String urlToCheck = "https://secure.quantserve.com/quant.js";
        Log.e(TAG, String.valueOf(shouldBlockUrl(currentPageDomain, urlToCheck)));
    }

    private static native boolean initAdBlocker(@NonNull byte[] filterBytes);

    private native boolean shouldBlockUrl(@NonNull String currentPageDomain, @NonNull String urlToCheck);
}
