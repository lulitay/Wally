package com.example.pam_app.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pam_app.R
import com.example.pam_app.adapter.BucketListAdapter
import com.example.pam_app.model.Bucket
import com.example.pam_app.model.BucketType
import com.example.pam_app.presenter.BucketListPresenter
import com.example.pam_app.utils.listener.ClickableTarget

class BucketListViewImpl @kotlin.jvm.JvmOverloads constructor(context: Context?, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attributeSet, defStyleAttr), BucketListView, ClickableTarget<Int> {
    private lateinit var spendingBuckets: RecyclerView
    private lateinit var savingsBuckets: RecyclerView
    private val presenter: BucketListPresenter
    private var spendingAdapter: BucketListAdapter? = null
    private var savingsAdapter: BucketListAdapter? = null
    private lateinit var onAddBucketClickedListener: () -> Unit
    private lateinit var onBucketClickedListener: (Int) -> Unit
    private var isSpendingListExpanded = true
    private var isSavingsListExpanded = true
    private var isSpendingListEmpty = true
    private var isSavingsListEmpty = true
    private val spendingBucketsUnavailable: TextView
    private val savingsBucketsUnavailable: TextView
    private val spendingHeader: LinearLayout
    private val savingHeader: LinearLayout
    override fun bind(
            context: Context,
            launchAddBucketActivity: () -> Unit,
            launchBucketDetailActivity: (Int) -> Unit,
            bucketList: List<Bucket?>?
    ) {
        setUpAddBucketButton()
        setUpSpendingCard()
        setUpSavingsCard()
        setUpSpendingList(context)
        setUpSavingsList(context)
        this.onAddBucketClickedListener = launchAddBucketActivity
        this.onBucketClickedListener = launchBucketDetailActivity
        presenter.onBucketsReceived(bucketList)
    }

    private fun setUpAddBucketButton() {
        val addBucketButton = findViewById<Button>(R.id.add_bucket_button)
        addBucketButton.setOnClickListener { presenter.onAddBucketClicked() }
    }

    private fun setUpSpendingCard() {
        val spendingCard: CardView = findViewById(R.id.spending_buckets_card)
        spendingCard.setOnClickListener { presenter.onSpendingCardClicked() }
    }

    private fun setUpSavingsCard() {
        val savingsCard: CardView = findViewById(R.id.savings_buckets_card)
        savingsCard.setOnClickListener { presenter.onSavingsCardClicked() }
    }

    private fun setUpSpendingList(context: Context) {
        spendingBuckets = findViewById(R.id.spending_buckets)
        spendingAdapter = BucketListAdapter()
        spendingBuckets.adapter = spendingAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        spendingBuckets.layoutManager = layoutManager
        spendingAdapter!!.setOnClickListener(this)
        val dividerItemDecoration = DividerItemDecoration(spendingBuckets.context,
                layoutManager.orientation)
        spendingBuckets.addItemDecoration(dividerItemDecoration)
    }

    private fun setUpSavingsList(context: Context) {
        savingsBuckets = findViewById(R.id.savings_buckets)
        savingsAdapter = BucketListAdapter()
        savingsBuckets.adapter = savingsAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        savingsBuckets.layoutManager = layoutManager
        savingsAdapter!!.setOnClickListener(this)
        val dividerItemDecoration = DividerItemDecoration(savingsBuckets.context,
                layoutManager.orientation)
        savingsBuckets.addItemDecoration(dividerItemDecoration)
    }

    override fun drawSpendingBucketList() {
        val indicator = findViewById<ImageView>(R.id.spending_buckets_collapsed_indicator)
        if (isSpendingListEmpty) {
            spendingBucketsUnavailable.visibility = View.VISIBLE
        } else {
            spendingBuckets.visibility = View.VISIBLE
            spendingHeader.visibility = View.VISIBLE
            spendingBucketsUnavailable.visibility = View.GONE
        }
        isSpendingListExpanded = true
        indicator.rotation = 180f
    }

