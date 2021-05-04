package com.example.pam_app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.pam_app.view.AddBucketView;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.TimeZone;

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
        presenter = new AddBucketPresenter(this, bucketRepository);
        final Button load_image = findViewById(R.id.button_load_image);

        load_image.setOnClickListener(view -> presenter.onClickLoadImage());
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
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
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

        setDatePicker(dueDate, selectedDate);
        saveBucket(title, target, dueDate, selectedDate, bucketType);
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
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void saveBucket(final EditText title,
                            final EditText target,
                            final EditText dueDate,
                            final Calendar selectedDate,
                            final AutoCompleteTextView bucketType) {
        final Button saveEntry = findViewById(R.id.save);
        saveEntry.setOnClickListener(v -> {
            final boolean fields = checkFields(title, target, dueDate, bucketType, imagePath);
            if (fields) {
                presenter.saveBucket(
                        title.getText().toString(),
                        selectedDate.getTime(),
                        BucketType.valueOf(bucketType.getText().toString().toUpperCase()),
                        Double.parseDouble(target.getText().toString()),
                        imagePath
                );
                onBackPressed();
            }
        });

    }

    //TODO improve this
    private boolean checkFields(
            final EditText title,
            final EditText target,
            final EditText dueDate,
            final AutoCompleteTextView bucketType,
            final String imagePath
    ) {
        if (title.length() == 0) {
            title.setError("Can't be empty");
            return false;
        }
        if (target.length() == 0) {
            target.setError("Can't be empty");
            return false;
        }
        if (dueDate.length() == 0) {
            dueDate.setError("Can't be empty");
            return false;
        }
        if (bucketType.length() == 0) {
            bucketType.setError("Can't be empty");
            return false;
        }
        return true;
    }

    private void setDatePicker(final EditText date, final Calendar selectedDate) {
        final MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pick a date").build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            date.setText(datePicker.getHeaderText());
            selectedDate.setTimeInMillis(selection);
        });
        date.setOnClickListener(v -> {
            if (!datePicker.isAdded()) {
                datePicker.show(getSupportFragmentManager(), "date_picker");
            }
        });
    }

    private void setBucketTypeValues(final AutoCompleteTextView bucketType) {
        final ArrayAdapter<BucketType> adapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.list_item, BucketType.values()
        );
        bucketType.setAdapter(adapter);
    }

    @Override
    public void onErrorSavingBucket() {
        Toast.makeText(
                getApplicationContext(),
                "Error saving bucket",
                Toast.LENGTH_LONG
        ).show();
    }

    @Override
    public void onSuccessSavingBucket(final String title) {
        Toast.makeText(
                getApplicationContext(),
                "Bucket " + title + " saved",
                Toast.LENGTH_LONG
        ).show();
    }

    @Override
    public void goToLoadImage() {
        galleryResultLauncher.launch("image/*");
    }
}
