package com.example.pam_app.presenter;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.view.BucketView;

import java.lang.ref.WeakReference;
import java.util.Collections;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BucketPresenter {

    private final int id;
    private final WeakReference<BucketView> bucketView;
    private final BucketRepository bucketRepository;
    private CompositeDisposable disposable;

    public BucketPresenter(final int id, final BucketView bucketView,
                           final BucketRepository bucketRepository) {
        this.id = id;
        this.bucketView = new WeakReference<>(bucketView);
        this.bucketRepository = bucketRepository;
    }

    public void onViewAttach() {
        this.disposable = new CompositeDisposable();
    }

    public void onViewResume() {
        disposable.add(
            bucketRepository.get(this.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Bucket b) -> {
                    if (bucketView.get() != null) {
                        Collections.sort(b.entries, (e1, e2) -> (int) Math.signum(e1.date.getTime() - e2.date.getTime()));
                        bucketView.get().bind(b);
                    }
                })//TODO handle error
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
        if (bucketView.get() != null){
            disposable.add(
                Completable.fromRunnable(() -> bucketRepository.delete(id))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()//TODO handle error
            );
            bucketView.get().back();
        }
    }

    public void onAddEntryClick() {
        if (bucketView.get() != null){
            bucketView.get().goToAddEntry();
        }
    }
}
