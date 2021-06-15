package com.example.pam_app.presenter;

import com.example.pam_app.R;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.utils.SchedulerProviderTest;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.AddBucketEntryView;
import com.google.common.collect.ImmutableList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

import static com.example.pam_app.fragment.AddBucketEntryFragment.MAX_AMOUNT;
import static com.example.pam_app.fragment.AddBucketEntryFragment.MAX_CHARACTERS;
import static com.example.pam_app.model.BucketType.SAVING;
import static io.reactivex.Flowable.just;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddBucketEntryPresenterTest {

    private static final Flowable<List<String>> BUCKETS = just(ImmutableList.of("test", "test2"));
    private static final Single<Bucket> BUCKET = Single.just(
            new Bucket("test2", new Date(), SAVING, 300, null, 1, null,  false)
    );

    @Mock
    private AddBucketEntryView addBucketEntryView;
    @Mock
    private BucketRepository bucketRepository;

    private SchedulerProvider schedulerProvider;
    private AddBucketEntryPresenter presenter;

    @BeforeEach
    public void setUp() {
        schedulerProvider = new SchedulerProviderTest();
        presenter = new AddBucketEntryPresenter(addBucketEntryView, bucketRepository, schedulerProvider);
    }

    @Test
    public void givenBucketListWhenViewAttachedThenShowCorrectDropdown() {
        when(addBucketEntryView.getBucketType()).thenReturn(0);
        when(bucketRepository.getTitleListByType(0)).thenReturn(BUCKETS);

        presenter.onViewAttached();

        verify(addBucketEntryView).setDropDownOptions(BUCKETS.blockingFirst());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    public void givenErrorInFieldWhenSaveEntryThenShowErrorWithoutParameter(final int variable) {
        final long now = new Date().getTime();
        switch (variable) {
            case 1:
                presenter.saveBucketEntry("", new Date(now + 1000), "test", "bucket");
                verify(addBucketEntryView, only()).showAmountError(R.string.error_empty, null);
                break;
            case 2:
                presenter.saveBucketEntry("200", null, "test", "bucket");
                verify(addBucketEntryView, only()).showDateError(R.string.error_empty);
                break;
            case 3:
                presenter.saveBucketEntry("200", new Date(now + 1000), "", "bucket");
                verify(addBucketEntryView, only()).showDescriptionError(R.string.error_empty, null);
                break;
            case 4:
                presenter.saveBucketEntry("200", new Date(now + 1000), "test", "");
                verify(addBucketEntryView, only()).showBucketTitleError(R.string.error_empty);
                break;
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void givenErrorInFieldWhenSaveEntryThenShowErrorWithParameter(final int variable) {
        final long now = new Date().getTime();
        switch (variable) {
            case 1:
                presenter.saveBucketEntry(String.valueOf(MAX_AMOUNT), new Date(now + 1000), "test", "bucket");
                verify(addBucketEntryView, only()).showAmountError(R.string.max_amount, MAX_AMOUNT);
                break;
            case 2:
                presenter.saveBucketEntry("200", new Date(now - 1000), "test", "bucket");
                verify(addBucketEntryView, only()).showDateError(R.string.error_past_date);
                break;
            case 3:
                presenter.saveBucketEntry("200", new Date(now + 1000), "qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnm", "bucket");
                verify(addBucketEntryView, only()).showDescriptionError(R.string.max_characters, MAX_CHARACTERS);
                break;
        }
    }

    @Test
    public void givenNoErrorsWhenSaveEntryThenShowSuccessInView() {
        final long now = new Date().getTime();
        when(bucketRepository.get("bucket")).thenReturn(BUCKET);
        when(bucketRepository.addEntry(any(BucketEntry.class), anyInt())).thenReturn(Single.just(2L));

        presenter.saveBucketEntry("200", new Date(now + 1000), "test", "bucket");

        verify(addBucketEntryView, only()).onSuccessSavingBucketEntry(any(BucketEntry.class));
    }

    @Test
    public void givenErrorsInGetBucketWhenSaveEntryThenShowErrorInView() {
        final long now = new Date().getTime();
        when(bucketRepository.get("bucket")).thenReturn(Single.error(new IOException()));

        presenter.saveBucketEntry("200", new Date(now + 1000), "test", "bucket");

        verify(addBucketEntryView, only()).onErrorSavingBucketEntry();
    }

    @Test
    public void givenErrorsInSaveBucketWhenSaveEntryThenShowErrorInView() {
        final long now = new Date().getTime();
        when(bucketRepository.get("bucket")).thenReturn(BUCKET);
        when(bucketRepository.addEntry(any(BucketEntry.class), anyInt())).thenReturn(Single.error(new IOException()));

        presenter.saveBucketEntry("200", new Date(now + 1000), "test", "bucket");

        verify(addBucketEntryView, only()).onErrorSavingBucketEntry();
    }

}
