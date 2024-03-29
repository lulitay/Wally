package com.example.pam_app.model

import android.annotation.SuppressLint
import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class BucketEntry : Serializable, Entry {
    val amount: Double
    val date: Date?
    override val comment: String?
    val entryId: Int?
    val bucketId: Int?
    val bucketTitle: String?

    constructor(amount: Double, date: Date?, comment: String?, bucketId: Int?) {
        this.amount = amount
        this.date = date
        this.comment = comment
        entryId = null
        this.bucketId = bucketId
        bucketTitle = null
    }

    constructor(amount: Double, date: Date?, comment: String?, bucketTitle: String?, bucketId: Int?) {
        this.amount = amount
        this.date = date
        this.comment = comment
        entryId = null
        this.bucketId = bucketId
        this.bucketTitle = bucketTitle
    }

    constructor(amount: Double, date: Date?, comment: String?, id: Int, bucketId: Int?) {
        this.amount = amount
        this.date = date
        this.comment = comment
        this.entryId = id
        this.bucketId = bucketId
        bucketTitle = null
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