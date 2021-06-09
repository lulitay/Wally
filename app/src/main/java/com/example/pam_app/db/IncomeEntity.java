package com.example.pam_app.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "income")
@TypeConverters(DateConverter.class)
public class IncomeEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private final String title;
    private final double amount;
    private final int incomeType;
    public final Date date;

    public IncomeEntity(final String title, final double amount, final int incomeType, final Date date) {
        this.title = title;
        this.amount = amount;
        this.incomeType = incomeType;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public int getIncomeType() {
        return incomeType;
    }

    public Date getDate() {
        return date;
    }
}
