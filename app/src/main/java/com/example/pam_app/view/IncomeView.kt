package com.example.pam_app.view

import com.example.pam_app.model.Income
import kotlin.jvm.Synchronized

interface IncomeView {
    fun bind(incomeList: List<Income>?, incomeLeft: Double)
    fun onIncomeAdded(income: Income?)
    fun onBucketEntryAdded(amount: Double?)
    fun setUpIncomeLeftText(isPositive: Boolean)
}