package com.example.pam_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pam_app.activity.AddEntryActivity;
import com.example.pam_app.activity.FTUActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private static final String SP_ID = "demo-pref";
    private static final String FTU = "ftu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences sharedPreferences = getSharedPreferences(SP_ID, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(FTU, true)) {
            sharedPreferences.edit().putBoolean(FTU, false).apply();
            startActivity(new Intent(this, FTUActivity.class));
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEntry(view);
            }
        });

    }

    public void addEntry(final View view) {
        Intent intent = new Intent(this, AddEntryActivity.class);
        startActivity(intent);
    }
}