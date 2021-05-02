package com.example.pam_app.utils;

import com.example.pam_app.model.BucketEntry;

import java.util.Comparator;

public class BucketEntryComparator implements Comparator<BucketEntry> {

    @Override
    public int compare(final BucketEntry o1, final BucketEntry o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
}
