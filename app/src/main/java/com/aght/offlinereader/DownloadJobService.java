package com.aght.offlinereader;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.aght.offlinereader.adblock.webview.AdBlockWebView;
import com.aght.offlinereader.adblock.webview.AdBlockWebViewClient;
import com.aght.offlinereader.database.WebPage;
import com.aght.offlinereader.database.WebPageDatabase;

import java.io.File;

public class DownloadJobService extends JobService {

    private final String TAG = DownloadJobService.class.getSimpleName();

    private AdBlockWebView adBlockWebView;
    private String webPageUrl;
    private JobParameters parameters;
    private WebPage page;
    private WebPageDatabase database;

    @Override
    public boolean onStartJob(final JobParameters parameters) {
        this.parameters = parameters;
        this.webPageUrl = parameters.getExtras().getString("url");
        this.database = WebPageDatabase.getInstance();

        if (database.access().findByUrl(webPageUrl) != null) {
            Log.d(TAG, "Already saved ignoring...");
            jobFinished(parameters, false);
            return false;
        }

        String url = parameters.getExtras().getString("url");
        loadWebPage(url);

        return true; // Continue running
    }

    @Override
    public boolean onStopJob(JobParameters parameters) {
        return false; // Do not reschedule the job
    }

    private void loadWebPage(String url) {
        adBlockWebView = new AdBlockWebView(App.getContext());
        adBlockWebView.setWebViewClient(createAdBlockWebViewClient());
        adBlockWebView.setWebChromeClient(createWebChromeClient());
        adBlockWebView.getSettings().setJavaScriptEnabled(true);
        adBlockWebView.loadUrl(url);
    }

    private AdBlockWebViewClient createAdBlockWebViewClient() {
        return new AdBlockWebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String url) {
                Log.d(TAG, "Finished loading: " + url);

                String path = App.getContext().getFilesDir().getAbsolutePath()
                        + File.separator
                        + page.getFileName();
                Log.d(TAG, "Saving page to: " + path);

                adBlockWebView.saveWebArchive(path);

//                new Runnable() {
//                    public void run() {
//                        try {
//                            Log.d(TAG, "Delaying for 2s");
//                            Thread.sleep(2000);
//                        } catch (InterruptedException e) {
//
//                        }
//                        Log.d(TAG, "Cleaning up and shutting down...");
//                        cleanup();
//                        jobFinished(parameters, false);
//                    }
//                }.run();

            }
        };
    }

    private WebChromeClient createWebChromeClient() {
        return new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                String url = ((AdBlockWebView) webView).getCurrentUrl();
                Log.d(TAG, "Loading: " + url + ": " + newProgress + "%");
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                page = new WebPage();
                page.setWebPageTitle(title);
                page.setWebPageUrl(webPageUrl);

                Log.d(TAG, "Title Received: " + title);
                Log.d(TAG, "Creating database entry...");

                createDatabaseEntry(page);
            }
        };
    }

    private void createDatabaseEntry(final WebPage page) {
        database.access().insertWebPage(page);
    }

    private void cleanup() {
        Log.d(TAG, "Finished job with id: " + parameters.getJobId());
        Log.d(TAG, "Cleaning up...");
        adBlockWebView.destroy();
    }
}
