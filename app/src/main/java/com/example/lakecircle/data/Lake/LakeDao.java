package com.example.lakecircle.data.Lake;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LakeDao {

    @Query("SELECT * FROM Lake")
    List<Lake> getLakeList();

    @Query( "DELETE FROM Lake WHERE id = :id")
    void deleteLake(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addLake(Lake lake);

    @Query("DELETE FROM Lake")
    void deleteAll();
}
