package com.opentmn.opentmn.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.opentmn.opentmn.utils.FontHelper;

import сom.opentmn.opentmn.R;

/**
 * Created by kost on 12.01.17.
 */

public class EditText extends android.widget.EditText {

    public EditText(Context context) {
        super(context);
    }

    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        setupFontFromAttrs(context, attrs);

    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

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
