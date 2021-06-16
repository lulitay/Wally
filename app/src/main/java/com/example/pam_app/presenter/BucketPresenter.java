package com.example.pam_app.presenter;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.BucketView;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.disposables.CompositeDisposable;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

public class BucketPresenter {

    private final int id;
    private final WeakReference<BucketView> bucketView;
    private final BucketRepository bucketRepository;
    private CompositeDisposable disposable;
    private final SchedulerProvider schedulerProvider;
    private Bucket currentBucket;
    private final ArrayList<Serializable> createdEntries;

    public BucketPresenter(final int id,
                           final BucketView bucketView,
                           final BucketRepository bucketRepository,
                           final SchedulerProvider schedulerProvider) {
        this.id = id;
        this.bucketView = new WeakReference<>(bucketView);
        this.bucketRepository = bucketRepository;
        this.schedulerProvider = schedulerProvider;
        this.createdEntries = new ArrayList<>();
    }

    public void onViewAttach() {
        this.disposable = new CompositeDisposable();
        if (currentBucket == null) {
            disposable.add(
                    bucketRepository.get(this.id)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .subscribe((Bucket b) -> {
                                if (bucketView.get() != null) {
                                    b.entries.sort((e1, e2) -> (int) Math.signum(e1.date.getTime() - e2.date.getTime()));
                                    bucketView.get().bind(b);
                                    currentBucket = b;
                                }
                            }, (throwable) -> {
                                if (bucketView.get() != null) {
                                    bucketView.get().showGetBucketError();
                                }
                            })
            );
        }
    }

    public void onViewDetached() {
        disposable.dispose();
    }

    public void onBackSelected() {
        if (bucketView.get() != null){
            bucketView.get().back(createdEntries, null);
        }
    }

    public void onDeleteSelected() {
        if (bucketView.get() != null) {
            bucketView.get().showSureDialog(this::onDelete);
        }
    }

    public void onDelete() {
        if (bucketView.get() != null) {
            disposable.add(
                 bucketRepository.delete(id)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe((Integer deleted) -> {
                        if (bucketView.get() != null) {
                            bucketView.get().showDeleteBucketSuccess();
                        }
                    }, (throwable) -> {
                        if (bucketView.get() != null) {
                            bucketView.get().showDeleteBucketError();
                        }
                    })
            );
            bucketView.get().back(createdEntries, currentBucket.id);
        }
    }

    public void onAddEntryClick() {
        if (bucketView.get() != null){
            bucketView.get().goToAddEntry(currentBucket.title, currentBucket.bucketType.ordinal());
        }
    }

    public void onAddEntry(final Serializable entry) {
        if (entry != null) {
            createdEntries.add(entry);
            if (entry instanceof BucketEntry && ((BucketEntry) entry).bucketTitle.equals(currentBucket.title)) {
                if (currentBucket.isRecurrent && ((BucketEntry) entry).date.before(getFirstDayOfMonth())) {
                    currentBucket.oldEntries.add(((BucketEntry) entry));
                }
                else {
                    currentBucket.entries.add(((BucketEntry) entry));
                }
                if (bucketView.get() != null) {
                    bucketView.get().bind(currentBucket);
                }
            }
        }
    }

    private Date getFirstDayOfMonth() {
        final Calendar today = getInstance();
        final Calendar next = getInstance();
        next.clear();
        next.set(YEAR, today.get(YEAR));
        next.set(MONTH, today.get(MONTH));
        next.set(DAY_OF_MONTH, 1);
        return next.getTime();
    }
}
