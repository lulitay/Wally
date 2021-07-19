package com.example.pam_app.repository

import com.example.pam_app.db.IncomeEntity
import com.example.pam_app.model.Income
import com.example.pam_app.model.IncomeType
import kotlin.jvm.Synchronized

class IncomeMapper {
    fun toModel(incomeEntity: IncomeEntity?): Income {
        return Income(incomeEntity?.id!!, incomeEntity.title, incomeEntity.amount,
                IncomeType.values()[incomeEntity.incomeType], incomeEntity.date)
    }

    fun toEntity(income: Income): IncomeEntity {
        return IncomeEntity(income.comment, income.amount,
                income.incomeType.ordinal, income.date)
    }
}