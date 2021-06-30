package com.example.pam_app.model

import com.example.pam_app.R
import java.util.*

enum class BucketType(val stringResource: Int) {
    SPENDING(R.string.spending), SAVING(R.string.saving);

    companion object {
        fun getBucketType(string: String): BucketType? {
            val stringResource = string.toUpperCase(Locale.getDefault())
            if (stringResource == "SPENDING" || stringResource == "GASTO") {
                return SPENDING
            } else if (stringResource == "SAVING" || stringResource == "AHORRO") {
                return SAVING
            }
            return null // TODO: improve this
        }
    }

}