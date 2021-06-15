package com.example.pam_app.presenter;

import com.example.pam_app.R;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.utils.SchedulerProviderTest;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.AddBucketView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.Single;

import static com.example.pam_app.fragment.AddBucketEntryFragment.MAX_AMOUNT;
import static com.example.pam_app.fragment.AddBucketEntryFragment.MAX_CHARACTERS;
import static com.example.pam_app.model.BucketType.SPENDING;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddBucketPresenterTest {

    @Mock
    private AddBucketView addBucketView;
    @Mock
    private BucketRepository bucketRepository;

    private SchedulerProvider schedulerProvider;
    private AddBucketPresenter presenter;

    @BeforeEach
    public void setUp() {
        schedulerProvider = new SchedulerProviderTest();
        presenter = new AddBucketPresenter(addBucketView, bucketRepository, schedulerProvider);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    public void givenErrorInFieldWhenSaveBucketThenShowErrorWithoutParameter(final int variable) {
        final long now = new Date().getTime();
        switch (variable) {
            case 1:
                presenter.saveBucket("", new Date(now + 1000), SPENDING, "200", null, false);
                verify(addBucketView, only()).showTitleError(R.string.error_empty, null);
                break;
            case 2:
                presenter.saveBucket("test", null, SPENDING, "200", null, false);
                verify(addBucketView, only()).showDateError(R.string.error_empty);
                break;
            case 3:
                presenter.saveBucket("test", new Date(now - 1000), SPENDING, "200", null, false);
                verify(addBucketView, only()).showDateError(R.string.error_past_date);
                break;
            case 4:
                presenter.saveBucket("test", new Date(now + 1000), null, "200", null, false);
                verify(addBucketView, only()).showBucketTypeError(R.string.error_empty);
                break;
            case 5:
                presenter.saveBucket("test", new Date(now + 1000), SPENDING, "", null, false);
                verify(addBucketView, only()).showTargetError(R.string.error_empty, null);
                break;
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5})
    public void givenErrorInFieldWhenSaveBucketThenShowErrorWithParameter(final int variable) {
        final long now = new Date().getTime();
        switch (variable) {
            case 1:
                presenter.saveBucket("qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnm", new Date(now + 1000), SPENDING, "200", null, false);
                verify(addBucketView, only()).showTitleError(R.string.max_characters, MAX_CHARACTERS);
                break;
            case 5:
                presenter.saveBucket("test", new Date(now + 1000), SPENDING, String.valueOf(MAX_AMOUNT), null, false);
                verify(addBucketView, only()).showTargetError(R.string.max_amount, MAX_AMOUNT);
                break;
        }
    }

    @Test
    public void givenRecurrentBucketWhenSaveBucketThenPickCorrectDate() {
        final long now = new Date().getTime();
        final Calendar today = getInstance();
        final Calendar next = getInstance();
        next.clear();
        next.set(YEAR, today.get(YEAR));
        next.set(MONTH, today.get(MONTH) + 1);
        next.set(DAY_OF_MONTH, 1);
        when(bucketRepository.create(any(Bucket.class))).thenReturn(Single.just(20L));

        presenter.saveBucket("test", new Date(now + 1000), SPENDING, "200", null, true);

        final ArgumentCaptor<Bucket> bucketAC = ArgumentCaptor.forClass(Bucket.class);
        verify(bucketRepository, only()).create(bucketAC.capture());
        assertEquals(next.getTime(), bucketAC.getValue().dueDate);
    }

    @Test
    public void givenNoErrorsWhenSaveBucketThenShowSuccessInView() {
        final long now = new Date().getTime();
        when(bucketRepository.create(any(Bucket.class))).thenReturn(Single.just(20L));

        presenter.saveBucket("test", new Date(now + 1000), SPENDING, "200", null, false);

        verify(addBucketView, only()).onSuccessSavingBucket(any(Bucket.class));
    }

    @Test
    public void givenErrorsInGetBucketWhenSaveEntryThenShowErrorInView() {
        final long now = new Date().getTime();
        when(bucketRepository.create(any(Bucket.class))).thenReturn(Single.error(new IOException()));

        presenter.saveBucket("test", new Date(now + 1000), SPENDING, "200", null, false);

        verify(addBucketView, only()).onErrorSavingBucket();
    }

    @Test
    public void whenClickLoadImageThenRequestStoragePermission() {
        presenter.onClickLoadImage();

        verify(addBucketView, only()).requestStoragePermission();
    }

    @Test
    public void whenIsRecurrentSwitchChangeThenChangeDatePickerState() {
        presenter.onIsRecurrentSwitchChange(true);

        verify(addBucketView, only()).changeDatePickerState(false);
    }

    @Test
    public void onViewDetachedTest() {
        presenter.onViewDetached();
    }
}
