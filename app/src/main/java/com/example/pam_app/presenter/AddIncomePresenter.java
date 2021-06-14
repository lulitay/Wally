package com.example.pam_app.presenter;

import com.example.pam_app.R;
import com.example.pam_app.model.Income;
import com.example.pam_app.model.IncomeType;
import com.example.pam_app.repository.IncomeRepository;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.AddIncomeView;

import java.lang.ref.WeakReference;
import java.util.Date;

import io.reactivex.disposables.Disposable;

import static com.example.pam_app.fragment.AddBucketEntryFragment.MAX_AMOUNT;
import static com.example.pam_app.fragment.AddBucketEntryFragment.MAX_CHARACTERS;

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
        final boolean fields = checkFields(description, amount, date);
        if (fields) {
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
    }

    private boolean checkFields(
            final String description,
            final String amount,
            final Date date
    ) {
        boolean isCorrect = true;
        if (description.length() == 0) {
            addIncomeView.get().showDescriptionError(R.string.error_empty, null);
            isCorrect = false;
        } else if (description.length() > MAX_CHARACTERS) {
            addIncomeView.get().showDescriptionError(R.string.max_characters, MAX_CHARACTERS);
            isCorrect = false;
        }
        if (amount.length() == 0) {
            addIncomeView.get().showAmountError(R.string.error_empty, null);
            isCorrect = false;
        } else if (Double.parseDouble(amount) >= MAX_AMOUNT) {
            addIncomeView.get().showAmountError(R.string.max_amount, MAX_AMOUNT);
            isCorrect = false;
        }
        if (date == null) {
            addIncomeView.get().showDateError(R.string.error_empty);
            isCorrect = false;
        } else if (date.getTime() > new Date().getTime()) {
            addIncomeView.get().showDateError(R.string.error_past_date);
            isCorrect = false;
        }
        return isCorrect;
    }

    public void onViewDetached() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
