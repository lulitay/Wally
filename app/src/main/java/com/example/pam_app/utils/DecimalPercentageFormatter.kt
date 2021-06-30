package com.example.pam_app.utils

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat
import kotlin.jvm.Synchronized

class DecimalPercentageFormatter(protected var mFormat: DecimalFormat) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return if (value < 3) "" else mFormat.format(value.toDouble()) + " %"
    }

}