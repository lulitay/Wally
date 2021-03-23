package com.example.pam_app.model;

import java.util.Date;

public class BucketEntry {
    private double amount;
    private Date date;
    private String comment;

    public BucketEntry(double amount, Date date, String comment) {
        this.amount = amount;
        this.date = date;
        this.comment = comment;
    }
}
