package com.example.pam_app.presenter;

import com.example.pam_app.view.IncomeView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class IncomePresenterTest {

    @Mock
    private IncomeView incomeView;

    private IncomePresenter presenter;

    @BeforeEach
    public void setUp() {
        presenter = new IncomePresenter(incomeView);
    }

    @Test
    public void givenPositiveIncomeLeftWhenIncomeLeftReceiveThenCallViewWithTrue() {
        presenter.onIncomeLeftAmountReceived(20.0);
        verify(incomeView, only()).setUpIncomeLeftText(true);
    }

    @Test
    public void givenNegativeIncomeLeftWhenIncomeLeftReceiveThenCallViewWithFalse() {
        presenter.onIncomeLeftAmountReceived(-20.0);
        verify(incomeView, only()).setUpIncomeLeftText(false);
    }
}
