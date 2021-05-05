package com.example.pam_app.model;

public enum BucketType {
    SPENDING("Spending"),
    SAVING("Saving");
    // INCOME("Income");

    private final String name;

    BucketType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
