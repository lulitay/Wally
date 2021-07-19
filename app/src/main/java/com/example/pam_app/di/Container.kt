package com.example.pam_app.di

import android.content.Context
import com.example.pam_app.repository.BucketRepository
import com.example.pam_app.repository.IncomeRepository
import com.example.pam_app.repository.LanguagesRepository
import com.example.pam_app.utils.schedulers.SchedulerProvider
import kotlin.jvm.Synchronized

interface Container {
    val applicationContext: Context?
    val schedulerProvider: SchedulerProvider?
    val bucketRepository: BucketRepository?
    val incomeRepository: IncomeRepository?
    val languageRepository: LanguagesRepository?
}