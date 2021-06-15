package com.example.pam_app.presenter;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.view.BucketListView;
import com.google.common.collect.ImmutableList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.pam_app.model.BucketType.SAVING;
import static com.example.pam_app.model.BucketType.SPENDING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BucketListPresenterTest {

    private static final Bucket BUCKET_1 = new Bucket("test", new Date(), SPENDING, 200, false);
    private static final Bucket BUCKET_2 = new Bucket("test2", new Date(), SAVING, 300, false);
    private static final List<Bucket> BUCKETS = ImmutableList.of(BUCKET_1, BUCKET_2);

    @Captor
    private ArgumentCaptor<ArrayList<Bucket>> savingsAC;
    @Captor
    private ArgumentCaptor<ArrayList<Bucket>> spendingAC;
    @Mock
    private BucketListView bucketListView;

    private BucketListPresenter presenter;

    @BeforeEach
    public void setUp() {
        presenter = new BucketListPresenter(bucketListView);
    }

    @Test
    public void givenViewAttachedWhenBucketsReceivedThenShowInfoCorrectly() {
        presenter.onBucketsReceived(BUCKETS);

        verify(bucketListView).bindSavingsBuckets(savingsAC.capture());
        assertEquals(BUCKET_2, savingsAC.getValue().get(0));
        verify(bucketListView).bindSpendingBuckets(spendingAC.capture());
        assertEquals(BUCKET_1, spendingAC.getValue().get(0));
        verify(bucketListView).setIsSavingsListEmpty(false);
        verify(bucketListView).setIsSpendingListEmpty(false);
        verify(bucketListView).drawSavingsBucketList();
        verify(bucketListView).drawSavingsBucketList();
    }

}
