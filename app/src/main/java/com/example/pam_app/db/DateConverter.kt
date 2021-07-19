package com.example.pam_app.db

import androidx.room.TypeConverter
import java.util.*
import kotlin.jvm.Synchronized

object DateConverter {
    @kotlin.jvm.JvmStatic
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @kotlin.jvm.JvmStatic
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}