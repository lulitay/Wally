package com.example.pam_app.repository;

import com.example.pam_app.model.Income;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface IncomeRepository {

    Flowable<List<Income>> getList();

    Single<Long> create(final Income income);

    void delete(final int id);

    Flowable<Income> get(final int id);
}
