package com.example.pam_app.activity;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.pam_app.R;
import com.example.pam_app.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.HashMap;
import java.util.Map;

public class AddBucketEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        Uri uri = getIntent().getData();
        String defaultBucketName = null;
        int defaultBucketType = 0;
        if(uri != null) {
            defaultBucketName = uri.getQueryParameter("bucket_name");
            defaultBucketType = Integer.parseInt(uri.getQueryParameter("bucket_type"));
        }

        setUpToolbar();
        setUpTabs(defaultBucketName, defaultBucketType);
    }

    private void setUpToolbar() {
        final Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        toolbar.setTitle(null);
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        }
    }

    private void setUpTabs(String defaultBucketName, int defaultBucketType) {
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, defaultBucketName, defaultBucketType);
        final TabLayout tabLayout = findViewById(R.id.add_entry_tabs);
        final ViewPager2 viewPager = findViewById(R.id.add_entry_view_pager);
        final Map<Integer, String> titles = new HashMap<Integer, String>() {
            {
                put(0, getString(R.string.spending));
                put(1, getString(R.string.saving));
                put(2, getString(R.string.income));
            }
        };

        viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titles.get(position))
        ).attach();
        TabLayout.Tab tab = tabLayout.getTabAt(defaultBucketType);
        tab.select();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
