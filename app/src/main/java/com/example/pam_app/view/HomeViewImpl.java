package com.example.pam_app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_app.R;
import com.example.pam_app.adapter.BucketEntryAdapter;
import com.example.pam_app.db.WallyDatabase;
import com.example.pam_app.utils.listener.Clickable;
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.presenter.HomePresenter;
import com.example.pam_app.repository.BucketMapper;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.RoomBucketRepository;

import java.util.List;

import static android.view.Gravity.CENTER;

public class HomeViewImpl extends LinearLayout implements HomeView {

    private final Button addBucketButton;
    private Clickable clickable;
    private final HomePresenter homePresenter;
    private BucketEntryAdapter adapter;

    public HomeViewImpl(Context context) {
        this(context, null);
    }

    public HomeViewImpl(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public HomeViewImpl(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);

        inflate(context, R.layout.home_view, this);
        setGravity(CENTER);
        setOrientation(VERTICAL);

        addBucketButton = findViewById(R.id.add_bucket);

        final BucketRepository bucketRepository = new RoomBucketRepository(
                WallyDatabase.getInstance(context).bucketDao(),
                new BucketMapper()
        );
        homePresenter = new HomePresenter(bucketRepository, this);
        setUpList();
    }

    @Override
    public void bind() {
        addBucketButton.setOnClickListener(v -> {
            if (clickable != null) {
                clickable.onClick();
            }
        });
        homePresenter.onViewAttach();
    }

    @Override
    public void setClickable(final Clickable listener) {
        clickable = listener;
    }

    @Override
    public void onViewStopped() {
        homePresenter.onViewDetached();
    }

    @Override
    public void onViewResumed() {
        //homePresenter.onViewResume();
    }

    @Override
    public void onViewPaused() {
     //   homePresenter.onViewPause();
    }

    @Override
    public void showEntries(final List<BucketEntry> entries) {
        adapter.update(entries);
    }

    private void setUpList() {
        final RecyclerView listView = findViewById(R.id.activity);
        adapter = new BucketEntryAdapter();
        listView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(listView, false);
    }
}
