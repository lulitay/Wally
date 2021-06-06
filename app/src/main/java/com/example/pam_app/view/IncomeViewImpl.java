package com.example.pam_app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.pam_app.R;

public class IncomeViewImpl extends LinearLayout implements IncomeView {

    public IncomeViewImpl(Context context) {
        this(context, null);
    }

    public IncomeViewImpl(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public IncomeViewImpl(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        inflate(context, R.layout.view_income, this);
        setOrientation(VERTICAL);
    }
}
