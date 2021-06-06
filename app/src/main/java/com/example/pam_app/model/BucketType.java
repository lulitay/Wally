package com.example.pam_app.model;

import com.example.pam_app.R;

public enum BucketType {
    SPENDING(R.string.spending),
    SAVING(R.string.saving);

    private final int stringResource;

    BucketType(int stringResource) {
        this.stringResource = stringResource;
    }

    public int getStringResource(){
        return stringResource;
    }

    public static BucketType getBucketType(String string) {
        string = string.toUpperCase();
        if(string.equals("SPENDING") || string.equals("GASTO")) {
            return SPENDING;
        } else if(string.equals("SAVING") || string.equals("AHORRO")) {
            return SAVING;
        }
        return null; // TODO: improve this
    }
}
