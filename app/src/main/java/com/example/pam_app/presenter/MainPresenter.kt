package com.example.pam_app.presenter

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.example.pam_app.model.Bucket
import com.example.pam_app.model.BucketEntry
import com.example.pam_app.model.Income
import com.example.pam_app.repository.BucketRepository
import com.example.pam_app.repository.IncomeRepository
import com.example.pam_app.repository.LanguagesRepository
import com.example.pam_app.repository.LanguagesRepositoryImpl
import com.example.pam_app.utils.schedulers.SchedulerProvider
import com.example.pam_app.view.MainView
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference
import java.util.*

class MainPresenter(
        private val bucketRepository: BucketRepository?,
        private val incomeRepository: IncomeRepository?,
        mainActivity: MainView?,
        private val schedulerProvider: SchedulerProvider?,
        private val languagesRepository: LanguagesRepository?
) {
    private val mainView: WeakReference<MainView?> = WeakReference(mainActivity)
    private var disposable: CompositeDisposable? = null
    private var totalIncome: Double?
    private var totalSpending: Double?
    private var bucketList: List<Bucket>?
    private var bucketEntryList: List<BucketEntry>?
    private var incomeList: List<Income>?
    fun onViewAttached() {
        disposable = CompositeDisposable()
        languagesRepository!!.setOnSharedPreferencesListener(sharedPreferencesListener())
        if (bucketList == null) {
            disposable!!.add(bucketRepository!!.list
                    .subscribeOn(schedulerProvider!!.computation())
                    .observeOn(schedulerProvider.ui())
                    .subscribe { bucketList: List<Bucket>? -> onBucketsReceived(bucketList) })
        }
        if (bucketEntryList == null) {
            disposable!!.add(bucketRepository?.entryList
                    ?.map { unsortedList: List<BucketEntry>? ->
                        val sortedList: List<BucketEntry> = ArrayList(unsortedList)
                        sortedList.sortedBy { it.date }
                        sortedList.toMutableList()
                    }
                    ?.subscribeOn(schedulerProvider!!.computation())
                    ?.observeOn(schedulerProvider.ui())
                    ?.subscribe { entries: MutableList<BucketEntry> -> onEntriesReceived(entries) }!!)
        }
        if (incomeList == null) {
            disposable!!.add(incomeRepository?.list
                    ?.subscribeOn(schedulerProvider!!.computation())
                    ?.observeOn(schedulerProvider.ui())
                    ?.subscribe { incomeList: List<Income> -> onIncomesReceived(incomeList) }!!)
        }
    }

    private fun onBucketsReceived(bucketList: List<Bucket>?) {
        this.bucketList = bucketList
        if (mainView.get() != null) {
            mainView.get()!!.onBucketListViewReceived(bucketList)
        }
    }

    private fun onEntriesReceived(entries: MutableList<BucketEntry>?) {
        bucketEntryList = entries
        totalSpending = entries?.stream()?.mapToDouble { obj: BucketEntry? -> obj?.amount!! }?.sum()
        if (mainView.get() != null) {
            mainView.get()!!.onEntriesReceived(entries)
        }
    }

    private fun onIncomesReceived(incomeList: List<Income>?) {
        this.incomeList = incomeList
        totalIncome = incomeList!!.stream().mapToDouble { obj: Income? -> obj?.amount!! }.sum()
        if (totalSpending != null && mainView.get() != null) {
            mainView.get()!!.onIncomeDataReceived(incomeList, totalIncome!! - totalSpending!!)
        }
    }

    fun onViewDetached() {
        disposable!!.dispose()
    }

    fun unregisterOnSharedPreferencesListener() {
        languagesRepository!!.unregisterOnSharedPreferencesListener()
    }

    val currentLocale: Locale?
        get() = languagesRepository?.currentLocale

    fun changeLanguage(language: String?) {
        languagesRepository!!.changeLanguage(language)
    }

    private fun sharedPreferencesListener(): OnSharedPreferenceChangeListener {
        return OnSharedPreferenceChangeListener { _: SharedPreferences?, key: String ->
            if (key == LanguagesRepositoryImpl.KEY_PREF_LANGUAGE) {
                when (languagesRepository!!.getKeyPrefLanguage(key)) {
                    "en" -> {
                        val localeEN = Locale("en")
                        mainView.get()!!.updateLocale(localeEN)
                    }
                    "es" -> {
                        val localeES = Locale("es")
                        mainView.get()!!.updateLocale(localeES)
                    }
                }
            }
        }
    }

    fun onBucketChanged(id: Int) {
        disposable!!.add(bucketRepository!![id]
                .subscribeOn(schedulerProvider!!.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe({ bucket: Bucket? ->
                    if (mainView.get() != null) {
                        mainView.get()!!.onUpdateBucket(bucket!!)
                    }
                }) { if (mainView.get() != null) {
                    mainView.get()!!.onDeleteBucket(id)
                } }
        )
    }

    init {
        totalIncome = null
        totalSpending = null
        bucketList = null
        bucketEntryList = null
        incomeList = null
    }
}