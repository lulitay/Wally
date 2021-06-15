package com.example.pam_app.view;

import com.example.pam_app.utils.listener.ClickableTarget;

public interface ProfileView {

    void bind(final String currentLanguage, ClickableTarget<String> applyChanges);
    void applyChanges();
}
