package com.example.pam_app.repository

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import java.util.*

class LanguagesRepositoryImpl(private val sharedPreferences: SharedPreferences) : LanguagesRepository {
    private var listener: OnSharedPreferenceChangeListener? = null
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override var currentLocale: Locale = Locale(sharedPreferences.getString(KEY_PREF_LANGUAGE, "en"), sharedPreferences.getString(KEY_PREF_COUNTRY, "US"))

    override fun getKeyPrefLanguage(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }

    override fun setOnSharedPreferencesListener(listener: OnSharedPreferenceChangeListener?) {
        this.listener = listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun unregisterOnSharedPreferencesListener() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun changeLanguage(language: String?) {
        sharedPreferences.edit().putString(KEY_PREF_LANGUAGE, language).apply()
        sharedPreferences.edit().putString(KEY_PREF_COUNTRY, if (language == "es") "ES" else "US").apply()
    }

    companion object {
        const val KEY_PREF_LANGUAGE = "pref_language"
        const val KEY_PREF_COUNTRY = "pref_language"
    }

}