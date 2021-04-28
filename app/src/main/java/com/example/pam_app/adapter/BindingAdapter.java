package com.example.pam_app.adapter;

import androidx.recyclerview.widget.RecyclerView;

public class BindingAdapter {
    @androidx.databinding.BindingAdapter("data")
    public static <T> void setRecyclerViewProperties(RecyclerView recyclerView, T data) {
        if (recyclerView.getAdapter() instanceof BindableAdapter) {
            ((BindableAdapter) recyclerView.getAdapter()).setData(data);
        }
    }
}
