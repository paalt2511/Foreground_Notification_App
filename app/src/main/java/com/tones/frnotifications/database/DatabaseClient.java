package com.tones.frnotifications.database;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseClient {

    private Context mCtx;
    private static DatabaseClient mInstance;
    private AppDatabase appDatabase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        //creating the app database with Room database builder
        //rt_module is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "demo_database")
                .allowMainThreadQueries() // To allow main thread queries
                .build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
