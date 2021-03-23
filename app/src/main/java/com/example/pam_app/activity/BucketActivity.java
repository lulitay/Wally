package com.example.pam_app.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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
        Bucket bucket = new Bucket("Titulo", new Date(new Date().getTime() + 24 * 60 * 60 * 1000),
                BucketType.SPENDING, 2000, entries);
        binding.setBucket(bucket);
    }
}
