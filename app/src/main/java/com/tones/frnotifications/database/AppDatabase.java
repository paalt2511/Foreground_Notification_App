package com.tones.frnotifications.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {RoomModelData.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    /*
        return RoomModelDAO interface to access methods related to content Table
    */
    public abstract RoomModelDAO getContentDAO();

}
