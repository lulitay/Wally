package com.example.pam_app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_app.R;
import com.example.pam_app.adapter.BucketEntryAdapter;
import com.example.pam_app.model.Income;
import com.example.pam_app.presenter.IncomePresenter;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class IncomeViewImpl extends LinearLayout implements IncomeView {

    private final Context context;
    private final MaterialTextView emptyNotice;
    private BucketEntryAdapter<Income> adapter;
    private final IncomePresenter incomePresenter;
    private Double incomeLeft;

    public IncomeViewImpl(Context context) {
        this(context, null);
    }

    public IncomeViewImpl(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public IncomeViewImpl(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        inflate(context, R.layout.view_income, this);
        setOrientation(VERTICAL);
        this.incomePresenter = new IncomePresenter(this);
        this.context = context;
        this.emptyNotice = findViewById(R.id.income_unavailable);
        this.incomeLeft = 0.0;

        setUpList();
    }

    @Override
    public void bind(final List<Income> incomeList, final Double incomeLeft) {
        if (incomeList.isEmpty()) {
            emptyNotice.setVisibility(VISIBLE);
            this.incomeLeft = 0.0;
        } else {
            emptyNotice.setVisibility(GONE);
            this.incomeLeft = incomeLeft;
        }
        adapter.setData(incomeList);
        incomePresenter.onIncomeLeftAmountReceived(this.incomeLeft);
    }

    @Override
    public void onIncomeAdded(final Income income) {
        if (income != null) {
            emptyNotice.setVisibility(GONE);
            this.incomeLeft = incomeLeft + income.getAmount();
            incomePresenter.onIncomeLeftAmountReceived(incomeLeft);
            adapter.showNewBucket(income);
        }
    }

    @Override
    public void onBucketEntryAdded(final Double amount) {
        this.incomeLeft = incomeLeft - amount;
        incomePresenter.onIncomeLeftAmountReceived(incomeLeft);
    }

    @Override
    public void setUpIncomeLeftText(final boolean isPositive) {
        final MaterialTextView incomeLeftText = findViewById(R.id.income_left);
        incomeLeftText.setText(context.getString(R.string.money_display, this.incomeLeft));
        incomeLeftText.setTextColor(
                isPositive ? context.getResources().getColor(R.color.green, context.getTheme()) :
                        context.getResources().getColor(R.color.red, context.getTheme())
        );
    }

    private void setUpList() {
        final RecyclerView listView = findViewById(R.id.monthly_income);
        adapter = new BucketEntryAdapter<>();
        listView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(listView, false);
    }
}
