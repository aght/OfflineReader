package com.aght.offlinereader.adblock.webview;

import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class AdBlockWebChromeClient extends WebChromeClient {

    private static String TAG = "AdBlockWebChromeClient";

    @Override
    public void onProgressChanged (WebView view, int newProgress) {
        Log.e(TAG, "Current Progress: " + String.valueOf(newProgress));
    }
}
