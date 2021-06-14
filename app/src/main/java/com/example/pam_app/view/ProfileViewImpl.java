package com.example.pam_app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.example.pam_app.R;
import com.example.pam_app.presenter.ProfilePresenter;
import com.example.pam_app.repository.LanguagesRepository;
import com.example.pam_app.utils.listener.ClickableTarget;

import static android.view.Gravity.CENTER_HORIZONTAL;

public class ProfileViewImpl extends LinearLayout implements ProfileView {

    private final RadioGroup languageRadioGroup;
    private LanguagesRepository languagesRepository;
    private ClickableTarget<String> onApplyChangesClickedListener;
    private final ProfilePresenter presenter;
    private String currentLanguage;

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
        presenter = new ProfilePresenter(this);
    }

    private void setUpLanguageRadioButtonGroup() {
        languageRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            final RadioButton button = languageRadioGroup.findViewById(checkedId);
            final int index = languageRadioGroup.indexOfChild(button);
            switch (index) {
                case 0:
                    currentLanguage = "es";
                    break;
                case 1:
                    currentLanguage = "en";
                    break;
            }
        });
    }

    @Override
    public void bind(final LanguagesRepository languagesRepository, ClickableTarget<String> applyChanges) {
        this.languagesRepository = languagesRepository;
        setUpLanguageRadioButtonGroup();
        setUpApplyChangesButton();
        this.onApplyChangesClickedListener = applyChanges;
        currentLanguage = languagesRepository.getCurrentLocale().toString();
    }

    @Override
    public void applyChanges() {
        onApplyChangesClickedListener.onClick(currentLanguage);
    }

    private void setUpApplyChangesButton() {
        final Button applyChangesButton = findViewById(R.id.apply_changes_button);
        applyChangesButton.setOnClickListener(v -> presenter.onApplyChangesClicked());
    }
}
