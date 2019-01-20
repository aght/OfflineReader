package com.aght.offlinereader.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {WebPage.class}, version = 1, exportSchema = false)
public abstract class WebPageDatabase extends RoomDatabase {
    public abstract WebPageDao access();
}
