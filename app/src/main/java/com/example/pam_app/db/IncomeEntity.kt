package com.example.pam_app.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*
import kotlin.jvm.Synchronized

@Entity(tableName = "income")
@TypeConverters(DateConverter::class)
class IncomeEntity(val title: String?, val amount: Double, val incomeType: Int, val date: Date?) {
    @kotlin.jvm.JvmField
    @PrimaryKey(autoGenerate = true)
    var id = 0

}