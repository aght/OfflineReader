package com.aght.offlinereader.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WebPageDao {
    @Insert
    void insertWebPage(WebPage page);

    @Query("SELECT * FROM WebPage")
    List<WebPage> getAllWebPages();

    @Delete
    void deleteWebPage(WebPage page);
}
