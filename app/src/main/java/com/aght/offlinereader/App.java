package com.aght.offlinereader;

import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;

public class App extends Application {

    static {
        loadLibraries();
    }

    private static WeakReference<Context> contextRef;

    @Override
    public void onCreate() {
        super.onCreate();
        contextRef = new WeakReference<>(getApplicationContext());
    }

    public static Context getContext() {
        return contextRef.get();
    }

    private static void loadLibraries() {
        System.loadLibrary("ad-block-lib");
    }
}
