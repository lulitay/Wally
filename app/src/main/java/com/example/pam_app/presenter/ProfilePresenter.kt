package com.example.pam_app.presenter

import com.example.pam_app.view.ProfileView
import java.lang.ref.WeakReference

class ProfilePresenter(view: ProfileView?) {
    private val view: WeakReference<ProfileView?> = WeakReference(view)
    fun onApplyChangesClicked() {
        if (view.get() != null) {
            view.get()!!.applyChanges()
        }
    }

}