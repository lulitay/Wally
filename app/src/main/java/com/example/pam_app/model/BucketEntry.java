package com.example.pam_app.model;

import java.util.Date;

public class BucketEntry {
    public final double amount;
    public final Date date;
    public final String comment;

    public BucketEntry(double amount, Date date, String comment) {
        this.amount = amount;
        this.date = date;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "" + amount;
    }
}
