package com.aght.offlinereader;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.aght.offlinereader.adblock.webview.AdBlockWebView;
import com.aght.offlinereader.adblock.webview.AdBlockWebViewClient;


public class DownloadJobService extends JobService {

    private final String TAG = DownloadJobService.class.getSimpleName();

    private AdBlockWebView adBlockWebView;
    private JobParameters parameters;

    private Handler handler = new Handler();
    private Runnable worker;

    @Override
    public boolean onStartJob(JobParameters parameters) {
        this.parameters = parameters;
        final JobParameters tmp = parameters;

        // TODO: Convert to AsyncTask
        worker = new Runnable() {
            @Override
            public void run() {
                String url = tmp.getExtras().getString("url");
                loadWebPage(url);
            }
        };

        handler.post(worker);

        return true; // Continue running
    }

    @Override
    public boolean onStopJob(JobParameters parameters) {
        handler.removeCallbacks(worker);
        cleanup();

        return false; // Do not reschedule the job
    }

    private void loadWebPage(String url) {
        adBlockWebView = new AdBlockWebView(App.getContext());
        adBlockWebView.setWebViewClient(createAdBlockWebViewClient());
        adBlockWebView.setWebChromeClient(createWebChromeClient());
        adBlockWebView.loadUrl(url);
    }

    private AdBlockWebViewClient createAdBlockWebViewClient() {
        return new AdBlockWebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String url) {
                saveWebPage();
            }
        };
    }

    private WebChromeClient createWebChromeClient() {
        return new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                AdBlockWebView tmp = (AdBlockWebView) webView;
                Log.d(TAG, "Loading: " + tmp.getCurrentUrl() + ": " + newProgress + "%");
            }
        };
    }

    private void saveWebPage() {
        Log.d(TAG, "Finished job with id: " + parameters.getJobId());

        jobFinished(parameters, false);
    }

    private void cleanup() {
        adBlockWebView.destroy();
    }
}
