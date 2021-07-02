package com.example.pam_app.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.pam_app.R
import com.example.pam_app.di.ContainerLocator
import com.example.pam_app.model.BucketEntry
import com.example.pam_app.presenter.AddBucketEntryPresenter
import com.example.pam_app.view.AddBucketEntryView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import java.util.*

abstract class AddBucketEntryFragment : Fragment(), AddBucketEntryView {
    private var presenter: AddBucketEntryPresenter? = null
    private var createdView: View? = null
    private var description: EditText? = null
    private var amount: EditText? = null
    private var selectedDate: EditText? = null
    private var date: Calendar? = null
    private var bucket: AutoCompleteTextView? = null
    private var dropdown: TextInputLayout? = null
    private var datePicker: MaterialDatePicker<Long>? = null
    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View? {
        val container = ContainerLocator.locateComponent(context)
        presenter = AddBucketEntryPresenter(this, container?.bucketRepository,
                container?.schedulerProvider)
        return super.onCreateView(inflater, viewGroup, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createdView = view
        presenter!!.onViewAttached()
        description = view.findViewById(R.id.description)
        amount = view.findViewById(R.id.amount)
        selectedDate = view.findViewById(R.id.date)
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        date = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        bucket = view.findViewById(R.id.bucket)
        datePicker = MaterialDatePicker.Builder.datePicker().setTitleText(getString(R.string.pick_a_date)).build()
        dropdown = view.findViewById(R.id.bucket_dropdown)
        bucket?.setText(requireArguments().getString("bucket_name"), false)
        setDatePicker()
        setSaveEntryListener()
    }

    override fun setDropDownOptions(buckets: List<String?>?) {
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, buckets!!)
        val editTextFilledExposedDropdown = createdView!!.findViewById<AutoCompleteTextView>(R.id.bucket)
        editTextFilledExposedDropdown.setAdapter(adapter)
    }

    override fun onErrorSavingBucketEntry() {
        Toast.makeText(
                context,
                getString(R.string.entry_saving_failed),
                Toast.LENGTH_LONG
        ).show()
    }

    override fun onSuccessSavingBucketEntry(bucketEntry: BucketEntry) {
        Toast.makeText(
                context,
                getString(R.string.entry_saving_success, bucketEntry.comment),
                Toast.LENGTH_LONG
        ).show()
        val result = Intent()
        result.putExtra("entry", bucketEntry)
        requireActivity().setResult(Activity.RESULT_OK, result)
        requireActivity().finish()
    }

    override fun onStop() {
        super.onStop()
        presenter!!.onViewDetached()
    }

    override fun showDescriptionError(error: Int, parameter: Int?) {
        if (parameter == null) {
            description!!.error = getString(error)
        } else {
            description!!.error = getString(error, parameter)
        }
    }

    override fun showAmountError(error: Int, parameter: Int?) {
        if (parameter == null) {
            amount!!.error = getString(error)
        } else {
            amount!!.error = getString(error, parameter)
        }
    }

    override fun showDateError(error: Int) {
        selectedDate!!.requestFocus()
        parentFragmentManager.beginTransaction().remove(datePicker!!).commit()
        selectedDate!!.error = getString(error)
    }

    override fun showBucketTitleError(error: Int) {
        dropdown!!.error = getString(error)
    }

    private fun setSaveEntryListener() {
        val saveEntry = createdView!!.findViewById<Button>(R.id.save)
        saveEntry.setOnClickListener {
            presenter!!.saveBucketEntry(
                    amount!!.text.toString(),
                    if (selectedDate!!.text.toString() == "") null else date!!.time,
                    description!!.text.toString(),
                    bucket!!.text.toString()
            )
        }
    }

    private fun setDatePicker() {
        datePicker!!.addOnPositiveButtonClickListener { selection: Long? ->
            selectedDate!!.setText(datePicker!!.headerText)
            date!!.timeInMillis = selection!!
        }
        selectedDate!!.onFocusChangeListener = OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (hasFocus && !datePicker!!.isAdded) {
                datePicker!!.show(parentFragmentManager, "date_picker")
            }
        }
        selectedDate!!.setOnClickListener {
            if (!datePicker!!.isAdded) {
                datePicker!!.show(parentFragmentManager, "date_picker")
            }
        }
    }

    companion object {
        const val MAX_AMOUNT = 1000000000
        const val MAX_CHARACTERS = 50
    }
}