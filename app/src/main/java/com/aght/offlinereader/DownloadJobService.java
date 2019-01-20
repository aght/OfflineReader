package com.aght.offlinereader;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.arch.persistence.room.Room;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.aght.offlinereader.adblock.webview.AdBlockWebView;
import com.aght.offlinereader.adblock.webview.AdBlockWebViewClient;
import com.aght.offlinereader.database.WebPage;
import com.aght.offlinereader.database.WebPageDatabase;


public class DownloadJobService extends JobService {

    private final String TAG = DownloadJobService.class.getSimpleName();

    private AdBlockWebView adBlockWebView;
    private String webPageTitle;

    private JobParameters parameters;

    private Handler handler = new Handler();
    private Runnable worker;

    @Override
    public boolean onStartJob(final JobParameters parameters) {
        this.parameters = parameters;

        worker = new Runnable() {
            @Override
            public void run() {
                String url = parameters.getExtras().getString("url");
                loadWebPage(url);
            }
        };

        handler.post(worker);

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
        adBlockWebView.loadUrl(url);
    }

    private AdBlockWebViewClient createAdBlockWebViewClient() {
        return new AdBlockWebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String url) {
                Log.d(TAG, "Finished loading: " + url);

                saveWebPage(url);
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
                webPageTitle = title;
            }
        };
    }

    private void saveWebPage(final String url) {

        final WebPage page = new WebPage();
        page.setWebPageTitle(webPageTitle);
        page.setWebPageUrl(url);

        new Thread(new Runnable() {
            @Override
            public void run() {
                WebPageDatabase db = Room.databaseBuilder(
                        App.getContext(),
                        WebPageDatabase.class,
                        "webpage-db").build();

                db.access().insertWebPage(page);
            }
        }).start();

        Log.d(TAG, page.getFileName());
//        Log.e(TAG, App.getContext().getFilesDir().getAbsolutePath());

        jobFinished(parameters, false);
        cleanup();
    }

    private void cleanup() {
        adBlockWebView.destroy();
        handler.removeCallbacks(worker);
        Log.d(TAG, "Finished job with id: " + parameters.getJobId());
    }
}
