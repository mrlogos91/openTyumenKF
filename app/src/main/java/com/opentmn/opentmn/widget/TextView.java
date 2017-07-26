package com.opentmn.opentmn.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.opentmn.opentmn.utils.FontHelper;

import сom.opentmn.opentmn.R;


public class TextView extends android.widget.TextView {

    public TextView(final Context context) {
        super(context);
    }

    public TextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        setupFontFromAttrs(context, attrs);
    }

    public TextView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        setupFontFromAttrs(context, attrs);
    }

    private void setupFontFromAttrs(Context context, AttributeSet attrs) {
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.com_opentmn_opentmn_widget_TextView);
        if (array != null) {
            final String typefaceAssetPath = array.getString(R.styleable.com_opentmn_opentmn_widget_TextView_customTypeface);
            FontHelper.setFont(this, typefaceAssetPath);
            array.recycle();
        }
    }

}
