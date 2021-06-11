package com.example.pam_app.di;

import android.content.Context;
import android.preference.PreferenceManager;

import com.example.pam_app.db.BucketDao;
import com.example.pam_app.db.IncomeDao;
import com.example.pam_app.db.WallyDatabase;
import com.example.pam_app.repository.BucketMapper;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.IncomeMapper;
import com.example.pam_app.repository.IncomeRepository;
import com.example.pam_app.repository.LanguagesRepository;
import com.example.pam_app.repository.LanguagesRepositoryImpl;
import com.example.pam_app.repository.RoomBucketRepository;
import com.example.pam_app.repository.RoomIncomeRepository;
import com.example.pam_app.utils.schedulers.AndroidSchedulerProvider;
import com.example.pam_app.utils.schedulers.SchedulerProvider;

public class Module {

    private final Context applicationContext;

    /* default */ Module(final Context context) {
        applicationContext = context.getApplicationContext();
    }

    /* default */ Context getApplicationContext() {
        return applicationContext;
    }

    /* default */ SchedulerProvider provideSchedulerProvider() {
        return new AndroidSchedulerProvider();
    }

    /* default */ WallyDatabase provideWallyDatabase() {
        return WallyDatabase.getInstance(applicationContext);
    }

    /* default */ BucketDao provideBucketDao(final WallyDatabase wallyDatabase) {
        return wallyDatabase.bucketDao();
    }

    /* default */ IncomeDao provideIncomeDao(final WallyDatabase wallyDatabase) {
        return wallyDatabase.incomeDao();
    }

    /* default */ BucketMapper provideBucketMapper() {
        return new BucketMapper();
    }

    /* default */ IncomeMapper provideIncomeMapper() {
        return new IncomeMapper();
    }

    /* default */ BucketRepository provideBucketRepository(final BucketDao bucketDao,
                                                           final BucketMapper bucketMapper) {
        return new RoomBucketRepository(bucketDao, bucketMapper);
    }

    /* default */ IncomeRepository provideBucketRepository(final IncomeDao incomeDao,
                                                           final IncomeMapper incomeMapper) {
        return new RoomIncomeRepository(incomeDao, incomeMapper);
    }

    /* default */ LanguagesRepository provideLanguageRepository() {
        return new LanguagesRepositoryImpl(PreferenceManager
                .getDefaultSharedPreferences(applicationContext));
    }
}
