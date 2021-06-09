package com.example.pam_app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pam_app.R;

public class AddSavingBucketEntryFragment extends AddBucketEntryFragment {
    public static final String ARG_OBJECT = "object";
    public static final String ARG_BUCKET = "bucket_name";

    public AddSavingBucketEntryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_add_saving, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static AddSavingBucketEntryFragment newInstance(Integer counter, String defaultBucket) {
        final AddSavingBucketEntryFragment fragment = new AddSavingBucketEntryFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_OBJECT, counter);
        if(defaultBucket != null) {
            args.putString(ARG_BUCKET, defaultBucket);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBucketType() {
        return this.getArguments().getInt(ARG_OBJECT);
    }
}
