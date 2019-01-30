package com.aght.offlinereader.adblock.webview;

import android.support.annotation.NonNull;
import android.util.Log;

import com.aght.offlinereader.App;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class AdBlockProvider implements AutoCloseable {

    private static final String TAG = "AdBlockProvider";

    private static final String AD_BLOCK_DATA_FILE = "filter.dat";

    /**
     * Address to C++ object
     */
    private final long handle;

    // MUST be an instance variable, else native code will crash, due to local variable being
    // garbage collected
    private byte[] filterData;

    private native long createAdBlockClient();

    private native void destroyAdBlockClient(long handle);

    private native boolean initAdBlockClient(long handle, @NonNull byte[] filterData);

    private native boolean shouldBlockUrl(long handle, String currentPageDomain, String urlToCheck);

    private AdBlockProvider(@NonNull byte[] filterData) {
        handle = createAdBlockClient();
        this.filterData = filterData;
        initAdBlockClient(handle, this.filterData);
    }

    public boolean shouldBlockUrl(String currentPageDomain, String urlToCheck) {
        return shouldBlockUrl(handle, currentPageDomain, urlToCheck);
    }

    public void destroy() {
        destroyAdBlockClient(handle);
    }

    public static AdBlockProvider defaultProvider() {
        return new AdBlockProvider(getFileBytes(AD_BLOCK_DATA_FILE));
    }


    private static byte[] getFileBytes(String filename) {
        try {
            InputStream dataStream = App.getContext().getAssets().open(filename);
            return IOUtils.toByteArray(dataStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void close() {
        destroy();
    }
}
