package com.example.pam_app.presenter;

import com.example.pam_app.R;
import com.example.pam_app.model.Income;
import com.example.pam_app.repository.IncomeRepository;
import com.example.pam_app.utils.SchedulerProviderTest;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.AddIncomeView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Date;

import io.reactivex.Single;

import static com.example.pam_app.fragment.AddBucketEntryFragment.MAX_AMOUNT;
import static com.example.pam_app.fragment.AddBucketEntryFragment.MAX_CHARACTERS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddIncomePresenterTest {

    @Mock
    private AddIncomeView addIncomeView;
    @Mock
    private IncomeRepository incomeRepository;

    private SchedulerProvider schedulerProvider;
    private AddIncomePresenter presenter;

    @BeforeEach
    public void setUp() {
        schedulerProvider = new SchedulerProviderTest();
        presenter = new AddIncomePresenter(addIncomeView, incomeRepository, schedulerProvider);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    public void givenErrorInFieldWhenSaveIncomeThenShowErrorWithoutParameter(final int variable) {
        final long now = new Date().getTime();
        switch (variable) {
            case 1:
                presenter.saveIncome("", "200", new Date(now - 1000));
                verify(addIncomeView, only()).showDescriptionError(R.string.error_empty, null);
                break;
            case 2:
                presenter.saveIncome("test", "200", null);
                verify(addIncomeView, only()).showDateError(R.string.error_empty);
                break;
            case 3:
                presenter.saveIncome("test", "200", new Date(now + 1000));
                verify(addIncomeView, only()).showDateError(R.string.error_future_date);
                break;
            case 4:
                presenter.saveIncome("test", "", new Date(now - 1000));
                verify(addIncomeView, only()).showAmountError(R.string.error_empty, null);
                break;
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 4})
    public void givenErrorInFieldWhenSaveIncomeThenShowErrorWithParameter(final int variable) {
        final long now = new Date().getTime();
        switch (variable) {
            case 1:
                presenter.saveIncome("qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnm", "200", new Date(now - 1000));
                verify(addIncomeView, only()).showDescriptionError(R.string.max_characters, MAX_CHARACTERS);
                break;
            case 4:
                presenter.saveIncome("test", String.valueOf(MAX_AMOUNT), new Date(now - 1000));
                verify(addIncomeView, only()).showAmountError(R.string.max_amount, MAX_AMOUNT);
                break;
        }
    }

    @Test
    public void givenNoErrorsWhenSaveBucketThenShowSuccessInView() {
        final long now = new Date().getTime();
        when(incomeRepository.create(any(Income.class))).thenReturn(Single.just(20L));

        presenter.saveIncome("test", "200", new Date(now - 1000));

        verify(addIncomeView, only()).onSuccessSavingIncome(any(Income.class));
    }

    @Test
    public void givenErrorsInGetBucketWhenSaveEntryThenShowErrorInView() {
        final long now = new Date().getTime();
        when(incomeRepository.create(any(Income.class))).thenReturn(Single.error(new IOException()));

        presenter.saveIncome("test", "200", new Date(now - 1000));

        verify(addIncomeView, only()).onErrorSavingIncome();
    }

    @Test
    public void onViewDetachedTest() {
        presenter.onViewDetached();
    }

}
