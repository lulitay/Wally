package com.example.pam_app.utils.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Pair
import androidx.activity.result.contract.ActivityResultContract
import com.example.pam_app.activity.BucketActivity
import java.io.Serializable
import kotlin.jvm.Synchronized

class EntryListContract : ActivityResultContract<String, Pair<Serializable?, Serializable?>?>() {
    override fun createIntent(context: Context, input: String): Intent {
        if (input != "") {
            val uri = "wally://bucket/detail?id=$input"
            return Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        }
        return Intent(context, BucketActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Pair<Serializable?, Serializable?>? {
        return if (resultCode == Activity.RESULT_OK && intent != null) {
            Pair(intent.getSerializableExtra("entries"), intent.getSerializableExtra("id"))
        } else null
    }
}