package com.example.lakecircle.data.Merchant;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Merchant.class}, version = 1, exportSchema = false)
public abstract class MerchantDatabase extends RoomDatabase {

    private static MerchantDatabase INSTANCE;

    private static final Object sLock = new Object();

    public abstract MerchantDao merchantDao();

    public static MerchantDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        MerchantDatabase.class, "Merchant.db").build();
            }
            return INSTANCE;
        }
    }

}
