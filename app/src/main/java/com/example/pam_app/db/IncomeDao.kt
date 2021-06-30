package com.example.pam_app.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Flowable
import kotlin.jvm.Synchronized

@Dao
interface IncomeDao {
    @get:Query("SELECT * FROM income")
    val list: Flowable<List<IncomeEntity?>?>

    @Insert
    fun create(income: IncomeEntity?): Long

    @Query("DELETE FROM income WHERE id=:id")
    fun delete(id: Int)

    @Transaction
    @Query("SELECT * FROM income WHERE id=:id")
    operator fun get(id: Int): Flowable<IncomeEntity?>
}