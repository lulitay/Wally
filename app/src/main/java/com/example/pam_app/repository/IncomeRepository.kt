package com.example.pam_app.repository

import com.example.pam_app.model.Income
import io.reactivex.Flowable
import io.reactivex.Single
import kotlin.jvm.Synchronized

interface IncomeRepository {
    val list: Flowable<List<Income>>
    fun create(income: Income): Single<Long?>
    fun delete(id: Int)
    operator fun get(id: Int): Flowable<Income?>
}