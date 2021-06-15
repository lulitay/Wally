package com.example.pam_app.utils;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class DecimalPercentageFormatter extends ValueFormatter {

    protected DecimalFormat mFormat;

    public DecimalPercentageFormatter(DecimalFormat format) {
        this.mFormat = format;
    }

    @Override
    public String getFormattedValue(float value) {
        if(value < 3) return "";
        return mFormat.format(value) + " %";
    }
}
