package com.example.pam_app.utils.schedulers;

import io.reactivex.Scheduler;

public interface SchedulerProvider {
    Scheduler io();

    Scheduler computation();

    Scheduler ui();
}
