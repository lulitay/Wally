package com.example.pam_app.presenter;

import com.example.pam_app.R;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketType;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.AddBucketView;

import java.lang.ref.WeakReference;
import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;

import static com.example.pam_app.fragment.AddBucketEntryFragment.MAX_AMOUNT;
import static com.example.pam_app.fragment.AddBucketEntryFragment.MAX_CHARACTERS;

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

    public void saveBucket(
            final String title,
            final Date dueDate,
            final BucketType bucketType,
            final Double target,
            final String imagePath
    ) {
        final boolean fields = checkFields(title, dueDate, bucketType, target);
        if (fields) {
            final Bucket bucket = new Bucket(title, dueDate, bucketType, target, imagePath);
            disposable = Completable.fromAction(() -> bucketRepository.create(bucket))
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(() -> {
                        if (addBucketView.get() != null) {
                            addBucketView.get().onSuccessSavingBucket(bucket);
                        }
                    }, (throwable) -> {
                        if (addBucketView.get() != null) {
                            addBucketView.get().onErrorSavingBucket();
                        }
                    });
        }
    }

    private boolean checkFields(
            final String title,
            final Date dueDate,
            final BucketType bucketType,
            final Double target
    ) {
        boolean isCorrect = true;
        if (title.length() == 0) {
            addBucketView.get().showTitleError(R.string.error_empty, null);
            isCorrect = false;
        } else if (title.length() > MAX_CHARACTERS) {
            addBucketView.get().showTitleError(R.string.max_characters, MAX_CHARACTERS);
            isCorrect = false;
        }
        if (target == null) {
            addBucketView.get().showTargetError(R.string.error_empty, null);
            isCorrect = false;
        } else if (target >= MAX_AMOUNT) {
            addBucketView.get().showTargetError(R.string.max_amount, MAX_AMOUNT);
            isCorrect = false;
        }
        if (dueDate == null) {
            addBucketView.get().showDateError(R.string.error_empty);
            isCorrect = false;
        } else if (dueDate.getTime() < new Date().getTime()) {
            addBucketView.get().showDateError(R.string.error_past_date);
            isCorrect = false;
        }
        if (bucketType == null) {
            addBucketView.get().showBucketTypeError(R.string.error_empty);
            isCorrect = false;
        }
        return isCorrect;
    }

    public void onClickLoadImage() {
        if (addBucketView.get() != null) {
            addBucketView.get().requestStoragePermission();
        }
    }

    public void onDetachView() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
