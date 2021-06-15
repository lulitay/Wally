package com.example.pam_app.view;

import com.example.pam_app.repository.LanguagesRepository;
import com.example.pam_app.utils.listener.ClickableTarget;

public interface ProfileView {

    void bind(final LanguagesRepository languagesRepository, ClickableTarget<String> applyChanges);
    void applyChanges();
}
