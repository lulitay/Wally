package com.example.pam_app.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Income implements Serializable {
    public final Integer id;
    private final String title;
    private final double amount;
    private final IncomeType incomeType;
    private final Date date;

    public Income(final String title, final double amount, final IncomeType incomeType, final Date date) {
        this.title = title;
        this.amount = amount;
        this.incomeType = incomeType;
        this.date = date;
        this.id = null;
    }

    public Income(final int id, final String title, final double amount, final IncomeType incomeType, final Date date) {
        this.title = title;
        this.amount = amount;
        this.incomeType = incomeType;
        this.date = date;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public String getAmountString() {
        return "$" + amount;
    }

    public String getDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(date);
    }

    public IncomeType getIncomeType() {
        return incomeType;
    }

    public Date getDate() {
        return date;
    }
}
