package com.example.pam_app.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.pam_app.R;
import com.example.pam_app.adapter.BucketListAdapter;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketType;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

public class BucketListActivity extends AppCompatActivity {
    Button addBucketButton;
    ListView bucketListView;

    private Bucket savingsBucket = new Bucket("Car", Date.valueOf("2021-06-12"), BucketType.SAVING, 20000.0, null);
    private Bucket spendingBucket = new Bucket("Food", Date.valueOf("2021-06-12"), BucketType.SPENDING, 10000.0, null);
    private ArrayList<Bucket> bucketList = new ArrayList(Arrays.asList(savingsBucket, spendingBucket));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_list);

        bucketListView = findViewById(R.id.listView);
        addBucketButton = findViewById(R.id.add_entry_button);
        addBucketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBucket(v);
            }
        });

        final ArrayAdapter<Bucket> adapter = new BucketListAdapter
                (this, bucketList);
        bucketListView.setAdapter(adapter);
    }

    public void addBucket(View view) {
        Intent intent = new Intent(this, AddEntryActivity.class);
        startActivity(intent);
    }
}