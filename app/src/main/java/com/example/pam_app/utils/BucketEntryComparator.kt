package com.example.pam_app.utils

import com.example.pam_app.model.BucketEntry
import java.util.*
import kotlin.jvm.Synchronized

class BucketEntryComparator : Comparator<BucketEntry> {
    override fun compare(o1: BucketEntry, o2: BucketEntry): Int {
        return o1.date?.compareTo(o2.date)!!
    }
}