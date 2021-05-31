package com.example.pam_app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_app.R;
import com.example.pam_app.adapter.BucketEntryHomeAdapter;
import com.example.pam_app.db.WallyDatabase;
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.presenter.HomePresenter;
import com.example.pam_app.repository.BucketMapper;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.RoomBucketRepository;
import com.example.pam_app.utils.schedulers.AndroidSchedulerProvider;
import com.example.pam_app.utils.schedulers.SchedulerProvider;

import java.util.List;

import static android.view.Gravity.CENTER;

public class HomeViewImpl extends LinearLayout implements HomeView {

    private final HomePresenter homePresenter;
    private BucketEntryHomeAdapter adapter;

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

        final BucketRepository bucketRepository = new RoomBucketRepository(
                WallyDatabase.getInstance(context).bucketDao(),
                new BucketMapper()
        );
        final SchedulerProvider provider = new AndroidSchedulerProvider();
        homePresenter = new HomePresenter(bucketRepository, this, provider);
        setUpList();
    }

    @Override
    public void bind() {
        homePresenter.onViewAttached();
    }

    @Override
    public void onViewStop() {
        homePresenter.onViewDetached();
    }

    @Override
    public void onViewResumed() {
        homePresenter.onViewResume();
    }

    @Override
    public void onViewPaused() {
        //homePresenter.onViewPause();
    }

    @Override
    public void showEntries(final List<BucketEntry> entries) {
        adapter.update(entries);
    }

    private void setUpList() {
        final RecyclerView listView = findViewById(R.id.activity);
        adapter = new BucketEntryHomeAdapter();
        listView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(listView, false);
    }
}
