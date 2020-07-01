package com.example.lakecircle.data.Lake;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Lake.class}, version = 1, exportSchema = false)
public abstract class LakeDatabase extends RoomDatabase {

    private static LakeDatabase INSTANCE;

    private static final Object sLock = new Object();

    public abstract LakeDao lakeDao();

    public static LakeDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        LakeDatabase.class, "Lake.db").build();
            }
            return INSTANCE;
        }
    }

}
