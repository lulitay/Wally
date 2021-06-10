package com.example.pam_app.di;

import android.content.Context;

import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.IncomeRepository;
import com.example.pam_app.repository.LanguagesRepository;
import com.example.pam_app.utils.schedulers.SchedulerProvider;

public interface Container {
    Context getApplicationContext();
    SchedulerProvider getSchedulerProvider();
    BucketRepository getBucketRepository();
    IncomeRepository getIncomeRepository();
    LanguagesRepository getLanguageRepository();
}
