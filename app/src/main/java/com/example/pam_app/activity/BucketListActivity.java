package com.example.pam_app.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.pam_app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BucketListActivity extends AppCompatActivity {
    ListView listview;
    Button addButton;
    EditText GetValue;
    String[] ListElements = new String[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_list);

        addButton = findViewById(R.id.button1);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}