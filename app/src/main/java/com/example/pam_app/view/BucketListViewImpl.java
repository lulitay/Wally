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
import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketType;
import com.example.pam_app.presenter.BucketListPresenter;
import com.example.pam_app.utils.listener.Clickable;
import com.example.pam_app.utils.listener.ClickableTarget;

import java.util.List;

public class BucketListViewImpl extends LinearLayout implements BucketListView, ClickableTarget<Integer> {

    private RecyclerView spendingBuckets;
    private RecyclerView savingsBuckets;

    private final BucketListPresenter presenter;
    private BucketListAdapter spendingAdapter;
    private BucketListAdapter savingsAdapter;

    private Clickable onAddBucketClickedListener;
    private ClickableTarget<Integer> onBucketClickedListener;

    private boolean isSpendingListExpanded = true;
    private boolean isSavingsListExpanded = true;
    private boolean isSpendingListEmpty = true;
    private boolean isSavingsListEmpty = true;

    private final TextView spendingBucketsUnavailable;
    private final TextView savingsBucketsUnavailable;

    private final LinearLayout spendingHeader;
    private final LinearLayout savingHeader;

    public BucketListViewImpl(Context context) {
        this(context, null);
    }

    public BucketListViewImpl(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BucketListViewImpl(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        inflate(context, R.layout.view_bucket_list, this);
        setOrientation(VERTICAL);
        presenter = new BucketListPresenter(this);
        this.spendingHeader = findViewById(R.id.spending_header);
        this.savingHeader = findViewById(R.id.savings_header);
        this.spendingBucketsUnavailable = findViewById(R.id.spending_buckets_unavailable);
        this.savingsBucketsUnavailable = findViewById(R.id.savings_buckets_unavailable);

    }

    @Override
    public void bind(
            final Context context,
            final Clickable onAddBucketClickedListener,
            final ClickableTarget<Integer> onBucketClickedListener,
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
        addBucketButton.setOnClickListener(v -> presenter.onAddBucketClicked());
    }

    private void setUpSpendingCard() {
        final CardView spendingCard = findViewById(R.id.spending_buckets_card);
        spendingCard.setOnClickListener(v -> presenter.onSpendingCardClicked());
    }

    private void setUpSavingsCard() {
        final CardView savingsCard = findViewById(R.id.savings_buckets_card);
        savingsCard.setOnClickListener(v -> presenter.onSavingsCardClicked());
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
        ImageView indicator = findViewById(R.id.spending_buckets_collapsed_indicator);

        if (isSpendingListEmpty) {
            spendingBucketsUnavailable.setVisibility(View.VISIBLE);
        } else {
            spendingBuckets.setVisibility(View.VISIBLE);
            spendingHeader.setVisibility(View.VISIBLE);
            spendingBucketsUnavailable.setVisibility(GONE);
        }
        isSpendingListExpanded = true;
        indicator.setRotation(180);
    }

    @Override
    public void drawSavingsBucketList() {
        ImageView indicator = findViewById(R.id.savings_buckets_collapsed_indicator);

        if (isSavingsListEmpty) {
            savingsBucketsUnavailable.setVisibility(View.VISIBLE);
        } else {
            savingsBuckets.setVisibility(View.VISIBLE);
            savingHeader.setVisibility(View.VISIBLE);
            savingsBucketsUnavailable.setVisibility(GONE);
        }
        isSavingsListExpanded = true;
        indicator.setRotation(180);
    }

    @Override
    public void onDeleteBucket(Integer id) {
        savingsAdapter.delete(id);
        spendingAdapter.delete(id);
        //TODO Remove message
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
        if (bucket != null && bucket.bucketType.equals(BucketType.SAVING)) {
            isSavingsListEmpty = false;
            drawSavingsBucketList();
            savingsAdapter.showNewBucket(bucket);
        } else if (bucket != null) {
            isSpendingListEmpty = false;
            drawSpendingBucketList();
            spendingAdapter.showNewBucket(bucket);
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
    public void onClick(final Integer bucketId) {
        presenter.onBucketClicked(bucketId);
    }
}
