package com.example.pam_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_app.R;
import com.example.pam_app.model.BucketEntry;

import java.util.ArrayList;
import java.util.List;

public class BucketEntryHomeAdapter extends RecyclerView.Adapter<BucketEntryHomeAdapter.BucketEntryViewHolder> {

    private final List<BucketEntry> bucketEntryList;

    public BucketEntryHomeAdapter() {
        this.bucketEntryList = new ArrayList<>();
    }

    @NonNull
    @Override
    public BucketEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bucket_entry_home_item, parent, false);
        return new BucketEntryViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull BucketEntryViewHolder holder, int position) {
        holder.bind(bucketEntryList.get(position));
    }

    @Override
    public int getItemCount() {
        return bucketEntryList.size();
    }

    public void update(final List<BucketEntry> newBucketEntryList) {
        bucketEntryList.clear();
        if (newBucketEntryList != null) {
            bucketEntryList.addAll(newBucketEntryList);
        }
        notifyDataSetChanged();
    }

    public static class BucketEntryViewHolder extends RecyclerView.ViewHolder {

        public BucketEntryViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(final BucketEntry entry) {
            final TextView comment = itemView.findViewById(R.id.comment);
            final TextView amount = itemView.findViewById(R.id.amount);
            final TextView date = itemView.findViewById(R.id.date);

            comment.setText(entry.getComment());
            amount.setText("$" + entry.getAmountString());
            date.setText(entry.getDateString());
        }
    }
}
