package com.example.pam_app.model;

import java.util.Date;

public class Income {
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

    public IncomeType getIncomeType() {
        return incomeType;
    }

    public Date getDate() {
        return date;
    }
}
