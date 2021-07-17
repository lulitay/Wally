package com.example.pam_app.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import com.example.pam_app.R
import com.example.pam_app.di.ContainerLocator
import com.example.pam_app.model.Bucket
import com.example.pam_app.model.BucketType
import com.example.pam_app.presenter.AddBucketPresenter
import com.example.pam_app.utils.contracts.GalleryContract
import com.example.pam_app.view.AddBucketView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputLayout
import java.io.FileNotFoundException
import java.util.*

class AddBucketActivity : AppCompatActivity(), AddBucketView, OnRequestPermissionsResultCallback {
    private var presenter: AddBucketPresenter? = null
    private var imageView: ImageView? = null
    private var imagePath: String? = null
    private var date: Calendar? = null
    private var title: EditText? = null
    private var dueDate: EditText? = null
    private var bucketType: AutoCompleteTextView? = null
    private var target: EditText? = null
    private var datePicker: MaterialDatePicker<Long>? = null
    private var dropdown: TextInputLayout? = null
    private var isRecurrent: SwitchMaterial? = null
    private var galleryResultLauncher: ActivityResultLauncher<String>? = null
    private var defaultBucketId: Int? = null
    private var updateBucket = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bucket)
        val uri = intent.data
        if (uri != null) {
            defaultBucketId = uri.getQueryParameter("bucket_id")?.toInt()
            updateBucket = true
        }
        val container = ContainerLocator.locateComponent(this)
        presenter = AddBucketPresenter(this, container!!.bucketRepository!!,
                container.schedulerProvider!!)
        val loadImage = findViewById<Button>(R.id.button_load_image)
        loadImage.setOnClickListener { presenter!!.onClickLoadImage() }
        imageView = findViewById(R.id.image_view)
        date = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        registerImageActivityResult()
    }

    override fun onStart() {
        super.onStart()
        title = findViewById(R.id.title)
        dueDate = findViewById(R.id.due_date)
        bucketType = findViewById(R.id.bucket_type)
        target = findViewById(R.id.target)
        dropdown = findViewById(R.id.bucket_type_dropdown)
        datePicker = MaterialDatePicker.Builder.datePicker().setTitleText(getString(R.string.pick_a_date)).build()
        isRecurrent = findViewById(R.id.switch_recurrent_bucket)
        presenter!!.onAttachView()
        if(updateBucket) {
            presenter!!.getDefaultBucket(defaultBucketId!!)
        }
        setDatePicker()
        setSaveBucketListener()
        setBucketTypeValues()
        setIsRecurrentSwitch()
    }

    private fun setIsRecurrentSwitch() {
        isRecurrent!!.setOnClickListener { presenter!!.onIsRecurrentSwitchChange(isRecurrent!!.isChecked) }
    }

    private fun setSaveBucketListener() {
        val saveEntry = findViewById<Button>(R.id.save)
        saveEntry.setOnClickListener {
            if(updateBucket) { presenter!!.updateBucket(
                    title!!.text.toString(),
                    if (dueDate!!.text.toString() == "") null else date!!.time,
                    BucketType.getBucketType(bucketType!!.text.toString())!!,
                    target!!.text.toString(),
                    imagePath,
                    isRecurrent!!.isChecked)
            } else {
            presenter!!.saveBucket(
                    title!!.text.toString(),
                    if (dueDate!!.text.toString() == "") null else date!!.time,
                    BucketType.getBucketType(bucketType!!.text.toString())!!,
                    target!!.text.toString(),
                    imagePath,
                    isRecurrent!!.isChecked)
            }
        }
    }

    override fun showTitleError(error: Int, parameter: Int?) {
        if (parameter == null) {
            title!!.error = getString(error)
        } else {
            title!!.error = getString(error, parameter)
        }
    }

    override fun showTargetError(error: Int, parameter: Int?) {
        if (parameter == null) {
            target!!.error = getString(error)
        } else {
            target!!.error = getString(error, parameter)
        }
    }

    override fun showDateError(error: Int) {
        dueDate!!.requestFocus()
        supportFragmentManager.beginTransaction().remove(datePicker!!).commit()
        dueDate!!.error = getString(error)
    }

    override fun showBucketTypeError(error: Int) {
        dropdown!!.error = getString(error)
    }

    private fun setDatePicker() {
        datePicker!!.addOnPositiveButtonClickListener { selection: Long? ->
            dueDate!!.setText(datePicker!!.headerText)
            date!!.timeInMillis = selection!!
        }
        dueDate!!.onFocusChangeListener = OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (hasFocus && !datePicker!!.isAdded) {
                datePicker!!.show(this@AddBucketActivity.supportFragmentManager, "date_picker")
            }
        }
        dueDate!!.setOnClickListener {
            if (!datePicker!!.isAdded) {
                datePicker!!.show(supportFragmentManager, "date_picker")
            }
        }
    }

    private fun setBucketTypeValues() {
        val bucketTypeResources = ArrayList<String>()
        for (type in BucketType.values()) {
            bucketTypeResources.add(getString(type.stringResource))
        }
        val adapter = ArrayAdapter(
                applicationContext, R.layout.list_item, bucketTypeResources
        )
        bucketType!!.setAdapter(adapter)
    }

    private fun registerImageActivityResult() {
        galleryResultLauncher = registerForActivityResult(
                GalleryContract()
        ) { result: Uri? ->
            if (result != null) {
                try {
                    val imageStream = contentResolver.openInputStream(result)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    imageView!!.setImageBitmap(selectedImage)
                    imagePath = result.toString()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, getString(R.string.error), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onErrorSavingBucket() {
        Toast.makeText(
                applicationContext,
                getString(R.string.error_saving_bucket),
                Toast.LENGTH_LONG
        ).show()
    }

    override fun onErrorExistingBucketName() {
        Toast.makeText(
                applicationContext,
                getString(R.string.error_existing_bucket_name),
                Toast.LENGTH_LONG
        ).show()
    }

    override fun onSuccessSavingBucket(bucket: Bucket?) {
        Toast.makeText(
                applicationContext,
                getString(R.string.bucket_saved, bucket!!.title),
                Toast.LENGTH_LONG
        ).show()
        val result = Intent()
        result.putExtra("bucket", bucket)
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    override fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_EXTERNAL_STORAGE)
    }

    override fun changeDatePickerState(state: Boolean) {
        val dueDate = findViewById<EditText>(R.id.due_date)
        dueDate.isEnabled = state
    }

    override fun onStop() {
        super.onStop()
        presenter!!.onViewDetached()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImage()
            } else {
                Toast.makeText(applicationContext, getString(R.string.warning_bucket), Toast.LENGTH_LONG).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun loadImage() {
        galleryResultLauncher!!.launch("image/*")
    }

    override fun setDefaultValues(bucket: Bucket?) {
        //TODO: add imagepath?
        title?.setText(bucket!!.title)
        title?.isEnabled = !updateBucket
        target?.setText(bucket!!.target.toString())
        bucketType?.setText(getString(bucket!!.bucketType.stringResource), false)
        isRecurrent?.setChecked(bucket!!.isRecurrent)
        date?.timeInMillis = bucket!!.dueDate!!.time
        dueDate?.setText(date?.time.toString())
    }

    companion object {
        private const val REQUEST_EXTERNAL_STORAGE = 0
    }
}