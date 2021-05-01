package com.example.pam_app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.pam_app.R;
import com.example.pam_app.adapter.BucketListAdapter;
import com.example.pam_app.db.WallyDatabase;
import com.example.pam_app.listener.OnBucketClickedListener;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.presenter.BucketListPresenter;
import com.example.pam_app.repository.BucketMapper;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.RoomBucketRepository;
import com.example.pam_app.view.BucketListView;

import java.util.List;

public class BucketListActivity extends AppCompatActivity implements BucketListView, OnBucketClickedListener {
    private RecyclerView spendingBuckets;
    private RecyclerView savingsBuckets;

    private BucketListPresenter presenter;
    private BucketListAdapter spendingAdapter;
    private BucketListAdapter savingsAdapter;

    private boolean isSpendingListExpanded = false;
    private boolean isSavingsListExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_list);
        final BucketRepository bucketRepository = new RoomBucketRepository(
                WallyDatabase.getInstance(getApplicationContext()).bucketDao(),
                new BucketMapper()
        );
        presenter = new BucketListPresenter(bucketRepository, this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        spendingBuckets = findViewById(R.id.spending_buckets);
        savingsBuckets = findViewById(R.id.savings_buckets);
        Button addBucketButton = findViewById(R.id.add_entry_button);
        CardView spendingCard = findViewById(R.id.spending_buckets_card);
        CardView savingsCard = findViewById(R.id.savings_buckets_card);

        addBucketButton.setOnClickListener(v -> presenter.OnAddBucketClicked());
        spendingCard.setOnClickListener(v -> presenter.OnSpendingCardClicked());
        savingsCard.setOnClickListener(v -> presenter.OnSavingsCardClicked());

        spendingAdapter = new BucketListAdapter();
        spendingBuckets.setAdapter(spendingAdapter);
        spendingBuckets.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        spendingAdapter.setOnClickListener(this);

        savingsAdapter = new BucketListAdapter();
        savingsBuckets.setAdapter(savingsAdapter);
        savingsBuckets.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        savingsAdapter.setOnClickListener(this);

        presenter.onViewAttached();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }

    @Override
    public void bindSpendingBuckets(final List<Bucket> model) {
        spendingAdapter.update(model);
    }

    @Override
    public void bindSavingsBuckets(final List<Bucket> model) {
        savingsAdapter.update(model);
    }

    @Override
    public void launchBucketDetailActivity(int bucketId) {
        String uri = "wally://bucket/detail?id=" + bucketId;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    @Override
    public void launchAddBucketActivity() {
        final Intent intent = new Intent(this, AddBucketActivity.class);
        startActivity(intent);
    }

    @Override
    public void collapseSpendingBuckets() {
        ImageView indicator = findViewById(R.id.spending_buckets_collapsed_indicator);
        if(isSpendingListExpanded) {
            spendingBuckets.setVisibility(View.GONE);
            isSpendingListExpanded = false;
            indicator.setRotation(0);
        } else {
            spendingBuckets.setVisibility(View.VISIBLE);
            isSpendingListExpanded = true;
            indicator.setRotation(180);
        }
    }

    @Override
    public void collapseSavingsBuckets() {
        ImageView indicator = findViewById(R.id.savings_buckets_collapsed_indicator);
        if(isSavingsListExpanded) {
            savingsBuckets.setVisibility(View.GONE);
            isSavingsListExpanded = false;
            indicator.setRotation(0);
        } else {
            savingsBuckets.setVisibility(View.VISIBLE);
            isSavingsListExpanded = true;
            indicator.setRotation(180);
        }
    }

    @Override
    public void onClick(int bucketId) {
        presenter.onBucketClicked(bucketId);
    }
}