package com.example.pam_app.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.pam_app.utils.listener.Clickable;
import com.example.pam_app.utils.schedulers.AndroidSchedulerProvider;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.BucketView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

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
        SchedulerProvider schedulerProvider = new AndroidSchedulerProvider();
        bucketPresenter = new BucketPresenter(id, this, bucketRepository, schedulerProvider);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_bucket);
        binding.setLifecycleOwner(this);

        this.setUpToolBar();
        this.setUpList();
        this.setUpAddEntryButton();

        ActivityResultLauncher<String> permissionResultLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (!isGranted) {
                        Context context = getApplicationContext();
                        Toast.makeText(context, context.getString(R.string.warning_bucket), Toast.LENGTH_LONG).show();
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
    public void bind(final Bucket bucket) {
        this.binding.setBucket(bucket);
        final MaterialTextView noEntriesText = findViewById(R.id.entries_unavailable);
        if (bucket.entries.isEmpty()) {
            noEntriesText.setVisibility(View.VISIBLE);
        } else {
            noEntriesText.setVisibility(View.GONE);
        }
        drawImage(bucket.imagePath);
    }

    @Override
    public void back() {
        onBackPressed();
    }

    @Override
    public void goToAddEntry(String bucketName) {
        final String uri = "wally://add_entry/detail?bucket_name=" + bucketName;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    @Override
    public void showGetBucketError() {
        Context context = getApplicationContext();
        Toast.makeText(context, context.getString(R.string.error_get_bucket), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDeleteBucketError() {
        Context context = getApplicationContext();
        Toast.makeText(context, context.getString(R.string.error_delete_bucket), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDeleteBucketSuccess() {
        Context context = getApplicationContext();
        Toast.makeText(context, context.getString(R.string.success_delete_bucket), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSureDialog(Clickable clickable) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    clickable.onClick();
                    dialog.dismiss();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        };
        Context context = getApplicationContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(BucketActivity.this);
        builder.setMessage(context.getString(R.string.are_you_sure))
                .setPositiveButton(context.getString(R.string.yes), dialogClickListener)
                .setNegativeButton(context.getString(R.string.no), dialogClickListener)
                .show();
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
                Context context = getApplicationContext();
                Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_LONG).show();
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
