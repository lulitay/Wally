package com.example.pam_app.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pam_app.R
import com.example.pam_app.di.ContainerLocator
import com.example.pam_app.model.Income
import com.example.pam_app.presenter.AddIncomePresenter
import com.example.pam_app.view.AddIncomeView
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class AddIncomeFragment : Fragment(), AddIncomeView {
    private var createdView: View? = null
    private var description: EditText? = null
    private var amount: EditText? = null
    private var selectedDate: EditText? = null
    private var date: Calendar? = null
    private var datePicker: MaterialDatePicker<Long>? = null
    private var presenter: AddIncomePresenter? = null
    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, viewGroup, savedInstanceState)
        val container = ContainerLocator.locateComponent(context)
        presenter = AddIncomePresenter(this, container?.incomeRepository,
                container?.schedulerProvider)
        date = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        return inflater.inflate(R.layout.fragment_add_income, viewGroup, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createdView = view
        description = view.findViewById(R.id.description)
        amount = view.findViewById(R.id.amount)
        selectedDate = view.findViewById(R.id.date)
        date = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        datePicker = MaterialDatePicker.Builder.datePicker().setTitleText(getString(R.string.pick_a_date)).build()
        setDatePicker()
        setSaveIncomeListener()
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

    private fun setSaveIncomeListener() {
        val saveIncome = createdView!!.findViewById<Button>(R.id.save)
        saveIncome.setOnClickListener {
            presenter!!.saveIncome(
                    description!!.text.toString(),
                    amount!!.text.toString(),
                    date!!.time
            )
        }
    }

    override fun onErrorSavingIncome() {
        Toast.makeText(
                context,
                getString(R.string.error_saving_income),
                Toast.LENGTH_LONG
        ).show()
    }

    override fun onSuccessSavingIncome(income: Income) {
        Toast.makeText(
                context,
                getString(R.string.income_saving_success, income.comment),
                Toast.LENGTH_LONG
        ).show()
        val result = Intent()
        result.putExtra("income", income)
        requireActivity().setResult(Activity.RESULT_OK, result)
        requireActivity().finish()
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

    override fun onStop() {
        super.onStop()
        presenter!!.onViewDetached()
    }

    companion object {
        private const val ARG_OBJECT = "object"
        private const val ARG_BUCKET = "bucket_name"
        fun newInstance(counter: Int?, defaultBucket: String?): AddIncomeFragment {
            val fragment = AddIncomeFragment()
            val args = Bundle()
            args.putInt(ARG_OBJECT, counter!!)
            if (defaultBucket != null) {
                args.putString(ARG_BUCKET, defaultBucket)
            }
            fragment.arguments = args
            return fragment
        }
    }
}