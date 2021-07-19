package com.example.pam_app.model

import kotlin.jvm.Synchronized

enum class IncomeType(private val data: String) {
    MONTHLY("Monthly"), EXTRA("Extra");

    override fun toString(): String {
        return data
    }

}