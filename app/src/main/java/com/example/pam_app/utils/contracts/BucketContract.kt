package com.example.pam_app.utils.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.example.pam_app.activity.AddBucketActivity
import com.example.pam_app.model.Bucket
import kotlin.jvm.Synchronized

class BucketContract : ActivityResultContract<String?, Bucket?>() {
    override fun createIntent(context: Context, input: String?): Intent {
        if (input != "") {
            val uri = "wally://add_bucket/detail?$input"
            return Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        }
        return Intent(context, AddBucketActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Bucket? {
        return if (resultCode == Activity.RESULT_OK && intent != null) {
            intent.getSerializableExtra("bucket") as Bucket?
        } else null
    }
}