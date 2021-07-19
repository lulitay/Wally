package com.example.pam_app.model

import java.io.Serializable
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.jvm.Synchronized

class Bucket : Serializable {
    @kotlin.jvm.JvmField
    val title: String?
    @kotlin.jvm.JvmField
    val dueDate: Date?
    @kotlin.jvm.JvmField
    val bucketType: BucketType
    val target: Double
    @kotlin.jvm.JvmField
    val entries: MutableList<BucketEntry?>
    @kotlin.jvm.JvmField
    val oldEntries: MutableList<BucketEntry?>
    val id: Int?
    val imagePath: String?
    @kotlin.jvm.JvmField
    val isRecurrent: Boolean

    constructor(title: String?, dueDate: Date?, bucketType: BucketType, target: Double,
                entries: List<BucketEntry?>?, isRecurrent: Boolean) {
        this.title = title
        this.dueDate = dueDate
        this.bucketType = bucketType
        this.target = target
        this.entries = LinkedList()
        oldEntries = LinkedList()
        id = null
        imagePath = null
        this.isRecurrent = isRecurrent
        initEntries(entries)
    }

    constructor(title: String?, dueDate: Date?, bucketType: BucketType, target: Double,
                entries: List<BucketEntry?>?, id: Int?, imagePath: String?, isRecurrent: Boolean) {
        this.title = title
        this.dueDate = dueDate
        this.bucketType = bucketType
        this.target = target
        this.entries = LinkedList()
        oldEntries = LinkedList()
        this.id = id
        this.imagePath = imagePath
        this.isRecurrent = isRecurrent
        initEntries(entries)
    }

    constructor(title: String?, dueDate: Date?, bucketType: BucketType, target: Double, isRecurrent: Boolean) {
        this.title = title
        this.dueDate = dueDate
        this.bucketType = bucketType
        this.target = target
        entries = LinkedList()
        oldEntries = LinkedList()
        id = null
        imagePath = null
        this.isRecurrent = isRecurrent
    }

    constructor(title: String?, dueDate: Date?, bucketType: BucketType, target: Double, imagePath: String?, isRecurrent: Boolean) {
        this.title = title
        this.dueDate = dueDate
        this.bucketType = bucketType
        this.target = target
        entries = LinkedList()
        oldEntries = LinkedList()
        id = null
        this.imagePath = imagePath
        this.isRecurrent = isRecurrent
    }

    val progress: Int
        get() {
            val total = total
            return if (total == 0.0) {
                0
            } else (total / target * 100).toInt()
        }

    val total: Double
        get() {
            var total = 0.0
            for (entry in entries) {
                total += entry!!.amount
            }
            return total
        }

    val totalString: String
        get() = "" + total + " / " + target

    val remainingTime: String
        get() {
            val timeNow = Date()
            if (dueDate!!.time > timeNow.time) {
                val diff = computeDiff(Date(), dueDate)
                return "" + diff[TimeUnit.DAYS] + "D " + diff[TimeUnit.HOURS] + "Hs"
            }
            return "0D 0Hs"
        }

    fun setDueDate(newDueDate: Date?): Bucket {
        return Bucket(title, newDueDate, bucketType, target,
                entries, id, imagePath, isRecurrent)
    }

    private fun computeDiff(date1: Date, date2: Date?): Map<TimeUnit?, Long> {
        val diffInMillis = date2!!.time - date1.time
        val units: List<TimeUnit?> = ArrayList(EnumSet.allOf(TimeUnit::class.java))
        Collections.reverse(units)
        val result: MutableMap<TimeUnit?, Long> = LinkedHashMap()
        var milliesRest = diffInMillis
        for (unit in units) {
            val diff = unit!!.convert(milliesRest, TimeUnit.MILLISECONDS)
            val diffInMilliesForUnit = unit.toMillis(diff)
            milliesRest = milliesRest - diffInMilliesForUnit
            result[unit] = diff
        }
        return result
    }

    private fun initEntries(entries: List<BucketEntry?>?) {
        if (entries != null) {
            val currentDate = Calendar.getInstance()
            currentDate[Calendar.HOUR_OF_DAY] = 0
            currentDate[Calendar.MINUTE] = 0
            currentDate[Calendar.SECOND] = 0
            currentDate[Calendar.DAY_OF_MONTH] = 1
            val firstDayOfMonth = currentDate.time
            for (e in entries) {
                if (isRecurrent && e!!.date!!.before(firstDayOfMonth)) {
                    oldEntries.add(e)
                } else {
                    this.entries.add(e)
                }
            }
        }
    }
}