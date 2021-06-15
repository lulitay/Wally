package com.example.pam_app.presenter;

import com.example.pam_app.view.IncomeView;

import java.lang.ref.WeakReference;

public class IncomePresenter {

    private final WeakReference<IncomeView> incomeView;

    public IncomePresenter(final IncomeView incomeView) {
        this.incomeView = new WeakReference<>(incomeView);
    }

    public void onIncomeLeftAmountReceived(final Double incomeLeft) {
        if (incomeView.get() != null) {
            incomeView.get().setUpIncomeLeftText(incomeLeft > 0);
        }
    }
}
