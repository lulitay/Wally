package com.example.pam_app.presenter

import com.example.pam_app.view.ProfileView
import java.lang.ref.WeakReference
import kotlin.jvm.Synchronized

class ProfilePresenter(view: ProfileView?) {
    private val view: WeakReference<ProfileView?>
    fun onApplyChangesClicked() {
        if (view.get() != null) {
            view.get()!!.applyChanges()
        }
    }

    init {
        this.view = WeakReference(view)
    }
}