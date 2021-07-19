package com.example.pam_app.presenter

import com.example.pam_app.view.IncomeView
import java.lang.ref.WeakReference
import kotlin.jvm.Synchronized

class IncomePresenter(incomeView: IncomeView?) {
    private val incomeView: WeakReference<IncomeView?>
    fun onIncomeLeftAmountReceived(incomeLeft: Double) {
        if (incomeView.get() != null) {
            incomeView.get()!!.setUpIncomeLeftText(incomeLeft > 0)
        }
    }

    init {
        this.incomeView = WeakReference(incomeView)
    }
}