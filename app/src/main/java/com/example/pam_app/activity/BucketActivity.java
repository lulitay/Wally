package com.example.pam_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_app.R;
import com.example.pam_app.adapter.BucketEntryAdapter;
import com.example.pam_app.databinding.ActivityBucketBinding;
import com.example.pam_app.db.WallyDatabase;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.presenter.BucketPresenter;
import com.example.pam_app.repository.BucketMapper;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.RoomBucketRepository;
import com.example.pam_app.view.BucketView;

public class BucketActivity extends AppCompatActivity implements BucketView {

    private BucketPresenter bucketPresenter;
    private ActivityBucketBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);

        int id = Integer.parseInt(getIntent().getData().getQueryParameter("id"));
        BucketRepository bucketRepository = new RoomBucketRepository(
                WallyDatabase.getInstance(getApplicationContext()).bucketDao(),
                new BucketMapper()
            );
        bucketPresenter = new BucketPresenter(id, this, bucketRepository);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_bucket);

        this.setUpToolBar();
        this.setUpList();
        this.setUpAddEntryButton();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bucketPresenter.onViewAttached();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bucketPresenter.onViewDetached();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id =  item.getItemId();
        if (id == android.R.id.home) {
            bucketPresenter.onBackSelected();
            return true;
        }
        else if (id == R.id.action_delete) {
            bucketPresenter.onDeleteSelected();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bucket_menu, menu);
        return true;
    }

    @Override
    public void bind(Bucket bucket) {
        this.binding.setBucket(bucket);
    }

    @Override
    public void back() {
        onBackPressed();
    }

    private void setUpList() {
        RecyclerView listView = findViewById(R.id.bucket_entries);
        BucketEntryAdapter adapter = new BucketEntryAdapter();
        listView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(listView, false);
    }

    private void setUpToolBar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setUpAddEntryButton() {
        Button addBucketButton = findViewById(R.id.add_entry_button);
        addBucketButton.setOnClickListener((View view) -> {
            Intent intent = new Intent(this, AddBucketEntryActivity.class);
            startActivity(intent);
        });
    }
}
