package com.example.pam_app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pam_app.R

class AddSpendingBucketEntryFragment : AddBucketEntryFragment() {
    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, viewGroup, savedInstanceState)
        return inflater.inflate(R.layout.fragment_add_spending, viewGroup, false)
    }

    override val bucketType: Int
        get() = this.requireArguments().getInt(ARG_OBJECT)

    companion object {
        const val ARG_OBJECT = "object"
        private const val ARG_BUCKET = "bucket_name"
        fun newInstance(counter: Int, defaultBucket: String?): AddSpendingBucketEntryFragment {
            val fragment = AddSpendingBucketEntryFragment()
            val args = Bundle()
            args.putInt(ARG_OBJECT, counter)
            if (defaultBucket != null) {
                args.putString(ARG_BUCKET, defaultBucket)
            }
            fragment.arguments = args
            return fragment
        }
    }
}