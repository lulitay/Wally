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
import com.example.pam_app.db.WallyDatabase;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.presenter.BucketListPresenter;
import com.example.pam_app.repository.BucketMapper;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.RoomBucketRepository;
import com.example.pam_app.view.BucketListView;

import java.util.ArrayList;
import java.util.List;

public class BucketListActivity extends AppCompatActivity implements BucketListView {
    private Button addBucketButton;
    private ListView bucketListView;

    private BucketListPresenter presenter;

    private List<Bucket> bucketList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_list);

        final BucketRepository bucketRepository = new RoomBucketRepository(
                WallyDatabase.getInstance(getApplicationContext()).bucketDao(),
                new BucketMapper()
        );
        presenter = new BucketListPresenter(this, bucketRepository);

        bucketListView = findViewById(R.id.listView);
        addBucketButton = findViewById(R.id.add_entry_button);
        addBucketButton.setOnClickListener(v -> addBucket(v));

        bucketList = presenter.getBuckets();

        final ArrayAdapter<Bucket> adapter = new BucketListAdapter
                (this, bucketList);
        bucketListView.setAdapter(adapter);
    }

    public void addBucket(View view) {
        Intent intent = new Intent(this, AddBucketActivity.class);
        startActivity(intent);
    }
}