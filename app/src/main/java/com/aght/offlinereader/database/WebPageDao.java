package com.aght.offlinereader.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WebPageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertWebPage(WebPage page);

    @Query("SELECT * FROM WebPage")
    List<WebPage> getAllWebPages();

    @Query("SELECT * FROM WebPage WHERE webPageUrl = :url LIMIT 1")
    WebPage findByUrl(String url);

    @Delete
    void deleteWebPage(WebPage page);
}
