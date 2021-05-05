package com.example.pam_app.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    public String getAmountString() {
        return "$" + amount;
    }

    public String getComment() {
        return comment;
    }

    public String getDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(date);
    }

    public Date getDate() {
        return this.date;
    }
}
