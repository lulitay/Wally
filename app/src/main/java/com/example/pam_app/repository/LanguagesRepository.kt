package com.example.pam_app.repository

import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import java.util.*
import kotlin.jvm.Synchronized

interface LanguagesRepository {
    val currentLocale: Locale
    fun getKeyPrefLanguage(key: String?): String?
    fun setOnSharedPreferencesListener(listener: OnSharedPreferenceChangeListener?)
    fun unregisterOnSharedPreferencesListener()
    fun changeLanguage(language: String?)
}