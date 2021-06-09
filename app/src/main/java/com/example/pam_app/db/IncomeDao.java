package com.example.pam_app.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface IncomeDao {

    @Query("SELECT * FROM income")
    Flowable<List<IncomeEntity>> getList();

    @Insert
    void create(final IncomeEntity income);

    @Query("DELETE FROM income WHERE id=:id")
    void delete(final int id);

    @Transaction
    @Query("SELECT * FROM income WHERE id=:id")
    Flowable<IncomeEntity> get(final int id);
}
