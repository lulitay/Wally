package com.example.pam_app.utils.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.example.pam_app.activity.AddBucketEntryActivity
import java.io.Serializable

class EntryContract : ActivityResultContract<String, Serializable?>() {
    override fun createIntent(context: Context, input: String): Intent {
        if (input != "") {
            val uri = "wally://add_entry/detail?$input"
            return Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        }
        return Intent(context, AddBucketEntryActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Serializable? {
        return if (resultCode == Activity.RESULT_OK && intent != null) {
            if (intent.getSerializableExtra("entry") == null) {
                intent.getSerializableExtra("income")
            } else intent.getSerializableExtra("entry")
        } else ""
    }
}