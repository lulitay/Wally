package com.example.pam_app.activity.bucket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_app.R;
import com.example.pam_app.activity.data_bind.BindableAdapter;
import com.example.pam_app.databinding.ActivityBucketEntryBinding;
import com.example.pam_app.model.BucketEntry;

import java.util.List;

public class BucketEntryAdapter extends RecyclerView.Adapter<BucketEntryAdapter.BucketEntryHolder>
        implements BindableAdapter<List<BucketEntry>> {

    private List<BucketEntry> items;

    @Override
    public void setData(List<BucketEntry> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public BucketEntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new BucketEntryHolder(ActivityBucketEntryBinding.inflate(inflater, parent, false));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(BucketEntryHolder holder, final int position) {
        holder.bind(items.get(position));
    }

    public static class BucketEntryHolder extends RecyclerView.ViewHolder {

        private final ActivityBucketEntryBinding binding;

        public BucketEntryHolder(ActivityBucketEntryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(BucketEntry entry) {
            this.binding.setEntry(entry);
        }
    }
}
