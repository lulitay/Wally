package com.example.pam_app.view;

import com.example.pam_app.utils.listener.Clickable;
import com.example.pam_app.model.BucketEntry;

import java.util.List;

public interface HomeView {

    void bind();
    void setClickable(final Clickable listener);
    void onViewStopped();
    void onViewResumed();
    void onViewPaused();
    void showEntries(final List<BucketEntry> entries);
}
