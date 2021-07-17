package com.example.pam_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.MenuItem
import android.widget.ViewFlipper
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.example.pam_app.di.ContainerLocator
import com.example.pam_app.model.Bucket
import com.example.pam_app.model.BucketEntry
import com.example.pam_app.model.Income
import com.example.pam_app.presenter.MainPresenter
import com.example.pam_app.utils.contracts.BucketContract
import com.example.pam_app.utils.contracts.EntryContract
import com.example.pam_app.utils.contracts.EntryListContract
import com.example.pam_app.utils.listener.Clickable
import com.example.pam_app.utils.notifications.NotificationUtils
import com.example.pam_app.utils.workers.RecurrentBucketWorker
import com.example.pam_app.utils.workers.RecurrentBucketWorkerFactory
import com.example.pam_app.utils.workers.RecurrentIncomeWorker
import com.example.pam_app.view.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), Clickable, MainView {
    private var navigationView: BottomNavigationView? = null
    private var viewFlipper: ViewFlipper? = null
    private var homeView: HomeView? = null
    private var bucketListView: BucketListView? = null
    private var incomeView: IncomeView? = null
    private var profileView: ProfileView? = null
    private var addBucketResultLauncher: ActivityResultLauncher<String?>? = null
    private var addBucketEntryResultLauncher: ActivityResultLauncher<String>? = null
    private var bucketDetailResultLauncher: ActivityResultLauncher<String>? = null
    private var presenter: MainPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = ContainerLocator.locateComponent(this)
        presenter = MainPresenter(container?.bucketRepository, container?.incomeRepository,
                this, container?.schedulerProvider, container?.languageRepository)
        setUpChosenLanguage()
        setContentView(R.layout.activity_main)
        setUpViews()
        setUpBottomNavigation()
        setUpAddBucketResultLauncher()
        setUpBucketDetailResultLauncher()
        setUpAddEntryResultLauncher()
        setUpFAB()
        setUpRecurrentBucketWorker()
    }

    override fun onClick() {
        addBucketResultLauncher!!.launch("addBucket")
    }

    override fun onStop() {
        super.onStop()
        presenter!!.onViewDetached()
        presenter!!.unregisterOnSharedPreferencesListener()
    }

    override fun onStart() {
        super.onStart()
        presenter!!.onViewAttached()
        profileView!!.bind(presenter?.getCurrentLanguage()?.language, ::applyChanges)
    }

    override fun onBucketListViewReceived(bucketList: List<Bucket>?) {
        bucketListView!!.bind(this, { launchAddBucketActivity() }, { bucketId -> launchBucketDetailActivity(bucketId) }, bucketList)
    }

    override fun onEntriesReceived(entryList: MutableList<BucketEntry>?) {
        homeView!!.bind(entryList?.toList())
    }

    override fun onIncomeDataReceived(incomeList: List<Income>?, incomeLeft: Double) {
        incomeView!!.bind(incomeList, incomeLeft)
    }

    override fun updateLocale(locale: Locale?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun setUpRecurrentBucketWorker() {
        val container = ContainerLocator.locateComponent(this)
        val myConfig = Configuration.Builder()
                .setMinimumLoggingLevel(Log.INFO)
                .setWorkerFactory(RecurrentBucketWorkerFactory(container?.bucketRepository, container?.incomeRepository))
                .build()
        val workManager: WorkManager = try {
            WorkManager.getInstance(applicationContext)
        } catch (e: Exception) {
            WorkManager.initialize(this, myConfig)
            WorkManager.getInstance(applicationContext)
        }
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        dueDate[Calendar.HOUR_OF_DAY] = 1
        dueDate[Calendar.MINUTE] = 0
        dueDate[Calendar.SECOND] = 0
        dueDate[Calendar.DAY_OF_MONTH] = currentDate[Calendar.DAY_OF_MONTH] + 1
        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
        val bucketRecurrent = PeriodicWorkRequestBuilder<RecurrentBucketWorker>(
                1, TimeUnit.DAYS,
                30, TimeUnit.MINUTES)
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .build()
        val incomeRecurrent = PeriodicWorkRequestBuilder<RecurrentIncomeWorker>(
                1, TimeUnit.DAYS,
                30, TimeUnit.MINUTES)
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .build()
        workManager.enqueueUniquePeriodicWork("recurrent_bucket",
                ExistingPeriodicWorkPolicy.REPLACE, bucketRecurrent)
        workManager.enqueueUniquePeriodicWork("recurrent_income",
                ExistingPeriodicWorkPolicy.REPLACE, incomeRecurrent)
    }

    @SuppressLint("NonConstantResourceId")
    private fun setUpBottomNavigation() {
        navigationView = findViewById(R.id.bottom_navigation)
        navigationView?.selectedItemId = R.id.home
        navigationView?.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> {
                    viewFlipper!!.displayedChild = HOME_VIEW
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.buckets -> {
                    viewFlipper!!.displayedChild = BUCKETS_VIEW
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.income -> {
                    viewFlipper!!.displayedChild = INCOME_VIEW
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    viewFlipper!!.displayedChild = PROFILE_VIEW
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
    }

    private fun setUpViews() {
        viewFlipper = findViewById(R.id.switcher)
        homeView = findViewById(R.id.home)
        bucketListView = findViewById(R.id.buckets)
        incomeView = findViewById(R.id.income)
        profileView = findViewById(R.id.profile)
    }

    private fun launchAddBucketEntryActivity() {
        addBucketEntryResultLauncher!!.launch("")
    }

    private fun launchBucketDetailActivity(bucketId: Int) {
        bucketDetailResultLauncher!!.launch(bucketId.toString())
    }

    private fun launchAddBucketActivity() {
        addBucketResultLauncher!!.launch("")
    }

    private fun setUpFAB() {
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { launchAddBucketEntryActivity() }
    }

    private fun setLocale(locale: Locale?) {
        val config = resources.configuration
        config.setLocale(locale)
        Locale.setDefault(locale!!)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun onEntryAdded(entry: java.io.Serializable) {
        if (entry is BucketEntry) {
            homeView!!.onBucketEntryAdded(entry)
            incomeView!!.onBucketEntryAdded(entry.amount)
        } else if (entry is Income) {
            incomeView!!.onIncomeAdded(entry)
        }
    }

    private fun setUpChosenLanguage() {
        setLocale(presenter?.getCurrentLanguage())
    }

    private fun setUpAddBucketResultLauncher() {
        addBucketResultLauncher = registerForActivityResult(
                BucketContract()
        ) { result: Bucket? -> bucketListView?.onBucketAdded(result)
            val time = getNotificationDelay(result!!) + 5 + Calendar.getInstance().timeInMillis
            NotificationUtils().setNotification(time, this@MainActivity, result.title!!)
        }
    }

    private fun setUpAddEntryResultLauncher() {
        addBucketEntryResultLauncher = registerForActivityResult(
                EntryContract()) { entry: java.io.Serializable? -> onEntryAdded(entry!!) }
    }

    @Suppress("UNCHECKED_CAST")
    private fun setUpBucketDetailResultLauncher() {
        bucketDetailResultLauncher = registerForActivityResult(
                EntryListContract()
        ) { result: Pair<java.io.Serializable?, java.io.Serializable?>? ->
            if (result?.first is List<*>) {
                (result.first as List<java.io.Serializable>).forEach { entry: java.io.Serializable -> onEntryAdded(entry) }
            }
            if (result?.second is Int) {
                presenter!!.onBucketChanged(result.second as Int)
            }
        }
    }

    private fun applyChanges(language: String) {
        presenter!!.changeLanguage(language)
    }

    private fun getNotificationDelay(bucket: Bucket) : Long {
        val borderDate = Calendar.getInstance()
        borderDate[Calendar.DAY_OF_MONTH] += 3
        val entryDate = Calendar.getInstance()
        entryDate.time = bucket.dueDate!!
        return if (entryDate < borderDate) {
            0
        } else {
            entryDate[Calendar.DAY_OF_MONTH] -= 3
            entryDate.timeInMillis
        }
    }

    override fun onUpdateBucket(bucket: Bucket) {
        bucketListView!!.onUpdateBucket(bucket)
    }

    override fun onDeleteBucket(id: Int) {
        bucketListView!!.onDeleteBucket(id)
        homeView!!.onDeleteBucket(id)
    }

    companion object {
        private const val HOME_VIEW = 0
        private const val BUCKETS_VIEW = 1
        private const val INCOME_VIEW = 2
        private const val PROFILE_VIEW = 3
    }
}