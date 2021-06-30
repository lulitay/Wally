package com.example.pam_app.presenter

import com.example.pam_app.R
import com.example.pam_app.fragment.AddBucketEntryFragment
import com.example.pam_app.model.Bucket
import com.example.pam_app.model.BucketEntry
import com.example.pam_app.repository.BucketRepository
import com.example.pam_app.utils.schedulers.SchedulerProvider
import com.example.pam_app.view.AddBucketEntryView
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference
import java.util.*

class AddBucketEntryPresenter(
        addBucketEntryView: AddBucketEntryView?,
        private val bucketRepository: BucketRepository?,
        private val schedulerProvider: SchedulerProvider?
) {
    private val addBucketEntryView: WeakReference<AddBucketEntryView?> = WeakReference(addBucketEntryView)
    private val disposable: CompositeDisposable = CompositeDisposable()
    fun onViewAttached() {
        if (addBucketEntryView.get() != null) {
            val buckets = bucketRepository!!.getTitleListByType(
                    addBucketEntryView.get()?.bucketType!!
            )!!.blockingFirst()
            addBucketEntryView.get()!!.setDropDownOptions(buckets)
        }
    }

    fun onViewDetached() {
        disposable.dispose()
    }

    fun saveBucketEntry(amount: String, date: Date?, description: String,
                        bucketTitle: String?) {
        val fields = checkFields(amount, date, description, bucketTitle)
        if (fields) {
            val entry = BucketEntry(amount.toDouble(), date, description, bucketTitle)
            disposable.add(
                    bucketRepository!![bucketTitle]
                            .subscribeOn(schedulerProvider!!.io())
                            .observeOn(schedulerProvider.ui())
                            .subscribe({ bucket: Bucket? -> addEntryToBucket(entry, bucket) }) {
                                if (addBucketEntryView.get() != null) {
                                    addBucketEntryView.get()!!.onErrorSavingBucketEntry()
                                }
                            }
            )
        }
    }

    private fun addEntryToBucket(entry: BucketEntry, bucket: Bucket?) {
        disposable.add(
                bucketRepository!!.addEntry(entry, bucket!!.id!!)
                        .subscribeOn(schedulerProvider!!.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe({
                            if (addBucketEntryView.get() != null) {
                                addBucketEntryView.get()!!.onSuccessSavingBucketEntry(entry)
                            }
                        }) {
                            if (addBucketEntryView.get() != null) {
                                addBucketEntryView.get()!!.onErrorSavingBucketEntry()
                            }
                        }
        )
    }

    private fun checkFields(
            amount: String,
            date: Date?,
            description: String,
            bucket: String?
    ): Boolean {
        var isCorrect = true
        if (description.isEmpty()) {
            addBucketEntryView.get()!!.showDescriptionError(R.string.error_empty, null)
            isCorrect = false
        } else if (description.length > AddBucketEntryFragment.MAX_CHARACTERS) {
            addBucketEntryView.get()!!.showDescriptionError(R.string.max_characters, AddBucketEntryFragment.MAX_CHARACTERS)
            isCorrect = false
        }
        when {
            amount.isEmpty() -> {
                addBucketEntryView.get()!!.showAmountError(R.string.error_empty, null)
                isCorrect = false
            }
            amount == "." -> {
                addBucketEntryView.get()!!.showAmountError(R.string.error_format, null)
                isCorrect = false
            }
            amount.toDouble() >= AddBucketEntryFragment.MAX_AMOUNT -> {
                addBucketEntryView.get()!!.showAmountError(R.string.max_amount, AddBucketEntryFragment.MAX_AMOUNT)
                isCorrect = false
            }
        }
        if (date == null) {
            addBucketEntryView.get()!!.showDateError(R.string.error_empty)
            isCorrect = false
        } else if (date.time > Date().time) {
            addBucketEntryView.get()!!.showDateError(R.string.error_future_date)
            isCorrect = false
        }
        if (bucket == null || bucket.isEmpty()) {
            addBucketEntryView.get()!!.showBucketTitleError(R.string.error_empty)
            isCorrect = false
        }
        return isCorrect
    }

}