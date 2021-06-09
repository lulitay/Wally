package com.example.pam_app.utils.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.RxWorker;
import androidx.work.WorkerParameters;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.repository.BucketRepository;

import java.util.Calendar;
import java.util.Date;

import io.reactivex.Single;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

public class RecurrentBucketWorker extends RxWorker {

    private final BucketRepository bucketRepository;

    public RecurrentBucketWorker(@NonNull Context context, @NonNull WorkerParameters params,
                                 @NonNull BucketRepository bucketRepository) {
        super(context, params);
        this.bucketRepository = bucketRepository;
    }

    @Override
    public Single<Result> createWork() {
        Calendar today = getInstance();
        if (today.get(DAY_OF_MONTH) != 1) {
            return Single.create((emitter) -> emitter.onSuccess(Result.success()));
        }
        Date now =  today.getTime();
        return this.bucketRepository.getList(true, now)
                .firstOrError()
                .doOnSuccess((buckets) -> {
                    for (Bucket b: buckets) {
                        this.bucketRepository.update(b.setDueDate(getFirstDayOfNextMonth()));
                    }
                })
                .map((buckets) -> Result.success())
                .onErrorReturn((error) -> Result.failure());
    }

    private Date getFirstDayOfNextMonth() {
        Calendar today = getInstance();
        Calendar next = getInstance();
        next.clear();
        next.set(YEAR, today.get(YEAR));
        next.set(MONTH, today.get(MONTH) + 1);
        next.set(DAY_OF_MONTH, 1);
        next.set(HOUR, 0);
        return next.getTime();
    }
}
