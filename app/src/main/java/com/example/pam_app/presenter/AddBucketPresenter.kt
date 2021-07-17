package com.example.pam_app.presenter

import com.example.pam_app.R
import com.example.pam_app.fragment.AddBucketEntryFragment
import com.example.pam_app.model.Bucket
import com.example.pam_app.model.BucketType
import com.example.pam_app.repository.BucketRepository
import com.example.pam_app.utils.schedulers.SchedulerProvider
import com.example.pam_app.view.AddBucketView
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference
import java.util.*

class AddBucketPresenter(
        addBucketView: AddBucketView?,
        private val bucketRepository: BucketRepository,
        private val schedulerProvider: SchedulerProvider
) {
    private val addBucketView: WeakReference<AddBucketView?> = WeakReference(addBucketView)
    private var disposable: CompositeDisposable? = null
    fun onAttachView() {
        disposable = CompositeDisposable()
    }

    fun saveBucket(
            title: String,
            dueDate: Date?,
            bucketType: BucketType?,
            target: String,
            imagePath: String?,
            isRecurrent: Boolean
    ) {
        val date = if (isRecurrent) firstDayOfNextMonth else dueDate
        val fields = checkFields(title, date, bucketType, target, isRecurrent)
        if (fields) {
            val bucket = Bucket(title, date, bucketType!!, target.toDouble(), imagePath, isRecurrent)
            disposable!!.add(bucketRepository.create(bucket)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ id: Long? ->
                        if (id == -1L) {
                            throwErrorSavingBucketWithExistingName()
                        } else {
                            fetchBucket(bucket)
                        }
                    }) { throwErrorSavingBucket() }
            )
        }
    }

    fun updateBucket(
            title: String,
            dueDate: Date?,
            bucketType: BucketType?,
            target: String,
            imagePath: String?,
            isRecurrent: Boolean
    ) {
        val date = if (isRecurrent) firstDayOfNextMonth else dueDate
        val fields = checkFields(title, date, bucketType, target, isRecurrent)
        if (fields) {
            disposable!!.add(bucketRepository[title]
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ bucket: Bucket? ->
                        val updatedBucket = Bucket(title, date, bucketType!!, target.toDouble(), bucket?.entries, bucket?.id, imagePath, isRecurrent)
                        bucketRepository.update(updatedBucket).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui()).subscribe({
                            if (addBucketView.get() != null) {
                                addBucketView.get()!!.onSuccessSavingBucket(updatedBucket)
                            }
                        }) {throwErrorSavingBucket()}
                    }
                    ) { throwErrorSavingBucket() }
            )
        }
    }

    private fun checkFields(
            title: String,
            dueDate: Date?,
            bucketType: BucketType?,
            target: String,
            isRecurrent: Boolean
    ): Boolean {
        var isCorrect = true
        if (title.isEmpty()) {
            addBucketView.get()!!.showTitleError(R.string.error_empty, null)
            isCorrect = false
        } else if (title.length > AddBucketEntryFragment.MAX_CHARACTERS) {
            addBucketView.get()!!.showTitleError(R.string.max_characters, AddBucketEntryFragment.MAX_CHARACTERS)
            isCorrect = false
        }
        when {
            target.isEmpty() -> {
                addBucketView.get()!!.showTargetError(R.string.error_empty, null)
                isCorrect = false
            }
            target == "." -> {
                addBucketView.get()!!.showTargetError(R.string.error_format, null)
                isCorrect = false
            }
            target.toDouble() >= AddBucketEntryFragment.MAX_AMOUNT -> {
                addBucketView.get()!!.showTargetError(R.string.max_amount, AddBucketEntryFragment.MAX_AMOUNT)
                isCorrect = false
            }
        }
        if (!isRecurrent && dueDate == null) {
            addBucketView.get()!!.showDateError(R.string.error_empty)
            isCorrect = false
        } else if (!isRecurrent && dueDate!!.time < Date().time) {
            addBucketView.get()!!.showDateError(R.string.error_past_date)
            isCorrect = false
        }
        if (bucketType == null) {
            addBucketView.get()!!.showBucketTypeError(R.string.error_empty)
            isCorrect = false
        }
        return isCorrect
    }

    fun onClickLoadImage() {
        if (addBucketView.get() != null) {
            addBucketView.get()!!.requestStoragePermission()
        }
    }

    fun onViewDetached() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    fun onIsRecurrentSwitchChange(isRecurrent: Boolean) {
        if (addBucketView.get() != null) {
            addBucketView.get()!!.changeDatePickerState(!isRecurrent)
        }
    }

    private val firstDayOfNextMonth: Date
        get() {
            val today = Calendar.getInstance()
            val next = Calendar.getInstance()
            next.clear()
            next[Calendar.YEAR] = today[Calendar.YEAR]
            next[Calendar.MONTH] = today[Calendar.MONTH] + 1
            next[Calendar.DAY_OF_MONTH] = 1
            return next.time
        }

    private fun throwErrorSavingBucket() {
        if (addBucketView.get() != null) {
            addBucketView.get()!!.onErrorSavingBucket()
        }
    }

    private fun throwErrorSavingBucketWithExistingName() {
        if (addBucketView.get() != null) {
            addBucketView.get()!!.onErrorExistingBucketName()
        }
    }

    private fun fetchBucket(bucket: Bucket) {
        disposable!!.add(bucketRepository[bucket.title]
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ b: Bucket? ->
                    if (addBucketView.get() != null) {
                        addBucketView.get()!!.onSuccessSavingBucket(b)
                    }
                }) { throwErrorSavingBucket() }
        )
    }

}