package com.example.testapp;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class ExpandableTextView extends AppCompatTextView {
    private boolean isExpanded = false;

    public ExpandableTextView(Context context) {
        super(context);
        initialize();
    }

    public ExpandableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ExpandableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        setOnClickListener(v -> toggle());
    }

    public void toggle() {
        isExpanded = !isExpanded;
        setMaxLines(isExpanded ? Integer.MAX_VALUE : 3); // Change the number of lines to expand or collapse
    }
}

