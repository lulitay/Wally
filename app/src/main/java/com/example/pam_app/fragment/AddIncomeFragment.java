package com.example.pam_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pam_app.R;
import com.example.pam_app.db.WallyDatabase;
import com.example.pam_app.model.Income;
import com.example.pam_app.presenter.AddIncomePresenter;
import com.example.pam_app.repository.BucketMapper;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.IncomeMapper;
import com.example.pam_app.repository.IncomeRepository;
import com.example.pam_app.repository.RoomBucketRepository;
import com.example.pam_app.repository.RoomIncomeRepository;
import com.example.pam_app.utils.schedulers.AndroidSchedulerProvider;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.AddIncomeView;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;

public class AddIncomeFragment extends Fragment implements AddIncomeView {
    public static final String ARG_OBJECT = "object";
    public static final String ARG_BUCKET = "bucket_name";

    private View createdView;

    private EditText description;
    private EditText amount;
    private EditText selectedDate;
    private Calendar date;
    private MaterialDatePicker<Long> datePicker;

    private AddIncomePresenter presenter;

    public AddIncomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final IncomeRepository incomeRepository = new RoomIncomeRepository(
                WallyDatabase.getInstance(requireActivity().getApplicationContext()).incomeDao(),
                new IncomeMapper()
        );
        final BucketRepository bucketRepository = new RoomBucketRepository(
                WallyDatabase.getInstance(requireActivity().getApplicationContext()).bucketDao(),
                new BucketMapper()
        );
        final SchedulerProvider schedulerProvider = new AndroidSchedulerProvider();
        presenter = new AddIncomePresenter(this, incomeRepository, schedulerProvider);
        this.date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return inflater.inflate(R.layout.fragment_add_income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createdView = view;
        description = view.findViewById(R.id.description);
        amount = view.findViewById(R.id.amount);
        selectedDate = view.findViewById(R.id.date);
        date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        datePicker = MaterialDatePicker.Builder.datePicker().setTitleText(getString(R.string.pick_a_date)).build();

        setDatePicker();
        setSaveIncomeListener();
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

    private void setSaveIncomeListener() {
        final Button saveIncome = createdView.findViewById(R.id.save);
        saveIncome.setOnClickListener(v -> presenter.saveIncome(
                description.getText().toString(),
                amount.getText().toString(),
                date.getTime()
        ));
    }

    public static AddIncomeFragment newInstance(final Integer counter, String defaultBucket) {
        final AddIncomeFragment fragment = new AddIncomeFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_OBJECT, counter);
        if(defaultBucket != null) {
            args.putString(ARG_BUCKET, defaultBucket);
        }
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onErrorSavingIncome() {
        Toast.makeText(
                getContext(),
                getString(R.string.error_saving_income),
                Toast.LENGTH_LONG
        ).show();
    }

    @Override
    public void onSuccessSavingIncome(final Income income) {
        Toast.makeText(
                getContext(),
                getString(R.string.income_saving_success, income.getTitle()),
                Toast.LENGTH_LONG
        ).show();
        final Intent result = new Intent();
        result.putExtra("income", income);
        requireActivity().setResult(RESULT_OK, result);
        requireActivity().finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }
}
