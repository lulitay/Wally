package com.example.pam_app.presenter;

import com.example.pam_app.view.ProfileView;

import java.lang.ref.WeakReference;

public class ProfilePresenter {
    private final WeakReference<ProfileView> view;

    public ProfilePresenter(final ProfileView view) {
        this.view = new WeakReference<>(view);
    }

    public void onApplyChangesClicked(){
        if(view.get() != null) {
            view.get().applyChanges();
        }
    }
}
