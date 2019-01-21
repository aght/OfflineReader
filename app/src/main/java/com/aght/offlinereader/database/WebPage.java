package com.aght.offlinereader.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

@Entity
public class WebPage {

    @Ignore
    private static final String FILE_EXT = ".mht";

    @PrimaryKey
    @NonNull
    private String webPageUrl;
    private String webPageTitle;
    private String fileName;

    public WebPage() {
        fileName = UUID.randomUUID().toString() + FILE_EXT;
    }

    @NonNull
    public String getWebPageUrl() {
        return webPageUrl;
    }

    public void setWebPageUrl(@NonNull String webPageUrl) {
        this.webPageUrl = webPageUrl;
    }

    public String getWebPageTitle() {
        return webPageTitle;
    }

    public void setWebPageTitle(String webPageTitle) {
        this.webPageTitle = webPageTitle;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
