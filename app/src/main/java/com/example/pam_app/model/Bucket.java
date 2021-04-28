package com.example.pam_app.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.sqrt;

public class Bucket {
    public final String title;
    public final Date dueDate;
    public final BucketType bucketType;
    public final double target;
    public final List<BucketEntry> entries;
    public final Integer id;

    public Bucket(String title, Date dueDate, BucketType bucketType, double target,
                  List<BucketEntry> entries) {
        this.title = title;
        this.dueDate = dueDate;
        this.bucketType = bucketType;
        this.target = target;
        this.entries = entries;
        this.id = null;
    }

    public Bucket(String title, Date dueDate, BucketType bucketType, double target,
                  List<BucketEntry> entries, int id) {
        this.title = title;
        this.dueDate = dueDate;
        this.bucketType = bucketType;
        this.target = target;
        this.entries = entries;
        this.id = id;
    }

    public Bucket(String title, Date dueDate, BucketType bucketType, double target) {
        this.title = title;
        this.dueDate = dueDate;
        this.bucketType = bucketType;
        this.target = target;
        this.entries = new LinkedList<>();
        this.id = null;
    }

    public int getProgress() {
        double total = this.getTotal();

        return (int) sqrt((target / total) * (target / total));
    }

    public double getTotal() {
        double total = 0;
        for (BucketEntry entry: this.entries) {
            total += entry.amount;
        }
        return total;
    }

    public String getTotalString() {
        return "" + this.getTotal() + " / " + this.target;
    }

    public String getRemainingTime() {
        return "300D 11Hs"; //TODO check how we are going to handle times
    }
}
