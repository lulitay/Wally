package com.example.pam_app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_app.R;
import com.example.pam_app.adapter.BucketEntryAdapter;
import com.example.pam_app.model.BucketEntry;

import java.util.List;

import static android.view.Gravity.CENTER;

public class HomeViewImpl extends LinearLayout implements HomeView {

    private BucketEntryAdapter<BucketEntry> adapter;

    public HomeViewImpl(Context context) {
        this(context, null);
    }

    public HomeViewImpl(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public HomeViewImpl(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);

        inflate(context, R.layout.view_home, this);
        setGravity(CENTER);
        setOrientation(VERTICAL);
        setUpList();
    }

    @Override
    public void bind(final List<BucketEntry> entryList) {
        adapter.setData(entryList);
    }

    @Override
    public void onBucketEntryAdded(final BucketEntry bucketEntry) {
        if (bucketEntry != null) {
            adapter.showNewBucket(bucketEntry);
        }
    }

    private void setUpList() {
        final RecyclerView listView = findViewById(R.id.activity);
        adapter = new BucketEntryAdapter<>();
        listView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(listView, false);
    }
}
