package com.example.pam_app.presenter;

import com.example.pam_app.R;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.AddBucketEntryView;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

import static com.example.pam_app.fragment.AddBucketEntryFragment.MAX_AMOUNT;
import static com.example.pam_app.fragment.AddBucketEntryFragment.MAX_CHARACTERS;

public class AddBucketEntryPresenter {

    private final WeakReference<AddBucketEntryView> addBucketEntryView;
    private final BucketRepository bucketRepository;
    private final SchedulerProvider schedulerProvider;
    private final CompositeDisposable disposable;

    public AddBucketEntryPresenter(
            final AddBucketEntryView addBucketEntryView,
            final BucketRepository bucketRepository,
            final SchedulerProvider schedulerProvider
    ) {
        this.addBucketEntryView = new WeakReference<>(addBucketEntryView);
        this.bucketRepository = bucketRepository;
        this.schedulerProvider = schedulerProvider;
        this.disposable = new CompositeDisposable();
    }

    public void onViewAttached() {
        if (addBucketEntryView.get() != null) {
            final List<String> buckets = bucketRepository.getTitleListByType(
                    addBucketEntryView.get().getBucketType()
            ).blockingFirst();
            addBucketEntryView.get().setDropDownOptions(buckets);
        }
    }

    public void onViewDetached() {
        disposable.dispose();
    }

    public void saveBucketEntry(final String amount, final Date date, final String description,
                                final String bucketTitle) {
        final boolean fields = checkFields(amount, date, description, bucketTitle);
        if (fields) {
            final BucketEntry entry = new BucketEntry(Double.parseDouble(amount), date, description, bucketTitle);
            disposable.add(
                bucketRepository.get(bucketTitle)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe((Bucket bucket) -> addEntryToBucket(entry, bucket), (throwable) ->
                    {
                        if (addBucketEntryView.get() != null) {
                            addBucketEntryView.get().onErrorSavingBucketEntry();
                        }
                    })
            );
        }
    }

    private void addEntryToBucket(final BucketEntry entry, final Bucket bucket) {
        disposable.add(
            bucketRepository.addEntry(entry, bucket.id)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe((Long id) -> {
                    if (addBucketEntryView.get() != null) {
                        addBucketEntryView.get().onSuccessSavingBucketEntry(entry);
                    }
                }, (throwable) -> {
                    if (addBucketEntryView.get() != null) {
                        addBucketEntryView.get().onErrorSavingBucketEntry();
                    }
                })
        );
    }

    private boolean checkFields(
            final String amount,
            final Date date,
            final String description,
            final String bucket
    ) {
        boolean isCorrect = true;
        if (description.length() == 0) {
            addBucketEntryView.get().showDescriptionError(R.string.error_empty, null);
            isCorrect = false;
        } else if (description.length() > MAX_CHARACTERS) {
            addBucketEntryView.get().showDescriptionError(R.string.max_characters, MAX_CHARACTERS);
            isCorrect = false;
        }
        if (amount.length() == 0) {
            addBucketEntryView.get().showAmountError(R.string.error_empty, null);
            isCorrect = false;
        } else if (amount.equals(".")){
            addBucketEntryView.get().showAmountError(R.string.error_format, null);
            isCorrect = false;
        } else if (Double.parseDouble(amount) >= MAX_AMOUNT) {
            addBucketEntryView.get().showAmountError(R.string.max_amount, MAX_AMOUNT);
            isCorrect = false;
        }
        if (date == null) {
            addBucketEntryView.get().showDateError(R.string.error_empty);
            isCorrect = false;
        } else if (date.getTime() > new Date().getTime()) {
            addBucketEntryView.get().showDateError(R.string.error_future_date);
            isCorrect = false;
        }
        if (bucket == null || bucket.isEmpty()) {
            addBucketEntryView.get().showBucketTitleError(R.string.error_empty);
            isCorrect = false;
        }
        return isCorrect;
    }
}
