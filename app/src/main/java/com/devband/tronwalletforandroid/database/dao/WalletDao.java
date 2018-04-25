package com.devband.tronwalletforandroid.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.devband.tronwalletforandroid.database.model.WalletModel;

import java.util.List;

@Dao
public interface WalletDao {

    @Query("SELECT * from wallet where id = :id LIMIT 1")
    WalletModel loadWalletById(int id);

    @Query("SELECT * from wallet")
    List<WalletModel> loadAllWallets();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WalletModel walletModels);

    @Update
    void update(WalletModel walletModel);

    @Delete
    void delete(WalletModel walletModel);
}
