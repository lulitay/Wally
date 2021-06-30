package com.example.pam_app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.pam_app.R
import com.example.pam_app.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class AddBucketEntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)
        val uri = intent.data
        var defaultBucketName: String? = null
        var defaultBucketType = 0
        if (uri != null) {
            defaultBucketName = uri.getQueryParameter("bucket_name")
            defaultBucketType = uri.getQueryParameter("bucket_type")?.toInt()!!
        }
        setUpToolbar()
        setUpTabs(defaultBucketName, defaultBucketType)
    }

    private fun setUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        toolbar.title = null
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close)
        }
    }

    private fun setUpTabs(defaultBucketName: String?, defaultBucketType: Int) {
        val viewPagerAdapter = ViewPagerAdapter(this, defaultBucketName!!, defaultBucketType)
        val tabLayout = findViewById<TabLayout>(R.id.add_entry_tabs)
        val viewPager = findViewById<ViewPager2>(R.id.add_entry_view_pager)
        val titles: Map<Int, String> = object : HashMap<Int, String>() {
            init {
                put(0, getString(R.string.spending))
                put(1, getString(R.string.saving))
                put(2, getString(R.string.income))
            }
        }
        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab: TabLayout.Tab, position: Int -> tab.text = titles[position] }.attach()
        val tab = tabLayout.getTabAt(defaultBucketType)
        tab!!.select()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}