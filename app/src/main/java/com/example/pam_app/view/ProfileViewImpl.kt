package com.example.pam_app.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.pam_app.R
import com.example.pam_app.presenter.ProfilePresenter

class ProfileViewImpl @kotlin.jvm.JvmOverloads constructor(context: Context?, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attributeSet, defStyleAttr), ProfileView {
    private val languageRadioGroup: RadioGroup
    private lateinit var onApplyChangesClickedListener: (String) -> Unit
    private val presenter: ProfilePresenter
    private var currentLanguage: String? = null
    private fun setUpLanguageRadioButtonGroup() {
        languageRadioGroup.setOnCheckedChangeListener { _: RadioGroup?, checkedId: Int ->
            val button = languageRadioGroup.findViewById<RadioButton>(checkedId)
            when (languageRadioGroup.indexOfChild(button)) {
                0 -> currentLanguage = "es"
                1 -> currentLanguage = "en"
            }
        }
    }

    override fun bind(currentLanguage: String?, applyChanges: (String) -> Unit) {
        setUpLanguageRadioButtonGroup()
        setUpApplyChangesButton()
        onApplyChangesClickedListener = applyChanges
        this.currentLanguage = currentLanguage
    }

    override fun applyChanges() {
        onApplyChangesClickedListener(currentLanguage!!)
    }

    private fun setUpApplyChangesButton() {
        val applyChangesButton = findViewById<Button>(R.id.apply_changes_button)
        applyChangesButton.setOnClickListener { presenter.onApplyChangesClicked() }
    }

    init {
        View.inflate(context, R.layout.view_profile, this)
        gravity = Gravity.CENTER_HORIZONTAL
        orientation = VERTICAL
        languageRadioGroup = findViewById(R.id.language)
        presenter = ProfilePresenter(this)
    }
}