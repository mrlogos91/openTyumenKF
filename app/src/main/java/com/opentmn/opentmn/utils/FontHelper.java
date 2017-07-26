package com.opentmn.opentmn.utils;

import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import сom.opentmn.opentmn.R;

/**
 * Created by kost on 03.01.17.
 */

public class FontHelper {

    private static Map<String, Typeface> mTypefaces;

    public static void setFont(TextView textView, String fontPath) {
        if(fontPath != null) {
            if (mTypefaces == null) {
                mTypefaces = new HashMap<String, Typeface>();
            }

            Typeface typeface = null;

            if (mTypefaces.containsKey(fontPath)) {
                typeface = mTypefaces.get(fontPath);
            } else {
                //TODO: context?
                AssetManager assets = textView.getContext().getAssets();
                typeface = Typeface.createFromAsset(assets, fontPath);
                mTypefaces.put(fontPath, typeface);
            }

            textView.setTypeface(typeface);
        }
    }
}
