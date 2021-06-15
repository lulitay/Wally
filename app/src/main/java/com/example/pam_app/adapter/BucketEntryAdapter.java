package com.example.pam_app.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_app.databinding.ActivityBucketEntryBinding;
import com.example.pam_app.model.BucketEntry;

import java.util.List;

public class BucketEntryAdapter extends RecyclerView.Adapter<BucketEntryAdapter.BucketEntryHolder>
        implements BindableAdapter<List<BucketEntry>> {

    private List<BucketEntry> items;

    @Override
    public void setData(final List<BucketEntry> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BucketEntryHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new BucketEntryHolder(ActivityBucketEntryBinding.inflate(inflater, parent, false));
    }

    @Override
    public int getItemCount() {
        return (items == null) ? 0 : items.size();
    }

    @Override
    public void onBindViewHolder(final BucketEntryHolder holder, final int position) {
        holder.bind(items.get(position));
    }

    public static class BucketEntryHolder extends RecyclerView.ViewHolder {

        private final ActivityBucketEntryBinding binding;

        public BucketEntryHolder(final ActivityBucketEntryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final BucketEntry entry) {
            this.binding.setEntry(entry);
        }
    }
}