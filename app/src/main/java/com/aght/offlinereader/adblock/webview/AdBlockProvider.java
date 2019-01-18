package com.aght.offlinereader.adblock.webview;

import android.support.annotation.NonNull;
import android.util.Log;

public class AdBlockProvider {

    static {
        System.loadLibrary("ad-block-lib");
    }

    private static final String TAG = "AdBlockProvider";

    private final long handle;

    private byte[] filterData;

    private native long createAdBlockClient();

    private native void destroyAdBlockClient(long handle);

    private native boolean initAdBlockClient(@NonNull long handle, @NonNull byte[] filterData);

    private native boolean shouldBlockUrl(long handle, String currentPageDomain, String urlToCheck);

    public AdBlockProvider(@NonNull byte[] filterData) {
        handle = createAdBlockClient();
        this.filterData = filterData;
        initAdBlockClient(handle, this.filterData);
        Log.e(TAG, String.valueOf(handle));
    }

    public boolean shouldBlockUrl(String currentPageDomain, String urlToCheck) {
        return shouldBlockUrl(handle, currentPageDomain, urlToCheck);
    }

    public void destroy() {
        destroyAdBlockClient(handle);
    }
}
