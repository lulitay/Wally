package com.example.pam_app.model

import kotlin.jvm.Synchronized

interface Entry {
    val comment: String?
    val amountString: String
    val dateString: String
}