package com.example.pam_app.utils.contracts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pam_app.activity.AddBucketEntryActivity;
import com.example.pam_app.model.BucketEntry;

public class EntryContract extends ActivityResultContract<String, BucketEntry> {

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, String input) {
        return new Intent(context, AddBucketEntryActivity.class);
    }

    @Override
    public BucketEntry parseResult(int resultCode, @Nullable Intent intent) {
        if (resultCode == Activity.RESULT_OK && intent != null) {
            return (BucketEntry) intent.getSerializableExtra("entry");
        }
        return null;
    }
}
