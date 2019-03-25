package com.tones.frnotifications.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RoomModelDAO {


    @Query("select * from content_table")
    public List<RoomModelData> getAllContent();


    @Insert
    void insert(RoomModelData task);

    @Delete
    void delete(RoomModelData task);


    @Query("DELETE FROM content_table")
    void deleteTable();

    @Update
    void update(RoomModelData task);

    @Query("SELECT * FROM  content_table WHERE  title IS :title")
    public RoomModelData getContentById(String title);

}
