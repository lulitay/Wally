package com.example.pam_app.utils.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.pam_app.repository.BucketRepository
import com.example.pam_app.repository.IncomeRepository

class RecurrentBucketWorkerFactory(bucketRepository: BucketRepository?, incomeRepository: IncomeRepository?) : WorkerFactory() {
    private val bucketRepository: BucketRepository = bucketRepository!!
    private val incomeRepository: IncomeRepository = incomeRepository!!
    override fun createWorker(appContext: Context,
                              workerClassName: String,
                              workerParameters: WorkerParameters): ListenableWorker? {
        return when (workerClassName) {
            RecurrentBucketWorker::class.java.name ->
                RecurrentBucketWorker(appContext, workerParameters, bucketRepository)
            RecurrentIncomeWorker::class.java.name ->
                RecurrentIncomeWorker(appContext, workerParameters, incomeRepository)
            else ->
                null
        }
    }
}