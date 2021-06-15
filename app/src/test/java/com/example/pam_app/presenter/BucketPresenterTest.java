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
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

import static com.example.pam_app.model.BucketType.SAVING;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BucketPresenterTest {

    private static final BucketEntry ENTRY_1 = new BucketEntry(20, new Date(12), "test", "bucket1");
    private static final BucketEntry ENTRY_2 = new BucketEntry(30, new Date(10), "test2", "non_recurrent_bucket");
    private static final List<BucketEntry> ENTRIES = ImmutableList.of(ENTRY_1, ENTRY_2);
    private static final List<BucketEntry> ENTRIES_ORDERED = ImmutableList.of(ENTRY_2, ENTRY_1);
    private static final Flowable<Bucket> BUCKET_1 = Flowable.just(
            new Bucket("bucket1", new Date(), SAVING, 300, ENTRIES, 1, null,  false)
    );
    private static final Flowable<Bucket> RECURRENT_BUCKET = Flowable.just(
            new Bucket("recurrent_bucket", new Date(), SAVING, 300, new LinkedList<>(), 1, null,  true)
    );
    private static final Flowable<Bucket> NON_RECURRENT_BUCKET = Flowable.just(
            new Bucket("non_recurrent_bucket", new Date(), SAVING, 300, new LinkedList<>(), 1, null,  false)
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
        when(bucketRepository.get(1)).thenReturn(BUCKET_1);
        presenter.onViewAttach();

        final ArgumentCaptor<Bucket> bucketAC = ArgumentCaptor.forClass(Bucket.class);
        verify(bucketView, only()).bind(bucketAC.capture());
        assertEquals(ENTRIES_ORDERED, bucketAC.getValue().entries);
    }

    @Test
    public void givenBucketIdWhenOnViewAttachFailureThenShowError() {
        when(bucketRepository.get(1)).thenReturn(Flowable.error(new IOException()));
        presenter.onViewAttach();

        verify(bucketView, only()).showGetBucketError();
    }

    @Test
    public void givenBucketIdWhenDeleteSuccessBucketThenShowSuccess() {
        when(bucketRepository.get(1)).thenReturn(BUCKET_1);
        when(bucketRepository.delete(1)).thenReturn(Single.just(1));
        presenter.onViewAttach();
        presenter.onDelete();

        verify(bucketView).showDeleteBucketSuccess();
        verify(bucketView).back(any());
    }

    @Test
    public void givenBucketIdWhenDeleteSuccessBucketThenShowError() {
        when(bucketRepository.get(1)).thenReturn(BUCKET_1);
        when(bucketRepository.delete(1)).thenReturn(Single.error(new IOException()));
        presenter.onViewAttach();
        presenter.onDelete();

        verify(bucketView).showDeleteBucketError();
        verify(bucketView).back(any());
    }

    @Test
    public void whenDeleteSelectedThenShowSureDialog() {
        presenter.onDeleteSelected();

        verify(bucketView, only()).showSureDialog(any());
    }

    @Test
    public void whenEntryClickThenGoToAddEntry() {
        when(bucketRepository.get(1)).thenReturn(BUCKET_1);
        presenter.onViewAttach();
        presenter.onAddEntryClick();

        verify(bucketView).goToAddEntry(anyString(), anyInt());
    }

    @Test
    public void whenBackSelectedThenGoBackInView() {
        presenter.onBackSelected();

        verify(bucketView, only()).back(any());
    }

    @Test
    public void onViewDetachedTest() {
        when(bucketRepository.get(1)).thenReturn(BUCKET_1);
        presenter.onViewAttach();
        presenter.onViewDetached();
    }

    @Test
    public void givenNullEntryWhenAddEntryThenDoNothing() {
        presenter.onAddEntry(null);
        verify(bucketView, never()).bind(any());
    }

    @Test
    public void givenRecurrentAndOldEntryWhenAddEntryThenAddToOldEntries() {
        final Date oldDate = getOldDate();
        final BucketEntry oldEntry = new BucketEntry(20, oldDate, "test", "recurrent_bucket");

        when(bucketRepository.get(1)).thenReturn(RECURRENT_BUCKET);
        presenter.onViewAttach();
        presenter.onAddEntry(oldEntry);

        final ArgumentCaptor<Bucket> bucketAC = ArgumentCaptor.forClass(Bucket.class);
        verify(bucketView, atLeastOnce()).bind(any());
        verify(bucketView, atLeastOnce()).bind(bucketAC.capture());
        assertEquals(oldEntry, bucketAC.getValue().oldEntries.get(0));
    }

    @Test
    public void givenRecurrentAndNewEntryWhenAddEntryThenAddToNewEntries() {
        final Date newDate = getNewDate();
        final BucketEntry newEntry = new BucketEntry(20, newDate, "test", "recurrent_bucket");

        when(bucketRepository.get(1)).thenReturn(RECURRENT_BUCKET);
        presenter.onViewAttach();
        presenter.onAddEntry(newEntry);

        final ArgumentCaptor<Bucket> bucketAC = ArgumentCaptor.forClass(Bucket.class);
        verify(bucketView, atLeastOnce()).bind(any());
        verify(bucketView, atLeastOnce()).bind(bucketAC.capture());
        assertEquals(newEntry, bucketAC.getValue().entries.get(0));
    }

    @Test
    public void givenNonRecurrentEntryWhenAddEntryThenOnlyAddToEntries() {
        when(bucketRepository.get(1)).thenReturn(NON_RECURRENT_BUCKET);
        presenter.onViewAttach();
        presenter.onAddEntry(ENTRY_2);

        final ArgumentCaptor<Bucket> bucketAC = ArgumentCaptor.forClass(Bucket.class);
        verify(bucketView, atLeastOnce()).bind(any());
        verify(bucketView, atLeastOnce()).bind(bucketAC.capture());
        assertEquals(ENTRY_2, bucketAC.getValue().entries.get(0));
    }

    private Date getOldDate() {
        return new Date(getFirstDayOfMonth().getTime() - 100);
    }

    private Date getNewDate() {
        return new Date(getFirstDayOfMonth().getTime() + 100);
    }

    private Date getFirstDayOfMonth() {
        final Calendar today = getInstance();
        final Calendar next = getInstance();
        next.clear();
        next.set(YEAR, today.get(YEAR));
        next.set(MONTH, today.get(MONTH));
        next.set(DAY_OF_MONTH, 1);
        return next.getTime();
    }
}
