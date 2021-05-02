package com.example.pam_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_app.R;
import com.example.pam_app.listener.ClickableWithParameter;
import com.example.pam_app.model.Bucket;

import java.util.ArrayList;
import java.util.List;

public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.BucketViewHolder> {
    private final List<Bucket> bucketList;
    private ClickableWithParameter listener;

    public BucketListAdapter() {
        this.bucketList = new ArrayList<>();
    }

    public void setOnClickListener(final ClickableWithParameter listener) {
        this.listener = listener;
    }

    public void update(final List<Bucket> newBucketList) {
        bucketList.clear();
        if (newBucketList != null) {
            bucketList.addAll(newBucketList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BucketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_list_item, parent, false);
        return new BucketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BucketViewHolder holder, int position) {
        holder.bind(bucketList.get(position));
        holder.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return bucketList.size();
    }

    public static class BucketViewHolder extends RecyclerView.ViewHolder {
        private ClickableWithParameter listener;

        public BucketViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Bucket bucket) {
            final TextView title = itemView.findViewById(R.id.bucket_title);
            final TextView target = itemView.findViewById(R.id.bucket_target);

            title.setText(bucket.title);
            target.setText(String.valueOf(bucket.target));
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClick(bucket.id);
                }
            });
        }

        public void setOnClickListener(ClickableWithParameter listener) {
            this.listener = listener;
        }
    }
}