    override fun drawSavingsBucketList() {
        val indicator = findViewById<ImageView>(R.id.savings_buckets_collapsed_indicator)
        if (isSavingsListEmpty) {
            savingsBucketsUnavailable.visibility = View.VISIBLE
        } else {
            savingsBuckets.visibility = View.VISIBLE
            savingHeader.visibility = View.VISIBLE
            savingsBucketsUnavailable.visibility = View.GONE
        }
        isSavingsListExpanded = true
        indicator.rotation = 180f
    }

    override fun onDeleteBucket(id: Int) {
        savingsAdapter!!.delete(id)
        spendingAdapter!!.delete(id)
        if (savingsAdapter!!.itemCount == 0) {
            isSavingsListEmpty = true
            savingsBucketsUnavailable.visibility = View.VISIBLE
        }
        if (spendingAdapter!!.itemCount == 0) {
            isSpendingListEmpty = true
            spendingBucketsUnavailable.visibility = View.VISIBLE
        }
    }

    override fun setIsSpendingListEmpty(isEmpty: Boolean) {
        isSpendingListEmpty = isEmpty
    }

    override fun setIsSavingsListEmpty(isEmpty: Boolean) {
        isSavingsListEmpty = isEmpty
    }

    override fun onBucketAdded(bucket: Bucket?) {
        if (bucket != null && bucket.bucketType == BucketType.SAVING) {
            isSavingsListEmpty = false
            drawSavingsBucketList()
            savingsAdapter!!.showNewBucket(bucket)
        } else if (bucket != null) {
            isSpendingListEmpty = false
            drawSpendingBucketList()
            spendingAdapter!!.showNewBucket(bucket)
        }
    }

    override fun bindSpendingBuckets(model: List<Bucket?>?) {
        spendingAdapter!!.update(model)
    }

    override fun bindSavingsBuckets(model: List<Bucket?>?) {
        savingsAdapter!!.update(model)
    }

    override fun launchBucketDetailActivity(position: Int) {
        onBucketClickedListener(position)
    }

    override fun launchAddBucketActivity() {
        onAddBucketClickedListener()
    }

    override fun collapseSpendingBuckets() {
        val indicator = findViewById<ImageView>(R.id.spending_buckets_collapsed_indicator)
        val header = findViewById<LinearLayout>(R.id.spending_header)
        val bucketsUnavailable = findViewById<TextView>(R.id.spending_buckets_unavailable)
        if (isSpendingListExpanded) {
            bucketsUnavailable.visibility = View.GONE
            spendingBuckets.visibility = View.GONE
            header.visibility = View.GONE
            isSpendingListExpanded = false
            indicator.rotation = 0f
        } else {
            drawSpendingBucketList()
        }
    }

    override fun collapseSavingsBuckets() {
        val indicator = findViewById<ImageView>(R.id.savings_buckets_collapsed_indicator)
        val header = findViewById<LinearLayout>(R.id.savings_header)
        val bucketsUnavailable = findViewById<TextView>(R.id.savings_buckets_unavailable)
        if (isSavingsListExpanded) {
            bucketsUnavailable.visibility = View.GONE
            savingsBuckets.visibility = View.GONE
            header.visibility = View.GONE
            isSavingsListExpanded = false
            indicator.rotation = 0f
        } else {
            drawSavingsBucketList()
        }
    }

    override fun onClick(id: Int) {
        presenter.onBucketClicked(id)
    }

    init {
        View.inflate(context, R.layout.view_bucket_list, this)
        orientation = VERTICAL
        presenter = BucketListPresenter(this)
        spendingHeader = findViewById(R.id.spending_header)
        savingHeader = findViewById(R.id.savings_header)
        spendingBucketsUnavailable = findViewById(R.id.spending_buckets_unavailable)
        savingsBucketsUnavailable = findViewById(R.id.savings_buckets_unavailable)
    }
}