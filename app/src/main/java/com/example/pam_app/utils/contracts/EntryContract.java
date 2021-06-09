package com.example.pam_app.utils.contracts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pam_app.activity.AddBucketEntryActivity;
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.model.Income;

public class EntryContract extends ActivityResultContract<String, BucketEntry> {

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, String input) {
        return new Intent(context, AddBucketEntryActivity.class);
    }

    @Override
    public Serializable parseResult(int resultCode, @Nullable Intent intent) {
        if (resultCode == Activity.RESULT_OK && intent != null) {
            if (intent.getSerializableExtra("entry") == null) {
                return intent.getSerializableExtra("income");
            }
            return intent.getSerializableExtra("entry");
        }
        return null;
    }
}
