package com.example.pam_app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pam_app.R;
import com.example.pam_app.model.BucketEntry;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class AddIncomeEntryFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    private static final List<String> BUCKETS = new ArrayList<>(Arrays.asList("Salary", "Bonus"));

    public AddIncomeEntryFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText description = view.findViewById(R.id.description);
        final EditText amount = view.findViewById(R.id.amount);
        final EditText date = view.findViewById(R.id.date);
        final Calendar selectedDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        final AutoCompleteTextView bucket = view.findViewById(R.id.bucket);

        setDropDownOptions(view);
        setDatePicker(date, selectedDate);
        saveEntry(view, description, amount, date, selectedDate, bucket);
    }

    private void saveEntry(final @NonNull View view,
                           final EditText description,
                           final EditText amount,
                           final EditText date,
                           final Calendar selectedDate,
                           final AutoCompleteTextView bucket
    ) {
        final Button saveEntry = view.findViewById(R.id.save);
        saveEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean fields = checkFields(description, amount, date, bucket);
                if (fields) {
                    final BucketEntry entry = new BucketEntry(
                            Double.parseDouble(amount.getText().toString()),
                            selectedDate.getTime(),
                            description.getText().toString()
                    );
                    Toast.makeText(getContext(), "Entry " + entry.comment + " saved", Toast.LENGTH_LONG).show();
                    requireActivity().onBackPressed();
                }
            }
        });
    }

    private boolean checkFields(
            final EditText description,
            final EditText amount,
            final EditText date,
            final AutoCompleteTextView bucket
    ) {
        if (description.length() == 0) {
            description.setError("Can't be empty");
            return false;
        }
        if (amount.length() == 0) {
            amount.setError("Can't be empty");
            return false;
        }
        if (date.length() == 0) {
            date.setError("Can't be empty");
            return false;
        }
        if (bucket.length() == 0) {
            bucket.setError("Can't be empty");
            return false;
        }
        return true;
    }

    private void setDatePicker(final EditText date, final Calendar selectedDate) {
        final MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pick a date").build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(final Long selection) {
                date.setText(datePicker.getHeaderText());
                selectedDate.setTimeInMillis(selection);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getParentFragmentManager(), "tag");
            }
        });
    }

    private void setDropDownOptions(final @NonNull View view) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, BUCKETS);
        final AutoCompleteTextView editTextFilledExposedDropdown = view.findViewById(R.id.bucket);
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
