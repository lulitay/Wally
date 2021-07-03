package com.example.pam_app.utils.workers

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.example.pam_app.model.Income
import com.example.pam_app.model.IncomeType
import com.example.pam_app.repository.IncomeRepository
import io.reactivex.Single
import io.reactivex.SingleEmitter
import java.util.*

class RecurrentIncomeWorker(context: Context, params: WorkerParameters,
                            private val incomeRepository: IncomeRepository) : RxWorker(context, params) {
    override fun createWork(): Single<Result> {
        val today = Calendar.getInstance()
        if (today[Calendar.DAY_OF_MONTH] != 1) {
            return Single.create { emitter: SingleEmitter<Result> -> emitter.onSuccess(Result.success()) }
        }
        val now = today.time
        return incomeRepository.getList(IncomeType.MONTHLY.ordinal, now)
                .firstOrError()
                .doOnSuccess { incomes: List<Income?>? ->
                    for(i in incomes!!) {
                        incomeRepository.create(Income(i!!.comment, i.amount, IncomeType.EXTRA, firstDayOfNextMonth)).blockingGet()
                    }
                }
                .map { Result.success() }
                .onErrorReturn { Result.failure() }
    }

    private val firstDayOfNextMonth: Date
        get() {
            val today = Calendar.getInstance()
            val next = Calendar.getInstance()
            next.clear()
            next[Calendar.YEAR] = today[Calendar.YEAR]
            next[Calendar.MONTH] = today[Calendar.MONTH] + 1
            next[Calendar.DAY_OF_MONTH] = 1
            next[Calendar.HOUR] = 0
            return next.time
        }
}