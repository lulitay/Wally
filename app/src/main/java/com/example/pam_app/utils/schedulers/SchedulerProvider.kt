package com.example.pam_app.utils.schedulers

import io.reactivex.Scheduler
import kotlin.jvm.Synchronized

interface SchedulerProvider {
    fun io(): Scheduler
    fun computation(): Scheduler
    fun ui(): Scheduler?
}