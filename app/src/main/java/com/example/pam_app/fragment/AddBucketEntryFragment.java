package com.example.pam_app.fragment;

import android.content.Intent;
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
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.presenter.AddBucketEntryPresenter;
import com.example.pam_app.repository.BucketMapper;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.RoomBucketRepository;
import com.example.pam_app.utils.schedulers.AndroidSchedulerProvider;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.AddBucketEntryView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;

public abstract class AddBucketEntryFragment extends Fragment implements AddBucketEntryView {

    public static final int MAX_AMOUNT = 1000000000;
    public static final int MAX_CHARACTERS = 50;
    private AddBucketEntryPresenter presenter;
    private View createdView;

    private EditText description;
    private EditText amount;
    private EditText selectedDate;
    private Calendar date;
    private AutoCompleteTextView bucket;
    private TextInputLayout dropdown;
    private MaterialDatePicker<Long> datePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final BucketRepository bucketRepository = new RoomBucketRepository(
                WallyDatabase.getInstance(requireActivity().getApplicationContext()).bucketDao(),
                new BucketMapper()
        );
        final SchedulerProvider schedulerProvider = new AndroidSchedulerProvider();
        presenter = new AddBucketEntryPresenter(this, bucketRepository, schedulerProvider);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createdView = view;
        presenter.onViewAttached();

        description = view.findViewById(R.id.description);
        amount = view.findViewById(R.id.amount);
        selectedDate = view.findViewById(R.id.date);
        date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        bucket = view.findViewById(R.id.bucket);
        datePicker = MaterialDatePicker.Builder.datePicker().setTitleText(getString(R.string.pick_a_date)).build();
        dropdown = view.findViewById(R.id.bucket_dropdown);

        bucket.setText(getArguments().getString("bucket_name"), false);

        setDatePicker();
        setSaveEntryListener();
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
                getString(R.string.entry_saving_failed),
                Toast.LENGTH_LONG
        ).show();
    }

    @Override
    public void onSuccessSavingBucketEntry(final BucketEntry entry) {
        Toast.makeText(
                getContext(),
                getString(R.string.entry_saving_success, entry.getComment()),
                Toast.LENGTH_LONG
        ).show();
        final Intent result = new Intent();
        result.putExtra("entry", entry);
        requireActivity().setResult(RESULT_OK, result);
        getActivity().finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }

    @Override
    public void showDescriptionError(final int error, final Integer parameter) {
        if (parameter == null) {
            description.setError(getString(error));
        } else {
            description.setError(getString(error, parameter));
        }
    }

    @Override
    public void showAmountError(final int error, final Integer parameter) {
        if (parameter == null) {
            amount.setError(getString(error));
        } else {
            amount.setError(getString(error, parameter));
        }
    }

    @Override
    public void showDateError(final int error) {
        selectedDate.requestFocus();
        getParentFragmentManager().beginTransaction().remove(datePicker).commit();
        selectedDate.setError(getString(error));
    }

    @Override
    public void showBucketTitleError(int error) {
        dropdown.setError(getString(error));
    }


    private void setSaveEntryListener() {
        final Button saveEntry = createdView.findViewById(R.id.save);
        saveEntry.setOnClickListener(v -> presenter.saveBucketEntry(
                amount.getText().toString(),
                date.getTime(),
                description.getText().toString(),
                bucket.getText().toString()
        ));
    }

    private void setDatePicker() {
        datePicker.addOnPositiveButtonClickListener(selection -> {
            selectedDate.setText(datePicker.getHeaderText());
            date.setTimeInMillis(selection);
        });

        selectedDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && !datePicker.isAdded()) {
                datePicker.show(getParentFragmentManager(), "date_picker");
            }
        });

        selectedDate.setOnClickListener(v -> {
            if (!datePicker.isAdded()) {
                datePicker.show(getParentFragmentManager(), "date_picker");
            }
        });
    }
}
