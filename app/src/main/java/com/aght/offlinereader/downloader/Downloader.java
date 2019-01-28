package com.aght.offlinereader.downloader;

import android.graphics.Bitmap;
import android.icu.util.LocaleData;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aght.offlinereader.App;
import com.aght.offlinereader.adblock.webview.AdBlockProvider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Downloader {

    private static final String TAG = Downloader.class.getSimpleName();

    private String pageUrl;
    private String pageTitle;
    private WebView headlessWebView;
    private Document document;
    private Map<String, String> resourceMap;

    private boolean hasLoadedJavaScript = false;

    public Downloader() {
        setupHeadlessWebView();
    }

    public void download(String url) {
        this.pageUrl = url;
        this.resourceMap = new HashMap<>();

        downloadHTML();
    }

    public void onDownloadFinish() {
        Log.d(TAG, "Finished downloading: " + pageUrl);
    }

    private void downloadHTML() {
        headlessWebView.loadUrl(pageUrl);
    }

    private void parseHTML() {
        pageTitle = document.title();

        Elements cssElements = document.select("link");
        Elements srcElements = document.select("[src]");

        downloadResources(cssElements, "href");
        downloadResources(srcElements, "src");
    }

    private void downloadResources(Elements elements, String selection) {
        AdBlockProvider adBlockProvider = AdBlockProvider.defaultProvider();

        try {
            for (Element e : elements) {
                String url = e.absUrl(selection);

                if (!adBlockProvider.shouldBlockUrl(pageUrl, url)) {
                    downloadResource(url);
                } else {
                    Log.d(TAG, "Blocked: " + url);
                }
            }
        } finally {
            adBlockProvider.destroy();
        }

    }

    private void downloadResource(String url) {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            String uuid = UUID.randomUUID().toString();
            String contentType = urlConnection.getContentType();
            String contentDisposition = urlConnection.getHeaderField("Content-Disposition");

            // TODO: Make a check to Content-Disposition and url file extension first
            String fileExtension = ContentTypeResolver
                    .getInstance()
                    .getExtension(contentType, contentDisposition);

            Log.d(TAG, "Downloading: " + url + ": " + fileExtension + " | " + contentType + " | " + contentDisposition);

        } catch (MalformedURLException e) {
            Log.d(TAG, "Malformed Url: " + url);
        } catch (IOException e) {
            Log.d(TAG, "IOException...");
        }
    }

    private void setupHeadlessWebView() {
        headlessWebView = new WebView(App.getContext());
        headlessWebView.setWebViewClient(createWebViewClient());
        headlessWebView.setWebChromeClient(createWebChromeClient());

        headlessWebView.getSettings().setJavaScriptEnabled(true);

        headlessWebView.addJavascriptInterface(new HTMLDownloadInterface(), "HTMLOUT");
    }

    private WebViewClient createWebViewClient() {
        return new WebViewClient() {

            boolean loadingFinished = true;
            boolean redirect = false;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (!loadingFinished) {
                    redirect = true;
                }

                loadingFinished = false;
                headlessWebView.loadUrl(request.getUrl().toString());

                return true;
            }

            @Override
            public void onPageStarted(WebView webView, String url, Bitmap icon) {
                super.onPageStarted(webView, url, icon);
                loadingFinished = false;
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                if (!redirect) {
                    loadingFinished = true;
                }

                if (loadingFinished && !redirect) {
                    if (!hasLoadedJavaScript) {
                        Log.d(TAG, "Finished Loading: " + url);
                        Log.d(TAG, "Getting raw HTML...: ");
                        headlessWebView.loadUrl("javascript:window.HTMLOUT.getHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                        hasLoadedJavaScript = true;
                    }
                } else {
                    redirect = false;
                }
            }
        };
    }

    private WebChromeClient createWebChromeClient() {
        return new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                if (!hasLoadedJavaScript) {
                    Log.d(TAG, "Loading: " + pageUrl + ": " + newProgress + "%");
                }
            }
        };
    }

    private class HTMLDownloadInterface {
        @JavascriptInterface
        public void getHTML(String html) {
            document = Jsoup.parse(html, pageUrl);
            parseHTML();
        }
    }
}
