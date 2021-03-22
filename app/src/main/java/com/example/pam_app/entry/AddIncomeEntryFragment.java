package com.example.pam_app.entry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pam_app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddIncomeEntryFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    private static final List<String> BUCKETS = new ArrayList<>(Arrays.asList("Salary", "Bonus"));

    public AddIncomeEntryFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, BUCKETS);

        AutoCompleteTextView editTextFilledExposedDropdown = view.findViewById(R.id.auto_complete);
        editTextFilledExposedDropdown.setAdapter(adapter);
    }

    public static AddIncomeEntryFragment newInstance(Integer counter) {
        AddIncomeEntryFragment fragment = new AddIncomeEntryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_OBJECT, counter);
        fragment.setArguments(args);
        return fragment;
    }

}
