package com.example.pam_app.presenter;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.utils.SchedulerProviderTest;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.BucketView;
import com.google.common.collect.ImmutableList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

import static com.example.pam_app.model.BucketType.SAVING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BucketPresenterTest {

    private static final BucketEntry ENTRY_1 = new BucketEntry(20, new Date(12), "test");
    private static final BucketEntry ENTRY_2 = new BucketEntry(30, new Date(10), "test2");
    private static final List<BucketEntry> ENTRIES = ImmutableList.of(ENTRY_1, ENTRY_2);
    private static final List<BucketEntry> ENTRIES_ORDERED = ImmutableList.of(ENTRY_2, ENTRY_1);
    private static final Flowable<Bucket> BUCKET = Flowable.just(
            new Bucket("test2", new Date(), SAVING, 300, ENTRIES, 1, null,  false)
    );

    @Mock
    private BucketView bucketView;
    @Mock
    private BucketRepository bucketRepository;

    private SchedulerProvider schedulerProvider;
    private BucketPresenter presenter;

    @BeforeEach
    public void setUp() {
        schedulerProvider = new SchedulerProviderTest();
        presenter = new BucketPresenter(1, bucketView, bucketRepository, schedulerProvider);
    }

    @Test
    public void givenBucketIdWhenViewResumeSuccessThenBindEntriesOrderedToView() {
        when(bucketRepository.get(1)).thenReturn(BUCKET);
        presenter.onViewAttach();
        presenter.onViewResume();

        final ArgumentCaptor<Bucket> bucketAC = ArgumentCaptor.forClass(Bucket.class);
        verify(bucketView, only()).bind(bucketAC.capture());
        assertEquals(ENTRIES_ORDERED, bucketAC.getValue().entries);
    }

    @Test
    public void givenBucketIdWhenViewResumeFailureThenShowError() {
        when(bucketRepository.get(1)).thenReturn(Flowable.error(new IOException()));
        presenter.onViewAttach();
        presenter.onViewResume();

        verify(bucketView, only()).showGetBucketError();
    }

    @Test
    public void givenBucketIdWhenDeleteSuccessBucketThenShowSuccess() {
        when(bucketRepository.delete(1)).thenReturn(Single.just(1));
        presenter.onViewAttach();
        presenter.onDelete();

        verify(bucketView).showDeleteBucketSuccess();
        verify(bucketView).back();
    }

    @Test
    public void givenBucketIdWhenDeleteSuccessBucketThenShowError() {
        when(bucketRepository.delete(1)).thenReturn(Single.error(new IOException()));
        presenter.onViewAttach();
        presenter.onDelete();

        verify(bucketView).showDeleteBucketError();
        verify(bucketView).back();
    }

    @Test
    public void whenDeleteSelectedThenShowSureDialog() {
        presenter.onDeleteSelected();

        verify(bucketView, only()).showSureDialog(any());
    }

    @Test
    public void whenEntryClickThenGoToAddEntry() {
        when(bucketRepository.get(1)).thenReturn(BUCKET);
        presenter.onViewAttach();
        presenter.onViewResume();
        presenter.onAddEntryClick();

        verify(bucketView).goToAddEntry(any());
    }

    @Test
    public void whenBackSelectedThenGoBackInView() {
        presenter.onBackSelected();

        verify(bucketView, only()).back();
    }

    @Test
    public void onViewPauseTest() {
        presenter.onViewAttach();
        presenter.onViewPause();
    }

    @Test
    public void onViewDetachedTest() {
        presenter.onViewAttach();
        presenter.onViewDetached();
    }
}
