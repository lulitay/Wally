package com.example.pam_app.di;

import android.content.Context;

import com.example.pam_app.db.BucketDao;
import com.example.pam_app.db.IncomeDao;
import com.example.pam_app.db.WallyDatabase;
import com.example.pam_app.repository.BucketMapper;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.IncomeMapper;
import com.example.pam_app.repository.IncomeRepository;
import com.example.pam_app.repository.LanguagesRepository;
import com.example.pam_app.utils.schedulers.SchedulerProvider;

public class ProductionContainer implements Container {

    private final Module module;

    private SchedulerProvider schedulerProvider;
    private BucketRepository bucketRepository;
    private IncomeRepository incomeRepository;
    private LanguagesRepository languagesRepository;
    private WallyDatabase wallyDatabase;
    private BucketDao bucketDao;
    private IncomeDao incomeDao;
    private BucketMapper bucketMapper;
    private IncomeMapper incomeMapper;

    public ProductionContainer(final Context context) {
        this.module = new Module(context);
    }

    @Override
    public Context getApplicationContext() {
        return module.getApplicationContext();
    }

    @Override
    public SchedulerProvider getSchedulerProvider() {
        if (schedulerProvider == null) {
            schedulerProvider = module.provideSchedulerProvider();
        }
        return schedulerProvider;
    }

    @Override
    public BucketRepository getBucketRepository() {
        if (bucketRepository == null) {
            bucketRepository = module.provideBucketRepository(getBucketDao(), getBucketMapper());
        }
        return bucketRepository;
    }

    @Override
    public IncomeRepository getIncomeRepository() {
        if (incomeRepository == null) {
            incomeRepository = module.provideIncomeRepository(getIncomeDao(), getIncomeMapper());
        }
        return incomeRepository;
    }

    @Override
    public LanguagesRepository getLanguageRepository() {
        if (languagesRepository == null) {
            languagesRepository = module.provideLanguageRepository();
        }
        return languagesRepository;
    }

    private WallyDatabase getWallyDatabase() {
        if (wallyDatabase == null) {
            wallyDatabase = module.provideWallyDatabase();
        }
        return wallyDatabase;
    }

    private BucketDao getBucketDao() {
        if (bucketDao == null) {
            bucketDao = module.provideBucketDao(getWallyDatabase());
        }
        return bucketDao;
    }

    private IncomeDao getIncomeDao() {
        if (incomeDao == null) {
            incomeDao = module.provideIncomeDao(getWallyDatabase());
        }
        return incomeDao;
    }

    private BucketMapper getBucketMapper() {
        if (bucketMapper == null) {
            bucketMapper = module.provideBucketMapper();
        }
        return bucketMapper;
    }

    private IncomeMapper getIncomeMapper() {
        if (incomeMapper == null) {
            incomeMapper = module.provideIncomeMapper();
        }
        return incomeMapper;
    }
}
