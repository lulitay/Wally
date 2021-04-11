package com.example.pam_app.activity.data_bind;

import androidx.recyclerview.widget.RecyclerView;

public class BindingAdapter {
    @androidx.databinding.BindingAdapter("data")
    public static <T> void setRecyclerViewProperties(RecyclerView recyclerView, T data) {
        if (recyclerView.getAdapter() instanceof BindableAdapter) {
            ((BindableAdapter) recyclerView.getAdapter()).setData(data);
        }
    }
}
