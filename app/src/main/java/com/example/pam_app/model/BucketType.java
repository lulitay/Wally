package com.example.pam_app.model;

import com.example.pam_app.R;

public enum BucketType {
    SPENDING(R.string.spending),
    SAVING(R.string.saving);
    // INCOME("Income", R.string.income);

    private final int stringResource;

    BucketType(int stringResource) {
        this.stringResource = stringResource;
    }

    public int getStringResource(){
        return stringResource;
    }
}
