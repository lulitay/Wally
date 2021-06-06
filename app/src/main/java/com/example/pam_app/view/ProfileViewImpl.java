package com.example.pam_app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.example.pam_app.R;
import com.example.pam_app.repository.LanguagesRepository;

import static android.view.Gravity.CENTER_HORIZONTAL;

public class ProfileViewImpl extends LinearLayout implements ProfileView {

    private final RadioGroup languageRadioGroup;
    private LanguagesRepository languagesRepository;

    public ProfileViewImpl(Context context) {
        this(context, null);
    }

    public ProfileViewImpl(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ProfileViewImpl(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);

        inflate(context, R.layout.view_profile, this);
        setGravity(CENTER_HORIZONTAL);
        setOrientation(VERTICAL);
        languageRadioGroup = findViewById(R.id.language);
    }

    private void setUpLanguageRadioButtonGroup() {
        languageRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            final RadioButton button = languageRadioGroup.findViewById(checkedId);
            final int index = languageRadioGroup.indexOfChild(button);
            switch (index) {
                case 0:
                    languagesRepository.changeLanguage("es");
                    break;
                case 1:
                    languagesRepository.changeLanguage("en");
                    break;
            }
        });
    }

    @Override
    public void bind(final LanguagesRepository languagesRepository) {
        this.languagesRepository = languagesRepository;
        setUpLanguageRadioButtonGroup();
    }
}
