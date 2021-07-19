package com.example.pam_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pam_app.R
import com.example.pam_app.adapter.BucketListAdapter.BucketViewHolder
import com.example.pam_app.model.Bucket
import com.example.pam_app.utils.listener.ClickableTarget
import java.util.*
import java.util.stream.Collectors

class BucketListAdapter : RecyclerView.Adapter<BucketViewHolder>() {
    private var bucketList: MutableList<Bucket?>
    private var listener: ClickableTarget<Int>? = null
    fun setOnClickListener(listener: ClickableTarget<Int>?) {
        this.listener = listener
    }

    fun update(newBucketList: List<Bucket?>?) {
        bucketList.clear()
        if (newBucketList != null) {
            bucketList.addAll(newBucketList)
            notifyDataSetChanged()
        }
    }

    fun showNewBucket(bucket: Bucket?) {
        if (bucket != null) {
            bucketList.add(bucket)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BucketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bucket_list_item, parent, false)
        return BucketViewHolder(view)
    }

    override fun onBindViewHolder(holder: BucketViewHolder, position: Int) {
        holder.bind(bucketList[position])
        holder.setOnClickListener(listener)
    }

    override fun getItemCount(): Int {
        return bucketList.size
    }

    fun delete(id: Int) {
        bucketList = bucketList.stream().filter { b: Bucket? -> b!!.id != id }.collect(Collectors.toList())
        notifyDataSetChanged()
    }

    class BucketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var listener: ClickableTarget<Int>? = null
        fun bind(bucket: Bucket?) {
            val title = itemView.findViewById<TextView>(R.id.bucket_title)
            val target = itemView.findViewById<TextView>(R.id.bucket_target)
            title.text = bucket!!.title
            target.text = StringBuilder().append("$").append(bucket.target)
            itemView.setOnClickListener {
                if (listener != null) {
                    listener!!.onClick(bucket.id!!)
                }
            }
        }

        fun setOnClickListener(listener: ClickableTarget<Int>?) {
            this.listener = listener
        }
    }

    init {
        bucketList = ArrayList()
    }
}