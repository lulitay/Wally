package com.example.pam_app.view

interface ProfileView {
    fun bind(currentLanguage: String?, applyChanges: (String) -> Unit)
    fun applyChanges()
}