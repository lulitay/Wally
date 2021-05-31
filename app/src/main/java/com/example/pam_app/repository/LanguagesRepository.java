package com.example.pam_app.repository;

import android.content.SharedPreferences;

import java.util.Locale;

public interface LanguagesRepository {

    Locale getCurrentLocale();
    String getKeyPrefLanguage(final String key);
    void setOnSharedPreferencesListener(final SharedPreferences.OnSharedPreferenceChangeListener listener);
    void unregisterOnSharedPreferencesListener();
    void changeLanguage(final String language);
}
