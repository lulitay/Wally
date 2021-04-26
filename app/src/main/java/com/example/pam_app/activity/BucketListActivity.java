package com.example.pam_app.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.pam_app.R;
import com.example.pam_app.adapter.BucketListAdapter;
import com.example.pam_app.db.BucketEntity;
import com.example.pam_app.db.WallyDatabase;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketType;
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

    private List<Bucket> bucketList;

    private BucketListPresenter presenter;
    private BucketListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_list);
        if (bucketList == null) {
            bucketList = new ArrayList<>();
        }
        final BucketRepository bucketRepository = new RoomBucketRepository(
                WallyDatabase.getInstance(getApplicationContext()).bucketDao(),
                new BucketMapper()
        );
        presenter = new BucketListPresenter(bucketRepository, this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        bucketListView = findViewById(R.id.listView);
        addBucketButton = findViewById(R.id.add_entry_button);
        addBucketButton.setOnClickListener(v -> addBucket(v));


        adapter = new BucketListAdapter(this);
        bucketListView.setAdapter(adapter);
        presenter.onViewAttached();
    }

    private List<Bucket> adaptModel(List<BucketEntity> model) {
        final List<Bucket> list = new ArrayList<>();
        for (final BucketEntity entity : model) {
            list.add(new Bucket(entity.title, entity.dueDate, BucketType.values()[entity.bucketType], entity.target));
        }

        return list;
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }

    public void addBucket(View view) {
        Intent intent = new Intent(this, AddBucketActivity.class);
        startActivity(intent);
    }

    @Override
    public void bindBuckets(final List<Bucket> model) {
        adapter.update(model);
    }
}