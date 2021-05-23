package com.example.pam_app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pam_app.R;
import com.example.pam_app.db.WallyDatabase;
import com.example.pam_app.model.BucketType;
import com.example.pam_app.presenter.AddBucketPresenter;
import com.example.pam_app.repository.BucketMapper;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.RoomBucketRepository;
import com.example.pam_app.utils.contracts.GalleryContract;
import com.example.pam_app.utils.schedulers.AndroidSchedulerProvider;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.AddBucketView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.example.pam_app.fragment.AddBucketEntryFragment.MAX_AMOUNT;
import static com.example.pam_app.fragment.AddBucketEntryFragment.MAX_CHARACTERS;

public class AddBucketActivity extends AppCompatActivity implements AddBucketView {

    private AddBucketPresenter presenter;

    private ImageView imageView;
    private String imagePath;

    private ActivityResultLauncher<String> galleryResultLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bucket);

        final BucketRepository bucketRepository = new RoomBucketRepository(
                WallyDatabase.getInstance(getApplicationContext()).bucketDao(),
                new BucketMapper()
        );
        final SchedulerProvider schedulerProvider = new AndroidSchedulerProvider();
        presenter = new AddBucketPresenter(this, bucketRepository, schedulerProvider);
        final Button loadImage = findViewById(R.id.button_load_image);
        loadImage.setOnClickListener(view -> presenter.onClickLoadImage());
        this.imageView = findViewById(R.id.image_view);

        this.galleryResultLauncher = registerForActivityResult(
                new GalleryContract(),
                result -> {
                    if (result != null) {
                        try {
                            final InputStream imageStream = getContentResolver().openInputStream(result);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            imageView.setImageBitmap(selectedImage);
                            imagePath = result.toString();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        final EditText title = findViewById(R.id.title);
        final EditText dueDate = findViewById(R.id.due_date);
        final Calendar selectedDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        final AutoCompleteTextView bucketType = findViewById(R.id.bucket_type);
        final EditText target = findViewById(R.id.target);
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setTitleText(getString(R.string.pick_a_date)).build();

        setDatePicker(dueDate, selectedDate, datePicker);
        saveBucket(title, target, dueDate, selectedDate, datePicker, bucketType);
        setBucketTypeValues(bucketType);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            final Uri imageUri = data.getData();
            try {
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
                imagePath = imageUri.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void saveBucket(final EditText title,
                            final EditText target,
                            final EditText dueDate,
                            final Calendar selectedDate,
                            MaterialDatePicker<Long> datePicker,
                            final AutoCompleteTextView bucketType) {
        final Button saveEntry = findViewById(R.id.save);
        saveEntry.setOnClickListener(v -> {
            final boolean fields = checkFields(title, target, dueDate, selectedDate, datePicker, bucketType, imagePath);
            if (fields) {
                presenter.saveBucket(
                        title.getText().toString(),
                        selectedDate.getTime(),
                        BucketType.valueOf(bucketType.getText().toString().toUpperCase()),
                        Double.parseDouble(target.getText().toString()),
                        imagePath
                );
            }
        });

    }

    //TODO improve this
    private boolean checkFields(
            final EditText title,
            final EditText target,
            final EditText dueDate,
            final Calendar selectedDate,
            MaterialDatePicker<Long> datePicker,
            final AutoCompleteTextView bucketType,
            final String imagePath
    ) {
        boolean isCorrect = true;
        if (title.length() == 0) {
            title.setError(getString(R.string.error_empty));
            isCorrect = false;
        } else if (title.getText().length() > MAX_CHARACTERS) {
            title.setError(getString(R.string.max_characters, MAX_CHARACTERS));
            isCorrect = false;
        }
        if (target.length() == 0) {
            target.setError(getString(R.string.error_empty));
            isCorrect = false;
        } else if (Double.parseDouble(target.getText().toString()) >= MAX_AMOUNT) {
            target.setError(getString(R.string.max_amount, MAX_AMOUNT));
            isCorrect = false;
        }
        if (selectedDate == null) {
            dueDate.requestFocus();
            getSupportFragmentManager().beginTransaction().remove(datePicker).commit();
            dueDate.setError(getString(R.string.error_empty));
            isCorrect = false;
        } else if (selectedDate.getTimeInMillis() < new Date().getTime()) {
            dueDate.requestFocus();
            getSupportFragmentManager().beginTransaction().remove(datePicker).commit();
            dueDate.setError(getString(R.string.error_past_date));
            isCorrect = false;
        }
        if (bucketType.length() == 0) {
            TextInputLayout dropdown = findViewById(R.id.bucket_type_dropdown);
            dropdown.setError(getString(R.string.error_empty));
            isCorrect = false;
        }
        return isCorrect;
    }

    private void setDatePicker(final EditText date, final Calendar selectedDate, MaterialDatePicker<Long> datePicker) {
        datePicker.addOnPositiveButtonClickListener(selection -> {
            date.setText(datePicker.getHeaderText());
            selectedDate.setTimeInMillis(selection);
        });

        date.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && !datePicker.isAdded()) {
                datePicker.show(AddBucketActivity.this.getSupportFragmentManager(), "date_picker");
            }
        });

        date.setOnClickListener(v -> {
            if (!datePicker.isAdded()) {
                datePicker.show(AddBucketActivity.this.getSupportFragmentManager(), "date_picker");
            }
        });
    }

    private void setBucketTypeValues(final AutoCompleteTextView bucketType) {
        ArrayList<String> bucketTypeResources = new ArrayList<>();
        for(BucketType type : BucketType.values()){
            bucketTypeResources.add(getString(type.getStringResource()));
        }

        final ArrayAdapter adapter = new ArrayAdapter(
                getApplicationContext(), R.layout.list_item, bucketTypeResources
        );
        bucketType.setAdapter(adapter);
    }

    @Override
    public void onErrorSavingBucket() {
        Toast.makeText(
                getApplicationContext(),
                getString(R.string.error_saving_bucket),
                Toast.LENGTH_LONG
        ).show();
    }

    @Override
    public void onSuccessSavingBucket(final String title) {
        Toast.makeText(
                getApplicationContext(),
                getString(R.string.bucket_saved, title),
                Toast.LENGTH_LONG
        ).show();
        onBackPressed();
    }

    @Override
    public void goToLoadImage() {
        galleryResultLauncher.launch("image/*");
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onDetachView();
    }
}
