package com.example.pam_app.db

import androidx.room.*
import io.reactivex.Flowable
import java.util.*

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

    @TypeConverters(DateConverter::class)
    @Query("SELECT * FROM income WHERE incomeType=:type AND date<:date")
    fun getList(type: Int, date: Date?): Flowable<List<IncomeEntity?>?>
}