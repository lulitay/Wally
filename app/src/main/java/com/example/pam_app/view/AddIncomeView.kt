package com.example.pam_app.view

import com.example.pam_app.model.Income
import kotlin.jvm.Synchronized

interface AddIncomeView {
    fun onErrorSavingIncome()
    fun onSuccessSavingIncome(income: Income)
    fun showDescriptionError(error: Int, parameter: Int?)
    fun showAmountError(error: Int, parameter: Int?)
    fun showDateError(error: Int)
}