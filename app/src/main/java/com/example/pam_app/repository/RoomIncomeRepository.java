package com.example.pam_app.repository;

import com.example.pam_app.db.IncomeDao;
import com.example.pam_app.db.IncomeEntity;
import com.example.pam_app.model.Income;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

import static io.reactivex.Single.just;

public class RoomIncomeRepository implements IncomeRepository {

    private final IncomeDao incomeDao;
    private final IncomeMapper incomeMapper;

    public RoomIncomeRepository(final IncomeDao incomeDao, final IncomeMapper incomeMapper) {
        this.incomeDao = incomeDao;
        this.incomeMapper = incomeMapper;
    }

    @Override
    public Flowable<List<Income>> getList() {
        return this.incomeDao.getList().map(incomeEntityList -> {
            final List<Income> incomes = new ArrayList<>();
            for (final IncomeEntity income: incomeEntityList) {
                incomes.add(incomeMapper.toModel(income));
            }
            return incomes;
        });
    }

    @Override
    public Single<Long> create(final Income income) {
        return just(incomeDao.create(incomeMapper.toEntity(income)));
    }

    @Override
    public void delete(final int id) {
        this.incomeDao.delete(id);
    }

    @Override
    public Flowable<Income> get(int id) {
        return this.incomeDao.get(id).map(incomeMapper::toModel);
    }
}
