package com.demoproject.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.GridView;

public class ExpandableHeightGridView extends GridView {
    boolean expanded = false;

    public ExpandableHeightGridView(Context context) {
        super(context);
    }

    public ExpandableHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableHeightGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isExpanded()) {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(16777215, Integer.MIN_VALUE));
            getLayoutParams().height = getMeasuredHeight();
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
