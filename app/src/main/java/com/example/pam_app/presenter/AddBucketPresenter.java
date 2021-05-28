package com.example.pam_app.presenter;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketType;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.AddBucketView;

import java.lang.ref.WeakReference;
import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;

import java.util.Calendar;

import static java.util.Calendar.*;
import static java.util.Calendar.DAY_OF_MONTH;

public class AddBucketPresenter {

    private final WeakReference<AddBucketView> addBucketView;
    private final BucketRepository bucketRepository;
    private final SchedulerProvider schedulerProvider;
    private Disposable disposable;

    public AddBucketPresenter(
            final AddBucketView addBucketView,
            final BucketRepository bucketRepository,
            final SchedulerProvider schedulerProvider
    ) {
        this.addBucketView = new WeakReference<>(addBucketView);
        this.bucketRepository = bucketRepository;
        this.schedulerProvider = schedulerProvider;
    }

    public void saveBucket(final String name, final Date dueDate, final BucketType bucketType,
                           final double target, final String imagePath, final boolean isRecurrent) {
        final Date date = isRecurrent ? getFirstDayOfNextMonth() : dueDate;
        final Bucket bucket = new Bucket(name, date, bucketType, target, imagePath, isRecurrent);
        disposable = Completable.fromAction(() -> bucketRepository.create(bucket))
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(() -> {
                    if (addBucketView.get() != null) {
                        addBucketView.get().onSuccessSavingBucket(name);
                    }
                }, (throwable) -> {
                    if (addBucketView.get() != null) {
                        addBucketView.get().onErrorSavingBucket();                    }
                });
    }

    public void onClickLoadImage() {
        if (addBucketView.get() != null) {
            addBucketView.get().goToLoadImage();
        }
    }

    public void onDetachView() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void onIsRecurrentSwitchChange(final boolean isRecurrent) {
        if (addBucketView.get() != null) {
            addBucketView.get().changeDatePickerState(!isRecurrent);
        }
    }

    private Date getFirstDayOfNextMonth() {
        Calendar today = getInstance();
        Calendar next = getInstance();
        next.clear();
        next.set(YEAR, today.get(YEAR));
        next.set(MONTH, today.get(MONTH) + 1);
        next.set(DAY_OF_MONTH, 1);
        return next.getTime();
    }
}
