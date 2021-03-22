package com.example.pam_app.entry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pam_app.R;

public class AddSavingEntryFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    public AddSavingEntryFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_saving, container, false);
    }

    public static AddSavingEntryFragment newInstance(Integer counter) {
        AddSavingEntryFragment fragment = new AddSavingEntryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_OBJECT, counter);
        fragment.setArguments(args);
        return fragment;
    }

}
