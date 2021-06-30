package com.example.pam_app.utils.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.example.pam_app.activity.AddBucketActivity
import com.example.pam_app.model.Bucket
import kotlin.jvm.Synchronized

class BucketContract : ActivityResultContract<String?, Bucket?>() {
    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(context, AddBucketActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Bucket? {
        return if (resultCode == Activity.RESULT_OK && intent != null) {
            intent.getSerializableExtra("bucket") as Bucket?
        } else null
    }
}