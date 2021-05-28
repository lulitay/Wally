package com.example.pam_app.utils.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.pam_app.repository.BucketRepository;

public class RecurrentBucketWorker extends Worker {

    private final BucketRepository bucketRepository;

    public RecurrentBucketWorker(@NonNull Context context, @NonNull WorkerParameters params,
                                 @NonNull BucketRepository bucketRepository) {
        super(context, params);
        this.bucketRepository = bucketRepository;
    }

    @Override
    public Result doWork() {
        return null;
    }
}
