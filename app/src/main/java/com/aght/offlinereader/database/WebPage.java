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
    private String id;
    private String webPageTitle;
    private String webPageUrl;
    private String fileName;

    public WebPage() {
        id = UUID.randomUUID().toString();
        fileName = id + FILE_EXT;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getWebPageTitle() {
        return webPageTitle;
    }

    public void setWebPageTitle(String webPageTitle) {
        this.webPageTitle = webPageTitle;
    }

    public String getWebPageUrl() {
        return webPageUrl;
    }

    public void setWebPageUrl(String webPageUrl) {
        this.webPageUrl = webPageUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
