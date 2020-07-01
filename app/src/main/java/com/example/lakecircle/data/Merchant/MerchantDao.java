package com.example.lakecircle.data.Merchant;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MerchantDao {

    @Query("SELECT * FROM Merchant")
    List<Merchant> getAllMerchant();

    @Query("SELECT * FROM Merchant WHERE name LIKE '%' || :query || '%' ")
    List<Merchant> queryMerchant(String query);

    @Query("DELETE FROM Merchant")
    void deleteAll();

    @Query("SELECT * FROM Merchant WHERE id=:id")
    Merchant getMerchant(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMerchant(Merchant merchant);

}
