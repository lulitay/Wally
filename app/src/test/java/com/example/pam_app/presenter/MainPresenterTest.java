package com.example.pam_app.presenter;

import android.content.SharedPreferences;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.model.Income;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.IncomeRepository;
import com.example.pam_app.repository.LanguagesRepository;
import com.example.pam_app.repository.LanguagesRepositoryImpl;
import com.example.pam_app.utils.SchedulerProviderTest;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.MainView;
import com.github.ivanshafran.sharedpreferencesmock.SPMockBuilder;
import com.google.common.collect.ImmutableList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Flowable;

import static com.example.pam_app.model.BucketType.SAVING;
import static com.example.pam_app.model.BucketType.SPENDING;
import static com.example.pam_app.model.IncomeType.EXTRA;
import static com.example.pam_app.model.IncomeType.MONTHLY;
import static io.reactivex.Flowable.just;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MainPresenterTest {

    private static final Bucket BUCKET_1 = new Bucket("test", new Date(), SPENDING, 200, false);
    private static final Bucket BUCKET_2 = new Bucket("test2", new Date(), SAVING, 300, false);
    private static final Flowable<List<Bucket>> BUCKETS = just(ImmutableList.of(BUCKET_1, BUCKET_2));

    private static final BucketEntry ENTRY_1 = new BucketEntry(20, new Date(), "test", 1);
    private static final BucketEntry ENTRY_2 = new BucketEntry(30, new Date(), "test2", 2);
    private static final Flowable<List<BucketEntry>> ENTRIES = just(ImmutableList.of(ENTRY_1, ENTRY_2));

    private static final Income INCOME_1 = new Income("test", 200, MONTHLY, new Date());
    private static final Income INCOME_2 = new Income("test2", 300, EXTRA, new Date());
    private static final Flowable<List<Income>> INCOMES = just(ImmutableList.of(INCOME_1, INCOME_2));

    private static final double TOTAL_INCOME = 500;
    private static final double TOTAL_SPENT = 50;

    @Mock
    private BucketRepository bucketRepository;
    @Mock
    private IncomeRepository incomeRepository;
    @Mock
    private MainView mainActivity;

    private SharedPreferences sharedPreferences;
    private LanguagesRepository languagesRepository;
    private SchedulerProvider schedulerProvider;
    private MainPresenter presenter;

    @BeforeEach
    public void setUp() {
        sharedPreferences = new SPMockBuilder().createSharedPreferences();
        languagesRepository = new LanguagesRepositoryImpl(sharedPreferences);
        schedulerProvider = new SchedulerProviderTest();
        presenter = new MainPresenter(bucketRepository, incomeRepository, mainActivity,
                schedulerProvider, languagesRepository
        );
    }

    @Test
    public void givenViewAttachedWhenDataReceivedThenBindViews() {
        when(bucketRepository.getList()).thenReturn(BUCKETS);
        when(bucketRepository.getEntryList()).thenReturn(ENTRIES);
        when(incomeRepository.getList()).thenReturn(INCOMES);

        presenter.onViewAttached();

        verify(mainActivity).onBucketListViewReceived(BUCKETS.blockingFirst());
        verify(mainActivity).onEntriesReceived(ENTRIES.blockingFirst());
        verify(mainActivity).onIncomeDataReceived(INCOMES.blockingFirst(), TOTAL_INCOME - TOTAL_SPENT);
    }

    @Test
    public void givenViewAttachedWhenChangeLanguageToEnglishThenUpdateLocaleInView() {
        when(bucketRepository.getList()).thenReturn(BUCKETS);
        when(bucketRepository.getEntryList()).thenReturn(ENTRIES);
        when(incomeRepository.getList()).thenReturn(INCOMES);

        presenter.onViewAttached();
        languagesRepository.changeLanguage("en");

        final ArgumentCaptor<Locale> localeAC = ArgumentCaptor.forClass(Locale.class);
        verify(mainActivity).updateLocale(localeAC.capture());
        assertEquals("en", localeAC.getValue().getLanguage());
    }

    @Test
    public void givenViewAttachedWhenChangeLanguageToSpanishThenUpdateLocaleInView() {
        when(bucketRepository.getList()).thenReturn(BUCKETS);
        when(bucketRepository.getEntryList()).thenReturn(ENTRIES);
        when(incomeRepository.getList()).thenReturn(INCOMES);

        presenter.onViewAttached();
        languagesRepository.changeLanguage("es");

        final ArgumentCaptor<Locale> localeAC = ArgumentCaptor.forClass(Locale.class);
        verify(mainActivity).updateLocale(localeAC.capture());
        assertEquals("es", localeAC.getValue().getLanguage());
    }

    @Test
    public void onViewDetachedTest() {
        when(bucketRepository.getList()).thenReturn(BUCKETS);
        when(bucketRepository.getEntryList()).thenReturn(ENTRIES);
        when(incomeRepository.getList()).thenReturn(INCOMES);
        presenter.onViewAttached();
        presenter.onViewDetached();
    }

    @Test
    public void whenGetCurrentLocaleCallLanguagesRepository() {
        final Locale locale = presenter.getCurrentLocale();
        final Locale localeExpected = languagesRepository.getCurrentLocale();

        assertEquals(localeExpected, locale);
    }

    @Test
    public void whenUnregisterListenerThenUnregisterFromLanguagesRepository() {
        presenter.unregisterOnSharedPreferencesListener();
    }

    @Test
    public void whenChangeLanguageThenChangeLanguageFromLanguagesRepository() {
        presenter.changeLanguage("en");

        assertEquals(new Locale("en"), languagesRepository.getCurrentLocale());
    }


}
