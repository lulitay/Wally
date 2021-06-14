package com.example.pam_app.view;

import com.example.pam_app.model.Income;

public interface AddIncomeView {

    void onErrorSavingIncome();
    void onSuccessSavingIncome(final Income income);
    void showDescriptionError(final int error, final Integer parameter);
    void showAmountError(final int error, final Integer parameter);
    void showDateError(final int error);
}
