package com.example.pam_app.utils;

import com.example.pam_app.utils.schedulers.SchedulerProvider;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class SchedulerProviderTest implements SchedulerProvider {
    @Override
    public Scheduler io() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler computation() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler ui() {
        return Schedulers.trampoline();
    }
}
