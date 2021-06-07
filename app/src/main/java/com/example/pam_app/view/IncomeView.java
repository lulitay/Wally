package com.example.pam_app.view;

import com.example.pam_app.model.Income;

import java.util.List;

public interface IncomeView {

    void bind(final List<Income> incomeList);
    void onIncomeAdded(final Income income);
}
