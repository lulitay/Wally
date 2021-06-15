package com.example.pam_app.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BucketEntity.class, BucketEntryEntity.class, IncomeEntity.class}, version = 9, exportSchema = false)
public abstract class WallyDatabase extends RoomDatabase {

    private static final String NAME = "wally_db";

    public abstract BucketDao bucketDao();
    public abstract IncomeDao incomeDao();

    public static synchronized WallyDatabase getInstance(final Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), WallyDatabase.class, NAME)
                            .fallbackToDestructiveMigration().build();
    }
}
