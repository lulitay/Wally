package com.example.pam_app.view;

import com.example.pam_app.repository.LanguagesRepository;
import com.example.pam_app.utils.listener.ClickableWithParameter;

public interface ProfileView {

    void bind(final LanguagesRepository languagesRepository, ClickableWithParameter<String> applyChanges);
    void applyChanges();
}
