package com.example.pam_app.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
object BindingAdapter {
    @kotlin.jvm.JvmStatic
    @BindingAdapter("data")
    fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, items: List<T>?) {
        if (recyclerView.adapter is BindableAdapter<*>) {
            (recyclerView.adapter as BindableAdapter<Collection<T>>?)!!.setData(items)
        }
    }
}