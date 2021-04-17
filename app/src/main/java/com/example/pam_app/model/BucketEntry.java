package com.example.pam_app.model;

import androidx.annotation.NonNull;

import java.util.Date;

public class BucketEntry {
    public final double amount;
    public final Date date;
    public final String comment;
    public final Integer id;

    public BucketEntry(double amount, Date date, String comment) {
        this.amount = amount;
        this.date = date;
        this.comment = comment;
        this.id = null;
    }

    public BucketEntry(double amount, Date date, String comment, int id) {
        this.amount = amount;
        this.date = date;
        this.comment = comment;
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return "" + amount;
    }
}
