package com.example.pam_app.model

import android.annotation.SuppressLint
import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Income : Serializable, Entry {
    val id: Int?
    override val comment: String?
    val amount: Double
    val incomeType: IncomeType
    val date: Date?

    constructor(title: String?, amount: Double, incomeType: IncomeType, date: Date?) {
        comment = title
        this.amount = amount
        this.incomeType = incomeType
        this.date = date
        id = null
    }

    constructor(id: Int, title: String?, amount: Double, incomeType: IncomeType, date: Date?) {
        comment = title
        this.amount = amount
        this.incomeType = incomeType
        this.date = date
        this.id = id
    }

    override val amountString: String
        get() = "$$amount"

    override val dateString: String
        @SuppressLint("SimpleDateFormat")
        get() {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd")
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            return dateFormat.format(date)
        }

}