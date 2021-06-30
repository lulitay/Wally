package com.example.pam_app.presenter

import com.example.pam_app.R
import com.example.pam_app.fragment.AddBucketEntryFragment
import com.example.pam_app.model.Income
import com.example.pam_app.model.IncomeType
import com.example.pam_app.repository.IncomeRepository
import com.example.pam_app.utils.schedulers.SchedulerProvider
import com.example.pam_app.view.AddIncomeView
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference
import java.util.*

class AddIncomePresenter(
        addIncomeView: AddIncomeView?,
        private val incomeRepository: IncomeRepository?,
        private val schedulerProvider: SchedulerProvider?
) {
    private val addIncomeView: WeakReference<AddIncomeView?> = WeakReference(addIncomeView)
    private var disposable: Disposable? = null
    fun saveIncome(
            description: String,
            amount: String,
            date: Date?
    ) {
        val fields = checkFields(description, amount, date)
        if (fields) {
            val income = Income(description, amount.toDouble(), IncomeType.MONTHLY, date)
            disposable = incomeRepository!!.create(income)
                    .subscribeOn(schedulerProvider!!.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe({
                        if (addIncomeView.get() != null) {
                            addIncomeView.get()!!.onSuccessSavingIncome(income)
                        }
                    }) {
                        if (addIncomeView.get() != null) {
                            addIncomeView.get()!!.onErrorSavingIncome()
                        }
                    }
        }
    }

    private fun checkFields(
            description: String,
            amount: String,
            date: Date?
    ): Boolean {
        var isCorrect = true
        if (description.isEmpty()) {
            addIncomeView.get()!!.showDescriptionError(R.string.error_empty, null)
            isCorrect = false
        } else if (description.length > AddBucketEntryFragment.MAX_CHARACTERS) {
            addIncomeView.get()!!.showDescriptionError(R.string.max_characters, AddBucketEntryFragment.MAX_CHARACTERS)
            isCorrect = false
        }
        when {
            amount.isEmpty() -> {
                addIncomeView.get()!!.showAmountError(R.string.error_empty, null)
                isCorrect = false
            }
            amount == "." -> {
                addIncomeView.get()!!.showAmountError(R.string.error_format, null)
                isCorrect = false
            }
            amount.toDouble() >= AddBucketEntryFragment.MAX_AMOUNT -> {
                addIncomeView.get()!!.showAmountError(R.string.max_amount, AddBucketEntryFragment.MAX_AMOUNT)
                isCorrect = false
            }
        }
        if (date == null) {
            addIncomeView.get()!!.showDateError(R.string.error_empty)
            isCorrect = false
        } else if (date.time > Date().time) {
            addIncomeView.get()!!.showDateError(R.string.error_future_date)
            isCorrect = false
        }
        return isCorrect
    }

    fun onViewDetached() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

}