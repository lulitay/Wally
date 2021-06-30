package com.example.pam_app.view

import com.example.pam_app.utils.listener.ClickableTarget

interface ProfileView {
    fun bind(currentLanguage: String?, applyChanges: (String) -> Unit)
    fun applyChanges()
}