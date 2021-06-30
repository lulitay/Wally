package com.example.pam_app.utils.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.pam_app.repository.BucketRepository
import kotlin.jvm.Synchronized

class RecurrentBucketWorkerFactory(bucketRepository: BucketRepository?) : WorkerFactory() {
    private val bucketRepository: BucketRepository
    override fun createWorker(appContext: Context,
                              workerClassName: String,
                              workerParameters: WorkerParameters): ListenableWorker? {
        return RecurrentBucketWorker(appContext, workerParameters, bucketRepository)
    }

    init {
        this.bucketRepository = bucketRepository!!
    }
}