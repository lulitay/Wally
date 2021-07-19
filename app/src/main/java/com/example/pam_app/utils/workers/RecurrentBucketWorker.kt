package com.example.pam_app.utils.workers

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.example.pam_app.MainActivity
import com.example.pam_app.model.Bucket
import com.example.pam_app.repository.BucketRepository
import com.example.pam_app.utils.notifications.NotificationUtils
import io.reactivex.Single
import io.reactivex.SingleEmitter
import java.util.*

class RecurrentBucketWorker(context: Context, params: WorkerParameters,
                            private val bucketRepository: BucketRepository) : RxWorker(context, params) {
    override fun createWork(): Single<Result> {
        val today = Calendar.getInstance()
        if (today[Calendar.DAY_OF_MONTH] != 1) {
            return Single.create { emitter: SingleEmitter<Result> -> emitter.onSuccess(Result.success()) }
        }
        val now = today.time
        val notificationDelay = getNotificationDelay(firstDayOfNextMonth) + 5 + Calendar.getInstance().timeInMillis
        return bucketRepository.getList(true, now)
                .firstOrError()
                .doOnSuccess { buckets: List<Bucket?>? ->
                    for (b in buckets!!) {
                        bucketRepository.update(b!!.setDueDate(firstDayOfNextMonth))
                        NotificationUtils().setNotification(notificationDelay, MainActivity(), b.title!!)
                    }
                }
                .map { Result.success() }
                .onErrorReturn { Result.failure() }
    }

    private fun getNotificationDelay(date: Date) : Long {
        val borderDate = Calendar.getInstance()
        borderDate[Calendar.DAY_OF_MONTH] += 3
        val entryDate = Calendar.getInstance()
        entryDate.time = date
        entryDate[Calendar.DAY_OF_MONTH] -= 3
        return entryDate.timeInMillis
    }

    private val firstDayOfNextMonth: Date
        get() {
            val today = Calendar.getInstance()
            val next = Calendar.getInstance()
            next.clear()
            next[Calendar.YEAR] = today[Calendar.YEAR]
            next[Calendar.MONTH] = today[Calendar.MONTH] + 1
            next[Calendar.DAY_OF_MONTH] = 1
            next[Calendar.HOUR] = 0
            return next.time
        }
}