package com.example.pam_app.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pam_app.R
import com.example.pam_app.adapter.BucketEntryAdapter
import com.example.pam_app.databinding.ActivityBucketBinding
import com.example.pam_app.di.ContainerLocator
import com.example.pam_app.model.Bucket
import com.example.pam_app.model.Entry
import com.example.pam_app.presenter.BucketPresenter
import com.example.pam_app.utils.contracts.BucketContract
import com.example.pam_app.utils.contracts.EntryContract
import com.example.pam_app.view.BucketView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.FileNotFoundException
import java.io.Serializable
import java.util.*

class BucketActivity : AppCompatActivity(), BucketView {
    private var bucketPresenter: BucketPresenter? = null
    private var binding: ActivityBucketBinding? = null
    private var readExternalStorage = false
    private var addBucketEntryResultLauncher: ActivityResultLauncher<String>? = null
    private var addBucketResultLauncher: ActivityResultLauncher<String?>? = null
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        val id: Int = intent.data!!.getQueryParameter("id")?.toInt()!!
        val container = ContainerLocator.locateComponent(this)
        bucketPresenter = BucketPresenter(id, this, container?.bucketRepository!!, container.schedulerProvider!!)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bucket)
        binding?.lifecycleOwner = this
        setUpToolBar()
        setUpList()
        setUpAddEntryButton()
        setUpAddEntryResultLauncher()
        setUpAddBucketResultLauncher()
        readExternalStorage = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    override fun onStart() {
        super.onStart()
        bucketPresenter!!.onViewAttach()
    }

    override fun onStop() {
        super.onStop()
        bucketPresenter!!.onViewDetached()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            bucketPresenter!!.onBackSelected()
            return true
        } else if (id == R.id.action_delete) {
            bucketPresenter!!.onDeleteSelected()
            return true
        } else if (id == R.id.action_edit) {
            bucketPresenter!!.onEditSelected()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun goToAddBucket(bucketId: Int) {
        addBucketResultLauncher!!.launch("bucket_id=$bucketId")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bucket_menu, menu)
        return true
    }

    override fun bind(bucket: Bucket?) {
        binding!!.bucket = bucket
        drawImage(bucket?.imagePath)
    }

    override fun back(entries: Serializable?, id: Int?) {
        val result = Intent()
        result.putExtra("entries", entries)
        result.putExtra("id", id)
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    override fun onBackPressed() {
        bucketPresenter!!.onBackSelected()
    }

    override fun goToAddEntry(bucketName: String?, bucketType: Int) {
        addBucketEntryResultLauncher!!.launch("bucket_name=$bucketName&bucket_type=$bucketType")
    }

    override fun showGetBucketError() {
        val context = applicationContext
        Toast.makeText(context, getString(R.string.error_get_bucket), Toast.LENGTH_LONG).show()
    }

    override fun showDeleteBucketError() {
        val context = applicationContext
        Toast.makeText(context, getString(R.string.error_delete_bucket), Toast.LENGTH_LONG).show()
    }

    override fun showDeleteBucketSuccess() {
        val context = applicationContext
        Toast.makeText(context, getString(R.string.success_delete_bucket), Toast.LENGTH_LONG).show()
    }

    override fun showSureDialog(clickable: () -> Unit) {
        val dialogClickListener = DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    clickable()
                    dialog.dismiss()
                }
                DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
            }
        }
        val builder = AlertDialog.Builder(this@BucketActivity)
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener)
                .show()
    }

    private fun drawImage(imagePath: String?) {
        val imageView = findViewById<AppCompatImageView>(R.id.image_view)
        var renderDefault = true
        if (imagePath != null && readExternalStorage) {
            try {
                val imageStream = contentResolver.openInputStream(Uri.parse(imagePath))
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                imageView.setImageBitmap(selectedImage)
                renderDefault = false
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                val context = applicationContext
                Toast.makeText(context, getString(R.string.error), Toast.LENGTH_LONG).show()
            }
        }
        if (renderDefault) {
            imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_launcher_background
                    )
            )
        }
    }

    private fun setUpList() {
        var listView = findViewById<RecyclerView>(R.id.bucket_entries)
        var adapter: BucketEntryAdapter<*> = BucketEntryAdapter<Entry>()
        listView.adapter = adapter
        ViewCompat.setNestedScrollingEnabled(listView, false)
        listView = findViewById(R.id.bucket_entries_old)
        adapter = BucketEntryAdapter<Entry>()
        listView.adapter = adapter
        ViewCompat.setNestedScrollingEnabled(listView, false)
    }

    private fun setUpToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        val appBar = findViewById<AppBarLayout>(R.id.app_bar)
        val time = findViewById<TextView>(R.id.time)
        appBar.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout: AppBarLayout, verticalOffset: Int -> time.alpha = 1.0f - Math.abs(verticalOffset / appBarLayout.totalScrollRange.toFloat()) })
    }

    private fun setUpAddEntryButton() {
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { bucketPresenter!!.onAddEntryClick() }
    }

    private fun setUpAddEntryResultLauncher() {
        addBucketEntryResultLauncher = registerForActivityResult(
                EntryContract()) { entry: Serializable? -> bucketPresenter!!.onAddEntry(entry) }
    }

    private fun setUpAddBucketResultLauncher() {
        addBucketResultLauncher = registerForActivityResult(
                BucketContract()) {bucket: Bucket? -> bind(bucket)}
    }

}