package com.example.pam_app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pam_app.R;

public class AddIncomeBucketEntryFragment extends AddBucketEntryFragment {
    public static final String ARG_OBJECT = "object";

    public AddIncomeBucketEntryFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_add_income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static AddIncomeBucketEntryFragment newInstance(final Integer counter) {
        final AddIncomeBucketEntryFragment fragment = new AddIncomeBucketEntryFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_OBJECT, counter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBucketType() {
        return this.getArguments().getInt(ARG_OBJECT);
    }
}
