package com.example.pam_app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_app.R;
import com.example.pam_app.adapter.BucketEntryAdapter;
import com.example.pam_app.databinding.ActivityBucketBinding;
import com.example.pam_app.db.WallyDatabase;
import com.example.pam_app.repository.BucketMapper;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.RoomBucketRepository;
;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BucketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        ActivityBucketBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_bucket);

        BucketRepository bucketRepository = new RoomBucketRepository(
                WallyDatabase.getInstance(getApplicationContext()).bucketDao(),
                new BucketMapper()
            );

        int id = Integer.parseInt(getIntent().getData().getQueryParameter("id"));

        Disposable disposable = bucketRepository.get(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(binding::setBucket);

        RecyclerView listView = findViewById(R.id.bucket_entries);
        BucketEntryAdapter adapter = new BucketEntryAdapter();
        listView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(listView, false);
    }
}
