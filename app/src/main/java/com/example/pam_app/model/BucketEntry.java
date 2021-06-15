package com.example.pam_app.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BucketEntry implements Serializable, Entry {
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

    @Override
    public String getAmountString() {
        return "$" + amount;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public String getDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(date);
    }

    public Date getDate() {
        return this.date;
    }

    public Double getAmount() {
        return amount;
    }
}
