package com.example.pam_app.view;

import com.example.pam_app.model.Income;

import java.util.List;

public interface IncomeView {

    void bind(final List<Income> incomeList, final Double incomeLeft);
    void onIncomeAdded(final Income income);
    void onBucketEntryAdded(final Double amount);
    void setUpIncomeLeftText(final boolean isPositive);
}
