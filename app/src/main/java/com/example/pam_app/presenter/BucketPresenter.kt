package com.example.pam_app.presenter

import com.example.pam_app.model.Bucket
import com.example.pam_app.model.BucketEntry
import com.example.pam_app.repository.BucketRepository
import com.example.pam_app.utils.schedulers.SchedulerProvider
import com.example.pam_app.view.BucketView
import io.reactivex.disposables.CompositeDisposable
import java.io.Serializable
import java.lang.ref.WeakReference
import java.util.*

class BucketPresenter(private val id: Int,
                      bucketView: BucketView?,
                      private val bucketRepository: BucketRepository,
                      private val schedulerProvider: SchedulerProvider) {
    private val bucketView: WeakReference<BucketView?> = WeakReference(bucketView)
    private var disposable: CompositeDisposable? = null
    private var currentBucket: Bucket? = null
    private val createdEntries: ArrayList<Serializable> = ArrayList()
    fun onViewAttach() {
        disposable = CompositeDisposable()
        if (currentBucket == null) {
            disposable!!.add(
                    bucketRepository[id]
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .subscribe({ b: Bucket? ->
                                if (bucketView.get() != null) {
                                    b!!.entries.sortBy { it?.date }
                                    bucketView.get()!!.bind(b)
                                    currentBucket = b
                                }
                            }) {
                                if (bucketView.get() != null) {
                                    bucketView.get()!!.showGetBucketError()
                                }
                            }
            )
        }
    }

    fun onViewDetached() {
        disposable!!.dispose()
    }

    fun onBackSelected() {
        if (bucketView.get() != null) {
            bucketView.get()!!.back(createdEntries, null)
        }
    }

    fun onDeleteSelected() {
        if (bucketView.get() != null) {
            bucketView.get()!!.showSureDialog { onDelete() }
        }
    }

    fun onDelete() {
        if (bucketView.get() != null) {
            disposable!!.add(
                    bucketRepository.delete(id)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .subscribe({
                                if (bucketView.get() != null) {
                                    bucketView.get()!!.showDeleteBucketSuccess()
                                }
                            }) {
                                if (bucketView.get() != null) {
                                    bucketView.get()!!.showDeleteBucketError()
                                }
                            }
            )
            bucketView.get()!!.back(createdEntries, currentBucket!!.id)
        }
    }

    fun onAddEntryClick() {
        if (bucketView.get() != null) {
            bucketView.get()!!.goToAddEntry(currentBucket!!.title, currentBucket!!.bucketType.ordinal)
        }
    }

    fun onAddEntry(entry: Serializable?) {
        if (entry != null) {
            createdEntries.add(entry)
            if (entry is BucketEntry && entry.bucketTitle == currentBucket!!.title) {
                if (currentBucket!!.isRecurrent && entry.date!!.before(firstDayOfMonth)) {
                    currentBucket!!.oldEntries.add(entry as BucketEntry?)
                } else {
                    currentBucket!!.entries.add(entry as BucketEntry?)
                }
                if (bucketView.get() != null) {
                    bucketView.get()!!.bind(currentBucket)
                }
            }
        }
    }

    private val firstDayOfMonth: Date
        get() {
            val today = Calendar.getInstance()
            val next = Calendar.getInstance()
            next.clear()
            next[Calendar.YEAR] = today[Calendar.YEAR]
            next[Calendar.MONTH] = today[Calendar.MONTH]
            next[Calendar.DAY_OF_MONTH] = 1
            return next.time
        }

}