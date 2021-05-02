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
import com.example.pam_app.db.WallyDatabase;
import com.example.pam_app.presenter.AddBucketEntryPresenter;
import com.example.pam_app.repository.BucketMapper;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.RoomBucketRepository;
import com.example.pam_app.view.AddBucketEntryView;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public abstract class AddBucketEntryFragment extends Fragment implements AddBucketEntryView {

    private AddBucketEntryPresenter presenter;
    private View createdView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final BucketRepository bucketRepository = new RoomBucketRepository(
                WallyDatabase.getInstance(requireActivity().getApplicationContext()).bucketDao(),
                new BucketMapper()
        );
        presenter = new AddBucketEntryPresenter(this, bucketRepository);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createdView = view;
        presenter.onViewAttached();

        final EditText description = view.findViewById(R.id.description);
        final EditText amount = view.findViewById(R.id.amount);
        final EditText date = view.findViewById(R.id.date);
        final Calendar selectedDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        final AutoCompleteTextView bucket = view.findViewById(R.id.bucket);

        setDatePicker(date, selectedDate);
        saveEntry(description, amount, date, selectedDate, bucket);
    }

    @Override
    public void setDropDownOptions(final List<String> buckets) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, buckets);
        final AutoCompleteTextView editTextFilledExposedDropdown = createdView.findViewById(R.id.bucket);
        editTextFilledExposedDropdown.setAdapter(adapter);
    }

    @Override
    public void onErrorSavingBucketEntry() {
        Toast.makeText(
                getContext(),
                "Entry could not be saved",
                Toast.LENGTH_LONG
        ).show();
    }

    @Override
    public void onSuccessSavingBucketEntry(final String description) {
        Toast.makeText(
                getContext(),
                "Entry " + description + " saved",
                Toast.LENGTH_LONG
        ).show();
    }

    void saveEntry(final EditText description,
                   final EditText amount,
                   final EditText date,
                   final Calendar selectedDate,
                   final AutoCompleteTextView bucket
    ) {
        final Button saveEntry = createdView.findViewById(R.id.save);
        saveEntry.setOnClickListener(v -> {
            final boolean fields = checkFields(description, amount, date, bucket);
            if (fields) {
                presenter.saveBucketEntry(
                        Double.parseDouble(amount.getText().toString()),
                        selectedDate.getTime(),
                        description.getText().toString(),
                        bucket.getText().toString()
                );
                requireActivity().onBackPressed();
            }
        });
    }

    //TODO improve this
    boolean checkFields(
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

    void setDatePicker(final EditText date, final Calendar selectedDate) {
        final MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pick a date").build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            date.setText(datePicker.getHeaderText());
            selectedDate.setTimeInMillis(selection);
        });
        date.setOnClickListener(v -> {
            if (!datePicker.isAdded()) {
                datePicker.show(getParentFragmentManager(), "date_picker");
            }
        });
    }
}
