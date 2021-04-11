package com.example.pam_app.activity.bucket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_app.R;
import com.example.pam_app.activity.data_bind.BindableAdapter;
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
        return new BucketEntryHolder(inflater.inflate(R.layout.activity_bucket_entry, parent, false));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(BucketEntryHolder holder, final int position) {
        holder.bind(items.get(position).toString());
    }

    public static class BucketEntryHolder extends RecyclerView.ViewHolder {

        private final View itemView;

        public BucketEntryHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public void bind(String text) {
            ((TextView) this.itemView.findViewById(R.id.textView)).setText(text);
        }
    }
}
