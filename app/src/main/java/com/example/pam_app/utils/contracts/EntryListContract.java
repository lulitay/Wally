package com.example.pam_app.utils.contracts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pam_app.activity.BucketActivity;

import java.io.Serializable;

public class EntryListContract extends ActivityResultContract<String, Serializable> {

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, String input) {
        if (!input.equals("")) {
            final String uri = "wally://bucket/detail?id=" + input;
            return new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        }
        return new Intent(context, BucketActivity.class);
    }

    @Override
    public Serializable parseResult(int resultCode, @Nullable Intent intent) {
        if (resultCode == Activity.RESULT_OK && intent != null) {
            return intent.getSerializableExtra("entries");
        }
        return null;
    }
}
