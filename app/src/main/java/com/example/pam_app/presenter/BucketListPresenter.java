package com.example.pam_app.presenter;

import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.view.BucketListView;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BucketListPresenter {
    private final WeakReference<BucketListView> view;
    private final BucketRepository repository;
    private Disposable disposable;

    public BucketListPresenter(final BucketRepository repository, final BucketListView view) {
        this.view = new WeakReference<>(view);
        this.repository = repository;
    }

    public void onViewAttached() {
        disposable = repository.getList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (view != null) {
                        view.get().bindBuckets(model);
                    }
                });
    }

    public void onViewDetached() {
        disposable.dispose();
    }

}
