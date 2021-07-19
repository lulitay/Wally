package com.example.pam_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pam_app.adapter.BucketEntryAdapter.BucketEntryHolder
import com.example.pam_app.databinding.ActivityBucketEntryBinding
import com.example.pam_app.model.Entry
import java.util.*

class BucketEntryAdapter<T : Entry?> : RecyclerView.Adapter<BucketEntryHolder>(), BindableAdapter<Collection<T>?> {
    private var items: LinkedList<T>? = null

    override fun setData(items: Collection<T>?) {
        this.items = LinkedList()
        this.items?.addAll(items!!)
        notifyDataSetChanged()
    }

    fun showNewBucket(entry: T) {
        if (items == null) {
            items = LinkedList()
        }
        items?.add(entry)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BucketEntryHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BucketEntryHolder(ActivityBucketEntryBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: BucketEntryHolder, position: Int) {
        holder.bind(items!![position])
    }

    class BucketEntryHolder(private val binding: ActivityBucketEntryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: Entry?) {
            binding.entry = entry
        }

    }
}