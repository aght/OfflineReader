package com.aght.offlinereader.adblock.webview;

import android.support.annotation.NonNull;
import android.util.Log;

public class AdBlockProvider {

    static {
        System.loadLibrary("ad-block-lib");
    }

    private static final String TAG = "AdBlockProvider";

    private final long handle;

    // MUST be an instance variable, else native code will crash
    private byte[] filterData;

    private native long createAdBlockClient();

    private native void destroyAdBlockClient(@NonNull long handle);

    private native boolean initAdBlockClient(@NonNull long handle, @NonNull byte[] filterData);

    private native boolean shouldBlockUrl(@NonNull long handle, String currentPageDomain, String urlToCheck);

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
        filterData = null;
    }
}
