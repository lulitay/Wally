package com.example.pam_app.presenter;

import com.example.pam_app.model.Income;
import com.example.pam_app.model.IncomeType;
import com.example.pam_app.repository.IncomeRepository;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.AddIncomeView;

import java.lang.ref.WeakReference;
import java.util.Date;

import io.reactivex.disposables.Disposable;

public class AddIncomePresenter {

    private final WeakReference<AddIncomeView> addIncomeView;
    private final IncomeRepository incomeRepository;
    private final SchedulerProvider schedulerProvider;
    private Disposable disposable;

    public AddIncomePresenter(
            final AddIncomeView addIncomeView,
            final IncomeRepository incomeRepository,
            final SchedulerProvider schedulerProvider
    ) {
        this.addIncomeView = new WeakReference<>(addIncomeView);
        this.incomeRepository = incomeRepository;
        this.schedulerProvider = schedulerProvider;
    }

    public void saveIncome(
            final String description,
            final String amount,
            final Date date
    ) {
        final Income income = new Income(description, Double.parseDouble(amount), IncomeType.MONTHLY, date);
        disposable = incomeRepository.create(income)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe((Long id) -> {
                if (addIncomeView.get() != null) {
                    addIncomeView.get().onSuccessSavingIncome(income);
                }
            }, (throwable) -> {
                if (addIncomeView.get() != null) {
                    addIncomeView.get().onErrorSavingIncome();
                }
            });
    }

    public void onViewDetached() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
