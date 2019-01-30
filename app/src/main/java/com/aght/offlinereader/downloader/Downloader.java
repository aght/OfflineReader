package com.aght.offlinereader.downloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aght.offlinereader.App;
import com.aght.offlinereader.adblock.webview.AdBlockProvider;
import com.aght.offlinereader.database.WebPageDatabase;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Downloader {

    private static final String TAG = Downloader.class.getSimpleName();
    private static final String BASE_FOLDER_NAME = "saves";

    private WebPageDatabase database = WebPageDatabase.getInstance();
    private String folderName;
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
        this.folderName = UUID.randomUUID().toString();

        headlessWebView.loadUrl(pageUrl);
    }

    public void onDownloadFinish() {
        Log.d(TAG, "Finished downloading: " + pageUrl);
    }

    private void parseHTML(String html) {
        document = Jsoup.parse(html, pageUrl);

        pageTitle = document.title();

        Elements cssElements = document.select("link");
        Elements srcElements = document.select("[src]");

        downloadHTML(html);
        downloadResources(cssElements, "href");
        downloadResources(srcElements, "src");

        onDownloadFinish();
    }

    private void downloadHTML(String html) {
        String path = getSaveDirectory();
        String fileName = folderName + ".html";

//        writeFile(path, fileName, new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)));
    }

    private void downloadResources(Elements elements, String selection) {
        AdBlockProvider adBlockProvider = AdBlockProvider.defaultProvider();

        try {
            for (Element e : elements) {
                String url = e.absUrl(selection);

                if (!adBlockProvider.shouldBlockUrl(pageUrl, url)) {
                    downloadResource(url);
                }
            }
        } finally {
            adBlockProvider.destroy();
        }
    }

    private void downloadResource(String url) {
        if (url.trim().length() == 0) {
            return;
        }

        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String contentType = connection.getContentType();
                String contentDisposition = connection.getHeaderField("Content-Disposition");

                String fileExtension = ContentTypeResolver.getInstance().getExtension(contentType, contentDisposition);

                String fileName = UUID.randomUUID().toString() + fileExtension;
                String saveDir = getSaveDirectory();

                String path = saveDir + File.separator + fileName;
                Log.d(TAG, "Saving as: " + path + ": " + url);

                InputStream inputStream = connection.getInputStream();
            }
        } catch (MalformedURLException e) {
            Log.d(TAG, "Malformed Url: " + url);
        } catch (IOException e) {
            Log.d(TAG, "Exception during downloading: " + App.stackTraceToString(e));
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String getSaveDirectory() {
        return App.getContext().getFilesDir().getAbsolutePath()
                + File.separator
                + BASE_FOLDER_NAME
                + File.separator
                + folderName;
    }

    private void writeFile(String path, String filename, InputStream dataStream) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(path + File.separator + filename));

            IOUtils.copy(dataStream, outputStream);
        } catch (IOException e) {
            Log.d(TAG, "Failed to create file: " + path + File.separator + filename);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.d(TAG, "Failed to close output stream");
                }
            }
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
                    Log.d(TAG, "Finished Loading: " + url);
                    if (!hasLoadedJavaScript) {
                        Log.d(TAG, "Getting raw HTML...");
                        headlessWebView.loadUrl("javascript:window.HTMLOUT.getHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

//                        Log.d(TAG, "Getting article view...");
//                        String readabilityAPI = readFile("Readability.js");
//                        headlessWebView.evaluateJavascript(readabilityAPI, new ValueCallback<String>() {
//                            @Override
//                            public void onReceiveValue(String value) {
//                                Log.d(TAG, value);
//                            }
//                        });

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

    private String readFile(String fileName) {
        try {
            Context appContext = App.getContext();
            InputStream stream = appContext.getResources().getAssets().open(fileName);
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private class HTMLDownloadInterface {
        @JavascriptInterface
        public void getHTML(String html) {
            parseHTML(html);
        }
    }
}
