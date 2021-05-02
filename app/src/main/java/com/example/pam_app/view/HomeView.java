package com.example.pam_app.view;

import com.example.pam_app.listener.OnAddBucketClickedListener;

public interface HomeView {

    void bind();
    void setOnAddBucketClickedListener(final OnAddBucketClickedListener listener);

}
