package com.example.pam_app.di

import android.content.Context
import androidx.preference.PreferenceManager
import com.example.pam_app.db.BucketDao
import com.example.pam_app.db.IncomeDao
import com.example.pam_app.db.WallyDatabase
import com.example.pam_app.repository.*
import com.example.pam_app.utils.schedulers.AndroidSchedulerProvider
import com.example.pam_app.utils.schedulers.SchedulerProvider

class Module internal constructor(context: Context?) {
    /* default */ val applicationContext: Context = context!!.applicationContext

    /* default */
    fun provideSchedulerProvider(): SchedulerProvider {
        return AndroidSchedulerProvider()
    }

    /* default */
    fun provideWallyDatabase(): WallyDatabase {
        return WallyDatabase.getInstance(applicationContext)
    }

    /* default */
    fun provideBucketDao(wallyDatabase: WallyDatabase?): BucketDao? {
        return wallyDatabase!!.bucketDao()
    }

    /* default */
    fun provideIncomeDao(wallyDatabase: WallyDatabase?): IncomeDao? {
        return wallyDatabase!!.incomeDao()
    }

    /* default */
    fun provideBucketMapper(): BucketMapper {
        return BucketMapper()
    }

    /* default */
    fun provideIncomeMapper(): IncomeMapper {
        return IncomeMapper()
    }

    /* default */
    fun provideBucketRepository(bucketDao: BucketDao?,
                                bucketMapper: BucketMapper?): BucketRepository {
        return RoomBucketRepository(bucketDao, bucketMapper)
    }

    /* default */
    fun provideIncomeRepository(incomeDao: IncomeDao?,
                                incomeMapper: IncomeMapper?): IncomeRepository {
        return RoomIncomeRepository(incomeDao, incomeMapper)
    }

    /* default */
    fun provideLanguageRepository(): LanguagesRepository {
        return LanguagesRepositoryImpl(PreferenceManager
                .getDefaultSharedPreferences(applicationContext))
    }

}