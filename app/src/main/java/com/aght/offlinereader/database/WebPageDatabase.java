package com.aght.offlinereader.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.aght.offlinereader.App;

@Database(entities = {WebPage.class}, version = 1, exportSchema = false)
public abstract class WebPageDatabase extends RoomDatabase {
    private static WebPageDatabase database = Room.databaseBuilder(
            App.getContext(),
            WebPageDatabase.class,
            "webpage-db").allowMainThreadQueries().build();

    public abstract WebPageDao access();

    public static WebPageDatabase getInstance() {
        return database;
    }
}
