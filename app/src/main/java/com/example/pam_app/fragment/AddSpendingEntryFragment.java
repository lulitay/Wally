package com.example.pam_app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pam_app.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddSpendingEntryFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    private static final List<String> BUCKETS = new ArrayList<>(Arrays.asList("Food", "Entertainment", "Gasoline"));

    public AddSpendingEntryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_spending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, BUCKETS);
        AutoCompleteTextView editTextFilledExposedDropdown = view.findViewById(R.id.auto_complete);
        editTextFilledExposedDropdown.setAdapter(adapter);

        final EditText dateInput = (EditText) view.findViewById(R.id.date);
        final MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pick a date").build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                dateInput.setText(datePicker.getHeaderText());
            }
        });

        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getParentFragmentManager(), "tag");
            }
        });
    }

    public static AddSpendingEntryFragment newInstance(int counter) {
        AddSpendingEntryFragment fragment = new AddSpendingEntryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_OBJECT, counter);
        fragment.setArguments(args);
        return fragment;
    }

}
