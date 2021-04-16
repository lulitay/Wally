package com.example.pam_app.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "buckets")
@TypeConverters(DateConverter.class)
public class BucketEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public Date dueDate;
    public int bucketType;
    public double target;

    public BucketEntity(String title, Date dueDate, int bucketType, double target) {
        this.title = title;
        this.dueDate = dueDate;
        this.bucketType = bucketType;
        this.target = target;
    }
}
