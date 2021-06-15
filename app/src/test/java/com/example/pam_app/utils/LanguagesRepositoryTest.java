package com.example.pam_app.utils;

import android.content.SharedPreferences;

import com.example.pam_app.repository.LanguagesRepository;

import java.util.Locale;

public class LanguagesRepositoryTest implements LanguagesRepository {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private String currentLocale;



    @Override
    public Locale getCurrentLocale() {
        return new Locale(currentLocale);
    }

    @Override
    public String getKeyPrefLanguage(String key) {
        return currentLocale;
    }

    @Override
    public void setOnSharedPreferencesListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {

    }

    @Override
    public void unregisterOnSharedPreferencesListener() {

    }

    @Override
    public void changeLanguage(String language) {

    }
}
