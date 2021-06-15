package com.example.pam_app.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "entries", indices = {@Index(value = {"comment"})},
        foreignKeys = {
                @ForeignKey(
                        entity = BucketEntity.class,
                        parentColumns = "id",
                        childColumns = "idBucket",
                        onDelete = ForeignKey.CASCADE
                )
        })
@TypeConverters(DateConverter.class)
public class BucketEntryEntity {
    @PrimaryKey(autoGenerate = true)
    public int idEntry;
    public final int idBucket;
    public final double amount;
    public final Date date;
    @ColumnInfo(name = "comment")
    public final String comment;

    public BucketEntryEntity(int idBucket, double amount, Date date, String comment) {
        this.idBucket = idBucket;
        this.amount = amount;
        this.date = date;
        this.comment = comment;
    }
}
