package com.example.pam_app.di

import android.content.Context
import com.example.pam_app.db.BucketDao
import com.example.pam_app.db.IncomeDao
import com.example.pam_app.db.WallyDatabase
import com.example.pam_app.repository.*
import com.example.pam_app.utils.schedulers.SchedulerProvider

class ProductionContainer(context: Context?) : Container {
    private val module: Module
    override var schedulerProvider: SchedulerProvider? = null
        get() {
            if (field == null) {
                field = module.provideSchedulerProvider()
            }
            return field
        }
        private set
    override var bucketRepository: BucketRepository? = null
        get() {
            if (field == null) {
                field = module.provideBucketRepository(bucketDao, bucketMapper)
            }
            return field
        }
        private set
    override var incomeRepository: IncomeRepository? = null
        get() {
            if (field == null) {
                field = module.provideIncomeRepository(incomeDao, incomeMapper)
            }
            return field
        }
        private set
    private var languagesRepository: LanguagesRepository? = null
    private var wallyDatabase: WallyDatabase? = null
        get() {
            if (field == null) {
                field = module.provideWallyDatabase()
            }
            return field
        }
    private var bucketDao: BucketDao? = null
        get() {
            if (field == null) {
                field = module.provideBucketDao(wallyDatabase)
            }
            return field
        }
    private var incomeDao: IncomeDao? = null
        get() {
            if (field == null) {
                field = module.provideIncomeDao(wallyDatabase)
            }
            return field
        }
    private var bucketMapper: BucketMapper? = null
        get() {
            if (field == null) {
                field = module.provideBucketMapper()
            }
            return field
        }
    private var incomeMapper: IncomeMapper? = null
        get() {
            if (field == null) {
                field = module.provideIncomeMapper()
            }
            return field
        }
    override val applicationContext: Context
        get() = module.applicationContext

    override val languageRepository: LanguagesRepository?
        get() {
            if (languagesRepository == null) {
                languagesRepository = module.provideLanguageRepository()
            }
            return languagesRepository
        }

    init {
        module = Module(context)
    }
}