package com.example.pam_app.utils.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.ListenableWorker;
import androidx.work.WorkerFactory;
import androidx.work.WorkerParameters;

import com.example.pam_app.repository.BucketRepository;

public class RecurrentBucketWorkerFactory extends WorkerFactory {

    private final BucketRepository bucketRepository;

    public RecurrentBucketWorkerFactory(BucketRepository bucketRepository) {
        this.bucketRepository = bucketRepository;
    }

    @Nullable
    @Override
    public ListenableWorker createWorker(@NonNull Context appContext,
                                         @NonNull String workerClassName,
                                         @NonNull WorkerParameters workerParameters) {
        return new RecurrentBucketWorker(appContext, workerParameters, bucketRepository);
    }
}
