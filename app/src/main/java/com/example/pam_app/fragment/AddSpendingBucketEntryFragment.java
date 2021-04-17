package com.example.pam_app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pam_app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddSpendingBucketEntryFragment extends AddBucketEntryFragment {
    public static final String ARG_OBJECT = "object";
    private static final List<String> BUCKETS = new ArrayList<>(Arrays.asList("Food", "Entertainment", "Gasoline"));

    public AddSpendingBucketEntryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_add_spending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDropDownOptions(view);
    }

    private void setDropDownOptions(final @NonNull View view) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, BUCKETS);
        final AutoCompleteTextView editTextFilledExposedDropdown = view.findViewById(R.id.bucket);
        editTextFilledExposedDropdown.setAdapter(adapter);
    }

    public static AddSpendingBucketEntryFragment newInstance(int counter) {
        AddSpendingBucketEntryFragment fragment = new AddSpendingBucketEntryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_OBJECT, counter);
        fragment.setArguments(args);
        return fragment;
    }
}
