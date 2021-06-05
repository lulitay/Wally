package com.example.pam_app.presenter;

import com.example.pam_app.view.HomeView;

import java.lang.ref.WeakReference;

public class HomePresenter {

    private final WeakReference<HomeView> homeView;

    public HomePresenter(final HomeView view) {
        this.homeView = new WeakReference<>(view);
    }
}
