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
import com.example.pam_app.utils.schedulers.AndroidSchedulerProvider;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.AddBucketEntryView;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public abstract class AddBucketEntryFragment extends Fragment implements AddBucketEntryView {

    public static final int MAX_AMOUNT = 1000000000;
    public static final int MAX_CHARACTERS = 50;
    private AddBucketEntryPresenter presenter;
    private View createdView;

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
                getString(R.string.entry_saving_failed),
                Toast.LENGTH_LONG
        ).show();
    }

    @Override
    public void onSuccessSavingBucketEntry(final String description) {
        Toast.makeText(
                getContext(),
                getString(R.string.entry_saving_success, description),
                Toast.LENGTH_LONG
        ).show();
        requireActivity().onBackPressed();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }

    void saveEntry(final EditText description,
                   final EditText amount,
                   final EditText date,
                   final Calendar selectedDate,
                   final AutoCompleteTextView bucket
    ) {
        final Button saveEntry = createdView.findViewById(R.id.save);
        saveEntry.setOnClickListener(v -> {
            final boolean fields = checkFields(description, amount, date, selectedDate, bucket);
            if (fields) {
                presenter.saveBucketEntry(
                        Double.parseDouble(amount.getText().toString()),
                        selectedDate.getTime(),
                        description.getText().toString(),
                        bucket.getText().toString()
                );
            }
        });
    }

    // TODO improve this
    boolean checkFields(
            final EditText description,
            final EditText amount,
            final EditText date,
            final Calendar selectedDate,
            final AutoCompleteTextView bucket
    ) {
        boolean isCorrect = true;
        if (description.length() == 0) {
            description.setError(getString(R.string.error_empty));
            isCorrect = false;
        } else if (description.getText().length() > MAX_CHARACTERS) {
            description.setError(getString(R.string.max_characters, MAX_CHARACTERS));
            isCorrect = false;
        }
        if (amount.length() == 0) {
            amount.setError(getString(R.string.error_empty));
            isCorrect = false;
        } else if (Double.parseDouble(amount.getText().toString()) >= MAX_AMOUNT) {
            amount.setError(getString(R.string.max_amount, MAX_AMOUNT));
            isCorrect = false;
        }
        if (selectedDate == null) {
            date.setError(getString(R.string.error_empty));
            isCorrect = false;
        } else if (selectedDate.getTimeInMillis() > new Date().getTime()) {
            date.setError(getString(R.string.error_future_date));
            isCorrect = false;
        }
        if (bucket.length() == 0) {
            bucket.setError(getString(R.string.error_empty));
            isCorrect = false;
        }
        return isCorrect;
    }

    void setDatePicker(final EditText date, final Calendar selectedDate) {
        final MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.pick_a_date)).build();

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
