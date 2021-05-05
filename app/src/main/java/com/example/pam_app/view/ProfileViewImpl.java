package com.example.pam_app.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.example.pam_app.R;

import static android.view.Gravity.CENTER_HORIZONTAL;
import static com.example.pam_app.MainActivity.KEY_PREF_LANGUAGE;

public class ProfileViewImpl extends LinearLayout implements ProfileView {

    final RadioGroup languageRadioGroup;
    SharedPreferences sharedPreferences;

    public ProfileViewImpl(Context context) {
        this(context, null);
    }

    public ProfileViewImpl(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ProfileViewImpl(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);

        inflate(context, R.layout.profile_view, this);
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
                    sharedPreferences.edit().putString(KEY_PREF_LANGUAGE, "es").apply();
                    break;
                case 1:
                    sharedPreferences.edit().putString(KEY_PREF_LANGUAGE, "en").apply();
                    break;
            }
        });
    }


    @Override
    public void bind(final SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        setUpLanguageRadioButtonGroup();
    }
}
