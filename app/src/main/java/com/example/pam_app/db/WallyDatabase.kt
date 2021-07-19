package com.example.pam_app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BucketEntity::class, BucketEntryEntity::class, IncomeEntity::class], version = 11, exportSchema = false)
abstract class WallyDatabase : RoomDatabase() {
    abstract fun bucketDao(): BucketDao?
    abstract fun incomeDao(): IncomeDao?

    companion object {
        private const val NAME = "wally_db"

        @Synchronized
        fun getInstance(context: Context): WallyDatabase {
            return Room.databaseBuilder(context.applicationContext, WallyDatabase::class.java, NAME)
                    .fallbackToDestructiveMigration().build()
        }
    }
}