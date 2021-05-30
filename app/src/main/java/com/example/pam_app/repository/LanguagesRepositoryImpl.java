package com.example.pam_app.repository;

import android.content.SharedPreferences;

import java.util.Locale;

public class LanguagesRepositoryImpl implements LanguagesRepository {

    public static final String KEY_PREF_LANGUAGE = "pref_language";
    private final SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    public LanguagesRepositoryImpl(final SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Locale getCurrentLocale() {
        return new Locale(sharedPreferences.getString(KEY_PREF_LANGUAGE, "en"));
    }

    @Override
    public String getKeyPrefLanguage(final String key) {
        return sharedPreferences.getString(key, "");
    }

    @Override
    public void setOnSharedPreferencesListener(final SharedPreferences.OnSharedPreferenceChangeListener listener) {
        this.listener = listener;
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferencesListener() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void changeLanguage(final String language) {
        sharedPreferences.edit().putString(KEY_PREF_LANGUAGE, language).apply();
    }
}
