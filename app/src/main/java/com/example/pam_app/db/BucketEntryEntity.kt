package com.example.pam_app.db

import androidx.room.*
import java.util.*
import kotlin.jvm.Synchronized

@Entity(tableName = "entries", indices = [Index(value = ["comment"])], foreignKeys = [ForeignKey(entity = BucketEntity::class, parentColumns = arrayOf("id"), childColumns = arrayOf("idBucket"), onDelete = ForeignKey.CASCADE)])
@TypeConverters(DateConverter::class)
class BucketEntryEntity(val idBucket: Int, val amount: Double, val date: Date?, @field:ColumnInfo(name = "comment") val comment: String?) {
    @kotlin.jvm.JvmField
    @PrimaryKey(autoGenerate = true)
    var idEntry = 0

}