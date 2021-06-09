package com.example.pam_app.view;

import com.example.pam_app.model.Income;

public interface AddIncomeView {

    void onErrorSavingIncome();
    void onSuccessSavingIncome(final Income income);
}
