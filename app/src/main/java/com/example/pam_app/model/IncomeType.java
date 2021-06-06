package com.example.pam_app.model;

public enum IncomeType {
    MONTHLY("Monthly"),
    EXTRA("Extra");

    private final String name;

    IncomeType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
