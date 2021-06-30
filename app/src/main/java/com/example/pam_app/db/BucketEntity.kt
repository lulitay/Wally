package com.example.pam_app.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "buckets", indices = [Index(value = ["title"], unique = true)])
@TypeConverters(DateConverter::class)
class BucketEntity(val title: String?, val dueDate: Date?, val bucketType: Int, val target: Double, val imagePath: String?,
                   val isRecurrent: Boolean, id: Int) {
    @kotlin.jvm.JvmField
    @PrimaryKey(autoGenerate = true)
    var id = 0

    init {
        this.id = id
    }
}