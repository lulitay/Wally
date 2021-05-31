package com.example.pam_app.utils.contracts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pam_app.activity.AddBucketActivity;
import com.example.pam_app.model.Bucket;

public class BucketContract extends ActivityResultContract<String, Bucket> {

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, String input) {
        return new Intent(context, AddBucketActivity.class);
    }

    @Override
    public Bucket parseResult(int resultCode, @Nullable Intent intent) {
        if (resultCode == Activity.RESULT_OK && intent != null) {
            return (Bucket) intent.getSerializableExtra("bucket");
        }
        return null;
    }
}
