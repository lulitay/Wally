package com.example.pam_app.presenter;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.BucketView;

import java.lang.ref.WeakReference;
import java.util.Collections;

import io.reactivex.disposables.CompositeDisposable;

public class BucketPresenter {

    private final int id;
    private final WeakReference<BucketView> bucketView;
    private final BucketRepository bucketRepository;
    private CompositeDisposable disposable;
    private final SchedulerProvider schedulerProvider;
    private Bucket currentBucket;

    public BucketPresenter(final int id, final BucketView bucketView,
                           final BucketRepository bucketRepository,
                           final SchedulerProvider schedulerProvider) {
        this.id = id;
        this.bucketView = new WeakReference<>(bucketView);
        this.bucketRepository = bucketRepository;
        this.schedulerProvider = schedulerProvider;
    }

    public void onViewAttach() {
        this.disposable = new CompositeDisposable();
    }

    public void onViewResume() {
        disposable.add(
            bucketRepository.get(this.id)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe((Bucket b) -> {
                    if (bucketView.get() != null) {
                        Collections.sort(b.entries, (e1, e2) -> (int) Math.signum(e1.date.getTime() - e2.date.getTime()));
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

    public void onViewPause() {
        disposable.clear();
    }

    public void onViewDetached() {
        disposable.dispose();
    }

    public void onBackSelected() {
        if (bucketView.get() != null){
            bucketView.get().back();
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
            bucketView.get().back();
        }
    }

    public void onAddEntryClick() {
        if (bucketView.get() != null){
            bucketView.get().goToAddEntry(currentBucket.title);
        }
    }
}
