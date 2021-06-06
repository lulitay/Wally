package com.example.pam_app.repository;

import com.example.pam_app.db.IncomeEntity;
import com.example.pam_app.model.Income;
import com.example.pam_app.model.IncomeType;

public class IncomeMapper {

    public Income toModel(final IncomeEntity incomeEntity) {
        return new Income(incomeEntity.getId(), incomeEntity.getTitle(), incomeEntity.getAmount(),
                IncomeType.values()[incomeEntity.getIncomeType()], incomeEntity.getDate());
    }

    public IncomeEntity toEntity(final Income income) {
        return new IncomeEntity(income.getTitle(), income.getAmount(),
                income.getIncomeType().ordinal(), income.getDate());
    }
}
