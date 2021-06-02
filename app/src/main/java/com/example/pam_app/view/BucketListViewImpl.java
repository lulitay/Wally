package com.example.pam_app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_app.R;
import com.example.pam_app.adapter.BucketListAdapter;
import com.example.pam_app.db.WallyDatabase;
import com.example.pam_app.model.BucketType;
import com.example.pam_app.utils.listener.Clickable;
import com.example.pam_app.utils.listener.ClickableWithParameter;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.presenter.BucketListPresenter;
import com.example.pam_app.repository.BucketMapper;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.RoomBucketRepository;
import com.example.pam_app.utils.schedulers.AndroidSchedulerProvider;
import com.example.pam_app.utils.schedulers.SchedulerProvider;

import java.util.List;

public class BucketListViewImpl extends LinearLayout implements BucketListView, ClickableWithParameter {

    private RecyclerView spendingBuckets;
    private RecyclerView savingsBuckets;

    private final BucketListPresenter presenter;
    private BucketListAdapter spendingAdapter;
    private BucketListAdapter savingsAdapter;

    private Clickable onAddBucketClickedListener;
    private ClickableWithParameter onBucketClickedListener;

    private boolean isSpendingListExpanded = true;
    private boolean isSavingsListExpanded = true;
    private boolean isSpendingListEmpty = true;
    private boolean isSavingsListEmpty = true;

    public BucketListViewImpl(Context context) {
        this(context, null);
    }

    public BucketListViewImpl(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BucketListViewImpl(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        inflate(context, R.layout.activity_bucket_list, this);
        setOrientation(VERTICAL);

        final BucketRepository bucketRepository = new RoomBucketRepository(
                WallyDatabase.getInstance(context).bucketDao(),
                new BucketMapper()
        );
        final SchedulerProvider provider = new AndroidSchedulerProvider();
        presenter = new BucketListPresenter(bucketRepository, this, provider);
    }

    @Override
    public void bind(
            final Context context,
            final Clickable onAddBucketClickedListener,
            final ClickableWithParameter onBucketClickedListener,
            final List<Bucket> bucketList
    ) {
        setUpAddBucketButton();
        setUpSpendingCard();
        setUpSavingsCard();
        setUpSpendingList(context);
        setUpSavingsList(context);

        this.onAddBucketClickedListener = onAddBucketClickedListener;
        this.onBucketClickedListener = onBucketClickedListener;
        presenter.onBucketsReceived(bucketList);
    }

    private void setUpAddBucketButton() {
        final Button addBucketButton = findViewById(R.id.add_bucket_button);
        addBucketButton.setOnClickListener(v -> presenter.OnAddBucketClicked());
    }

    private void setUpSpendingCard() {
        final CardView spendingCard = findViewById(R.id.spending_buckets_card);
        spendingCard.setOnClickListener(v -> presenter.OnSpendingCardClicked());
    }

    private void setUpSavingsCard() {
        final CardView savingsCard = findViewById(R.id.savings_buckets_card);
        savingsCard.setOnClickListener(v -> presenter.OnSavingsCardClicked());
    }

    private void setUpSpendingList(final Context context) {
        spendingBuckets = findViewById(R.id.spending_buckets);
        spendingAdapter = new BucketListAdapter();
        spendingBuckets.setAdapter(spendingAdapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        spendingBuckets.setLayoutManager(layoutManager);
        spendingAdapter.setOnClickListener(this);

        final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(spendingBuckets.getContext(),
                layoutManager.getOrientation());
        spendingBuckets.addItemDecoration(dividerItemDecoration);
    }

    private void setUpSavingsList(final Context context) {
        savingsBuckets = findViewById(R.id.savings_buckets);
        savingsAdapter = new BucketListAdapter();
        savingsBuckets.setAdapter(savingsAdapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        savingsBuckets.setLayoutManager(layoutManager);
        savingsAdapter.setOnClickListener(this);

        final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(savingsBuckets.getContext(),
                layoutManager.getOrientation());
        savingsBuckets.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void drawSpendingBucketList() {
        TextView bucketsUnavailable = findViewById(R.id.spending_buckets_unavailable);
        LinearLayout header = findViewById(R.id.spending_header);
        ImageView indicator = findViewById(R.id.spending_buckets_collapsed_indicator);

        if (isSpendingListEmpty) {
            bucketsUnavailable.setVisibility(View.VISIBLE);
        } else {
            spendingBuckets.setVisibility(View.VISIBLE);
            header.setVisibility(View.VISIBLE);
        }
        isSpendingListExpanded = true;
        indicator.setRotation(180);
    }

    @Override
    public void drawSavingsBucketList() {
        TextView bucketsUnavailable = findViewById(R.id.savings_buckets_unavailable);
        LinearLayout header = findViewById(R.id.savings_header);
        ImageView indicator = findViewById(R.id.savings_buckets_collapsed_indicator);

        if (isSavingsListEmpty) {
            bucketsUnavailable.setVisibility(View.VISIBLE);
        } else {
            savingsBuckets.setVisibility(View.VISIBLE);
            header.setVisibility(View.VISIBLE);
        }
        isSavingsListExpanded = true;
        indicator.setRotation(180);
    }

    @Override
    public void onViewStop() {
        presenter.onViewDetached();
    }

    @Override
    public void onViewResume() {
        presenter.onViewResume();
    }

    @Override
    public void setIsSpendingListEmpty(boolean isEmpty) {
        isSpendingListEmpty = isEmpty;
    }

    @Override
    public void setIsSavingsListEmpty(boolean isEmpty) {
        isSavingsListEmpty = isEmpty;
    }

    @Override
    public void onBucketAdded(final Bucket bucket) {
        if (bucket != null) {
            if (bucket.bucketType.equals(BucketType.SAVING)) {
                savingsAdapter.showNewBucket(bucket);
            } else {
                spendingAdapter.showNewBucket(bucket);
            }
        }
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
        onBucketClickedListener.onClick(bucketId);
    }

    @Override
    public void launchAddBucketActivity() {
        onAddBucketClickedListener.onClick();
    }

    @Override
    public void collapseSpendingBuckets() {
        ImageView indicator = findViewById(R.id.spending_buckets_collapsed_indicator);
        LinearLayout header = findViewById(R.id.spending_header);
        TextView bucketsUnavailable = findViewById(R.id.spending_buckets_unavailable);

        if (isSpendingListExpanded) {
            bucketsUnavailable.setVisibility(View.GONE);
            spendingBuckets.setVisibility(View.GONE);
            header.setVisibility(View.GONE);
            isSpendingListExpanded = false;
            indicator.setRotation(0);
        } else {
            drawSpendingBucketList();
        }
    }

    @Override
    public void collapseSavingsBuckets() {
        ImageView indicator = findViewById(R.id.savings_buckets_collapsed_indicator);
        LinearLayout header = findViewById(R.id.savings_header);
        TextView bucketsUnavailable = findViewById(R.id.savings_buckets_unavailable);

        if (isSavingsListExpanded) {
            bucketsUnavailable.setVisibility(View.GONE);
            savingsBuckets.setVisibility(View.GONE);
            header.setVisibility(View.GONE);
            isSavingsListExpanded = false;
            indicator.setRotation(0);
        } else {
            drawSavingsBucketList();
        }
    }

    @Override
    public void onClick(int bucketId) {
        presenter.onBucketClicked(bucketId);
    }
}
