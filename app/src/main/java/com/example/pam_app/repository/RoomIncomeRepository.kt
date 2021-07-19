package com.example.pam_app.repository

import com.example.pam_app.db.IncomeDao
import com.example.pam_app.db.IncomeEntity
import com.example.pam_app.model.Income
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

class RoomIncomeRepository(private val incomeDao: IncomeDao?, private val incomeMapper: IncomeMapper?) : IncomeRepository {
    override val list: Flowable<List<Income>>
        get() = incomeDao?.list?.map { incomeEntityList: List<IncomeEntity?>? ->
            val incomes: MutableList<Income> = ArrayList()
            for (income in incomeEntityList!!) {
                incomes.add(incomeMapper!!.toModel(income))
            }
            incomes
        }!!

    override fun create(income: Income): Single<Long?> {
        return Single.fromCallable { incomeDao!!.create(incomeMapper!!.toEntity(income)) }
    }

    override fun delete(id: Int) {
        incomeDao!!.delete(id)
    }

    override fun get(id: Int): Flowable<Income?> {
        return incomeDao!![id].map { incomeEntity: IncomeEntity? -> incomeMapper!!.toModel(incomeEntity) }
    }

    override fun getList(type: Int, date: Date?): Flowable<List<Income?>> {
        return incomeDao!!.getList(type, date).map { incomeEntityList: List<IncomeEntity?>? ->
            val incomes: MutableList<Income> = ArrayList()
            for (income in incomeEntityList!!) {
                incomes.add(incomeMapper!!.toModel(income))
            }
            incomes
        }
    }
}