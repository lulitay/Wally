package com.example.pam_app.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_app.R;
import com.example.pam_app.adapter.BucketEntryAdapter;
import com.example.pam_app.databinding.ActivityBucketBinding;
import com.example.pam_app.db.WallyDatabase;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.presenter.BucketPresenter;
import com.example.pam_app.repository.BucketMapper;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.RoomBucketRepository;
import com.example.pam_app.view.BucketView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class BucketActivity extends AppCompatActivity implements BucketView {

    private BucketPresenter bucketPresenter;
    private ActivityBucketBinding binding;

    private boolean readExternalStorage;

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);

        int id = Integer.parseInt(getIntent().getData().getQueryParameter("id"));
        BucketRepository bucketRepository = new RoomBucketRepository(
                WallyDatabase.getInstance(getApplicationContext()).bucketDao(),
                new BucketMapper()
            );
        bucketPresenter = new BucketPresenter(id, this, bucketRepository);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_bucket);
        binding.setLifecycleOwner(this);

        this.setUpToolBar();
        this.setUpList();
        this.setUpAddEntryButton();

        ActivityResultLauncher<String>  permissionResultLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (!isGranted) {
                        Toast.makeText(getApplicationContext(), "You will not be able to see the buckets pictures", Toast.LENGTH_LONG).show();
                    }
                    readExternalStorage = isGranted;
                });
        permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bucketPresenter.onViewAttach();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bucketPresenter.onViewResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bucketPresenter.onViewPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bucketPresenter.onViewDetached();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id =  item.getItemId();
        if (id == android.R.id.home) {
            bucketPresenter.onBackSelected();
            return true;
        }
        else if (id == R.id.action_delete) {
            bucketPresenter.onDeleteSelected();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bucket_menu, menu);
        return true;
    }

    @Override
    public void bind(Bucket bucket) {
        this.binding.setBucket(bucket);
        drawImage(bucket.imagePath);
    }

    @Override
    public void back() {
        onBackPressed();
    }

    @Override
    public void goToAddEntry() {
        Intent intent = new Intent(this, AddBucketEntryActivity.class);
        startActivity(intent);
    }

    private void drawImage(String imagePath) {
        final AppCompatImageView imageView = findViewById(R.id.image_view);
        boolean renderDefault = true;
        if (imagePath != null && readExternalStorage) {
            try {

                final InputStream imageStream = getContentResolver().openInputStream(Uri.parse(imagePath));
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
                renderDefault = false;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }

        if (renderDefault) {
            imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                            getApplicationContext(),
                            R.drawable.ic_launcher_background
                    )
            );
        }
    }

    private void setUpList() {
        RecyclerView listView = findViewById(R.id.bucket_entries);
        BucketEntryAdapter adapter = new BucketEntryAdapter();
        listView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(listView, false);
    }

    private void setUpToolBar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setUpAddEntryButton() {
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> bucketPresenter.onAddEntryClick());
    }
}
