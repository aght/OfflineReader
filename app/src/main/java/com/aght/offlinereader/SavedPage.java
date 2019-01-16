package com.aght.offlinereader;

import android.content.res.Resources;

public class SavedPage {
    private int imageId;
    private String pageTitle;
    private String pageUrl;

    public SavedPage() {}

    public SavedPage(String pageTitle, String pageUrl) {
        this.imageId = R.drawable.placeholder;
        this.pageTitle = pageTitle;
        this.pageUrl = pageUrl;
    }

    public SavedPage(int imageId, String pageTitle, String pageUrl) {
        this.imageId = imageId;
        this.pageTitle = pageTitle;
        this.pageUrl = pageUrl;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }
}
