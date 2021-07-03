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
            date: Date?,
            isRecurrent: Boolean
    ) {
        val dateAux = if (isRecurrent) firstDayOfNextMonth else date
        val fields = checkFields(description, amount, dateAux, isRecurrent)
        if (fields) {
            val income = Income(description, amount.toDouble(), if(isRecurrent) IncomeType.MONTHLY else IncomeType.EXTRA, date)
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
            date: Date?,
            isRecurrent: Boolean
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
        if (!isRecurrent && date == null) {
            addIncomeView.get()!!.showDateError(R.string.error_empty)
            isCorrect = false
        } else if (!isRecurrent && date!!.time > Date().time) {
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

    fun onIsRecurrentSwitchChange(isRecurrent: Boolean) {
        if (addIncomeView.get() != null) {
            addIncomeView.get()!!.changeDatePickerState(!isRecurrent)
        }
    }

    private val firstDayOfNextMonth: Date
        get() {
            val today = Calendar.getInstance()
            val next = Calendar.getInstance()
            next.clear()
            next[Calendar.YEAR] = today[Calendar.YEAR]
            next[Calendar.MONTH] = today[Calendar.MONTH] + 1
            next[Calendar.DAY_OF_MONTH] = 1
            return next.time
        }
}