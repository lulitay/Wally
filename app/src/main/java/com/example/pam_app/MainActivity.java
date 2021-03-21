package com.example.pam_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String SP_ID = "demo-pref";
    private static final String FTU = "ftu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        final SharedPreferences sharedPreferences = getSharedPreferences(SP_ID, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(FTU, true)) {
            sharedPreferences.edit().putBoolean(FTU, false).apply();
            startActivity(new Intent(this, FTUActivity.class));
        }
    }
}