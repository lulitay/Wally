package com.example.pam_app.activity.bucket;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_app.R;
import com.example.pam_app.databinding.ActivityBucketBinding;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.model.BucketType;

import java.util.ArrayList;
import java.util.Date;

public class BucketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        ActivityBucketBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_bucket);
        ArrayList<BucketEntry> entries = new ArrayList<>();
        entries.add(new BucketEntry(200, new Date(), "Algo"));
        entries.add(new BucketEntry(-100, new Date(), "Algo2"));
        entries.add(new BucketEntry(-100, new Date(), "Algo2"));
        entries.add(new BucketEntry(200, new Date(), "Algo"));
        entries.add(new BucketEntry(200, new Date(), "Algo"));
        entries.add(new BucketEntry(200, new Date(), "Algo"));
        entries.add(new BucketEntry(-100, new Date(), "Algo2"));
        entries.add(new BucketEntry(-100, new Date(), "Algo2"));
        entries.add(new BucketEntry(-100, new Date(), "Algo2"));
        entries.add(new BucketEntry(-100, new Date(), "Algo2"));
        entries.add(new BucketEntry(-100, new Date(), "Algo2"));
        entries.add(new BucketEntry(-100, new Date(), "Algo2"));
        entries.add(new BucketEntry(-100, new Date(), "Algo2"));
        entries.add(new BucketEntry(-100, new Date(), "Algo2"));

        Bucket bucket = new Bucket("Titulo", new Date(new Date().getTime() + 24 * 60 * 60 * 1000),
                BucketType.SPENDING, 2000, entries);
        binding.setBucket(bucket);

        RecyclerView listView = findViewById(R.id.bucket_entries);
        BucketEntryAdapter adapter = new BucketEntryAdapter();
        listView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(listView, false);
    }
}
