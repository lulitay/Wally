package com.example.pam_app.model;

import java.util.Date;
import java.util.List;

public class Bucket {
    public final String title;
    public final Date dueDate;
    public final BucketType bucketType;
    public final double target;
    public final List<BucketEntry> entries;


    public Bucket(String title, Date dueDate, BucketType bucketType, double target,
                  List<BucketEntry> entries) {
        this.title = title;
        this.dueDate = dueDate;
        this.bucketType = bucketType;
        this.target = target;
        this.entries = entries;
    }
}
