package com.example.pam_app.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pam_app.R
import com.example.pam_app.adapter.BucketEntryAdapter
import com.example.pam_app.model.Income
import com.example.pam_app.presenter.IncomePresenter
import com.google.android.material.textview.MaterialTextView

class IncomeViewImpl @kotlin.jvm.JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attributeSet, defStyleAttr), IncomeView {
    private val emptyNotice: MaterialTextView
    private var adapter: BucketEntryAdapter<Income>? = null
    private val incomePresenter: IncomePresenter
    private var incomeLeft: Double
    private val eye: ImageView
    private val incomeLeftText: MaterialTextView
    private var showAmount: Boolean
    private fun setOnEyeClickListener() {
        eye.setOnClickListener {
            showAmount = !showAmount
            setUpEye()
        }
    }

    override fun bind(incomeList: List<Income>?, incomeLeft: Double) {
        setUpEye()
        if (incomeList?.isEmpty()!!) {
            emptyNotice.visibility = View.VISIBLE
        } else {
            emptyNotice.visibility = View.GONE
        }
        this.incomeLeft = incomeLeft
        adapter!!.setData(incomeList)
        incomePresenter.onIncomeLeftAmountReceived(this.incomeLeft)
    }

    override fun onIncomeAdded(income: Income?) {
        if (income != null) {
            emptyNotice.visibility = View.GONE
            incomeLeft += income.amount
            incomePresenter.onIncomeLeftAmountReceived(incomeLeft)
            adapter!!.showNewBucket(income)
        }
    }

    override fun onBucketEntryAdded(amount: Double?) {
        incomeLeft -= amount!!
        incomePresenter.onIncomeLeftAmountReceived(incomeLeft)
    }

    override fun setUpIncomeLeftText(isPositive: Boolean) {
        if (showAmount) {
            incomeLeftText.text = context.getString(R.string.money_display, incomeLeft)
            incomeLeftText.setTextColor(
                    if (isPositive) context.resources.getColor(R.color.green, context.theme) else context.resources.getColor(R.color.red, context.theme)
            )
        }
    }

    private fun setUpList() {
        val listView: RecyclerView = findViewById(R.id.monthly_income)
        adapter = BucketEntryAdapter()
        listView.adapter = adapter
        ViewCompat.setNestedScrollingEnabled(listView, false)
    }

    private fun setUpEye() {
        if (showAmount) {
            eye.setImageResource(R.drawable.ic_nonvisibility)
            setUpIncomeLeftText(incomeLeft > 0)
        } else {
            eye.setImageResource(R.drawable.ic_visibility)
            incomeLeftText.text = "****.**"
            incomeLeftText.setTextColor(context.resources.getColor(R.color.highlightText, context.theme))
        }
    }

    init {
        View.inflate(context, R.layout.view_income, this)
        orientation = VERTICAL
        incomePresenter = IncomePresenter(this)
        showAmount = false
        emptyNotice = findViewById(R.id.income_unavailable)
        incomeLeft = 0.0
        eye = findViewById(R.id.eye)
        incomeLeftText = findViewById(R.id.income_left)
        setUpList()
        setOnEyeClickListener()
    }
}