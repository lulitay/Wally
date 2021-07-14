package com.example.pam_app.utils.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class NotificationPublisher : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val service = Intent(context, NotificationService::class.java)
        service.putExtra("reason", intent.getStringExtra("reason"))
        service.putExtra("timestamp", intent.getLongExtra("timestamp", 0))
        service.putExtra("bucketTitle", intent.getStringExtra("bucketTitle"))

        NotificationService.enqueueWork(context, service)
    }
}