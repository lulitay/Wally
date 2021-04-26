package com.example.pam_app.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pam_app.R;
import com.example.pam_app.model.Bucket;

import java.util.ArrayList;
import java.util.List;

public class BucketListAdapter extends ArrayAdapter<Bucket> {
    private final Activity context;
    private final List<Bucket> bucketList = new ArrayList<>();

    static class ViewHolder {
        public TextView title;
        public TextView target;
    }

    public BucketListAdapter(Activity context) {
        super(context, R.layout.bucket_list_item);
        this.context = context;

    }

    public void update(final List<Bucket> newBucketList) {
        bucketList.clear();
        if (newBucketList != null) {
            bucketList.addAll(newBucketList);
        }

        super.addAll(bucketList);

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.bucket_list_item, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) rowView.findViewById(R.id.bucket_title);
            viewHolder.target = (TextView) rowView.findViewById(R.id.bucket_target);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        Bucket currentBucket = bucketList.get(position);
        holder.title.setText(currentBucket.title);
        holder.target.setText(String.valueOf(currentBucket.target));
        return rowView;
    }
}
