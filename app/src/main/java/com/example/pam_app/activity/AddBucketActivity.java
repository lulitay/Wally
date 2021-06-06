package com.example.pam_app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.pam_app.R;
import com.example.pam_app.db.WallyDatabase;
import com.example.pam_app.model.Bucket;
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
import java.util.TimeZone;

public class AddBucketActivity extends AppCompatActivity implements AddBucketView, ActivityCompat.OnRequestPermissionsResultCallback  {
    private static final int REQUEST_EXTERNAL_STORAGE = 0;

    private AddBucketPresenter presenter;

    private ImageView imageView;
    private String imagePath;
    private Calendar date;
    private EditText title;
    private EditText dueDate;
    private AutoCompleteTextView bucketType;
    private EditText target;
    MaterialDatePicker<Long> datePicker;
    TextInputLayout dropdown;

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
        this.date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        registerImageActivityResult();
    }

    @Override
    protected void onStart() {
        super.onStart();
        title = findViewById(R.id.title);
        dueDate = findViewById(R.id.due_date);
        bucketType = findViewById(R.id.bucket_type);
        target = findViewById(R.id.target);
        datePicker = MaterialDatePicker.Builder.datePicker().setTitleText(getString(R.string.pick_a_date)).build();

        setDatePicker();
        setSaveBucketListener();
        setBucketTypeValues();
    }

    private void setSaveBucketListener() {
        final Button saveEntry = findViewById(R.id.save);
        saveEntry.setOnClickListener(v ->
                presenter.saveBucket(
                title.getText().toString(),
                date.getTime(),
                BucketType.getBucketType(bucketType.getText().toString()),
                Double.parseDouble(target.getText().toString()),
                imagePath
        ));

    }

    @Override
    public void showTitleError(final int error, final Integer parameter) {
        if (parameter == null) {
            title.setError(getString(error));
        } else {
            title.setError(getString(error, parameter));
        }
    }

    @Override
    public void showTargetError(final int error, final Integer parameter) {
        if (parameter == null) {
            target.setError(getString(error));
        } else {
            target.setError(getString(error, parameter));
        }
    }

    @Override
    public void showDateError(final int error) {
        dueDate.requestFocus();
        getSupportFragmentManager().beginTransaction().remove(datePicker).commit();
        dueDate.setError(getString(error));
    }

    @Override
    public void showBucketTypeError(int error) {
        dropdown.setError(getString(error));
    }

    private void setDatePicker() {
        datePicker.addOnPositiveButtonClickListener(selection -> {
            dueDate.setText(datePicker.getHeaderText());
            date.setTimeInMillis(selection);
        });

        dueDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && !datePicker.isAdded()) {
                datePicker.show(AddBucketActivity.this.getSupportFragmentManager(), "date_picker");
            }
        });

        dueDate.setOnClickListener(v -> {
            if (!datePicker.isAdded()) {
                datePicker.show(getSupportFragmentManager(), "date_picker");
            }
        });
    }

    private void setBucketTypeValues() {
        ArrayList<String> bucketTypeResources = new ArrayList<>();
        for (BucketType type : BucketType.values()) {
            bucketTypeResources.add(getString(type.getStringResource()));
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.list_item, bucketTypeResources
        );
        bucketType.setAdapter(adapter);
    }

    private void registerImageActivityResult() {
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
    public void onErrorSavingBucket() {
        Toast.makeText(
                getApplicationContext(),
                getString(R.string.error_saving_bucket),
                Toast.LENGTH_LONG
        ).show();
    }

    @Override
    public void onSuccessSavingBucket(final Bucket bucket) {
        Toast.makeText(
                getApplicationContext(),
                getString(R.string.bucket_saved, bucket.title),
                Toast.LENGTH_LONG
        ).show();
        final Intent result = new Intent();
        result.putExtra("bucket", bucket);
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onDetachView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImage();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.warning_bucket), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void loadImage() {
        galleryResultLauncher.launch("image/*");
    }
}
