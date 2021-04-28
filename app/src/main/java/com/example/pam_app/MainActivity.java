package com.example.pam_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pam_app.activity.AddBucketActivity;
import com.example.pam_app.activity.AddBucketEntryActivity;
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

        final FloatingActionButton fab = findViewById(R.id.fab);
        final Button addBucketButton = findViewById(R.id.add_bucket);
        fab.setOnClickListener(this::addEntry);
        addBucketButton.setOnClickListener(this::addBucket);
    }

    public void addEntry(final View view) {
        final Intent intent = new Intent(this, AddBucketEntryActivity.class);
        startActivity(intent);
    }

    public void addBucket(final View view) {
        final Intent intent = new Intent(this, AddBucketActivity.class);
        startActivity(intent);
    }
}