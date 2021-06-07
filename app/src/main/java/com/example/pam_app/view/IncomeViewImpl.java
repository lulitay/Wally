package com.example.pam_app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_app.R;
import com.example.pam_app.adapter.IncomeAdapter;
import com.example.pam_app.model.Income;

import java.util.List;

public class IncomeViewImpl extends LinearLayout implements IncomeView {

    private IncomeAdapter adapter;

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

        setUpList();
    }

    @Override
    public void bind(final List<Income> incomeList) {
        adapter.update(incomeList);
    }

    @Override
    public void onIncomeAdded(final Income income) {
        adapter.showNewIncome(income);
    }

    private void setUpList() {
        final RecyclerView listView = findViewById(R.id.monthly_income);
        adapter = new IncomeAdapter();
        listView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(listView, false);
    }
}
