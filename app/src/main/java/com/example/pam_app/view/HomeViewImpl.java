package com.example.pam_app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.pam_app.R;
import com.example.pam_app.listener.OnAddBucketClickedListener;

import static android.view.Gravity.CENTER;

public class HomeViewImpl extends LinearLayout implements HomeView {

    private final TextView textView;

    //private final ImageView imageView;
    private final Button addBucketButton;
    private OnAddBucketClickedListener onAddBucketClickedListener;

    public HomeViewImpl(Context context) {
        this(context, null);
    }

    public HomeViewImpl(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public HomeViewImpl(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);

        inflate(context, R.layout.home_view, this);
        setGravity(CENTER);
        setOrientation(VERTICAL);

        textView = findViewById(R.id.welcome);
        //imageView = findViewById(R.id.logo);
        addBucketButton = findViewById(R.id.add_bucket);
    }

    @Override
    public void bind() {
        addBucketButton.setOnClickListener(v -> {
            if (onAddBucketClickedListener != null) {
                onAddBucketClickedListener.onClick();
            }
        });
    }

    @Override
    public void setOnAddBucketClickedListener(final OnAddBucketClickedListener listener) {
        onAddBucketClickedListener = listener;
    }
}
