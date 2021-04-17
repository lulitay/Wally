package com.example.pam_app.view;

import java.util.List;

public interface AddBucketEntryView {

    int getBucketType();
    void setDropDownOptions(final List<String> buckets);
}
