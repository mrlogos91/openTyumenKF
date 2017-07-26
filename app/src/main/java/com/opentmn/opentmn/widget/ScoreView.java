package com.opentmn.opentmn.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.widget.TextView;

import butterknife.BindView;
import com.opentmn.opentmn.R;

/**
 * Created by kost on 31.12.16.
 */

public class ScoreView extends FrameLayout {

    private TextView mValueTextView;
    private CharSequence[] mSubtitles;
    private TextView mSubtitleTextView;

    public ScoreView(Context context) {
        this(context, null);
    }

    public ScoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.widget_score, this);
        mValueTextView = (TextView) findViewById(R.id.value_text_view);
        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        mSubtitleTextView = (TextView) findViewById(R.id.subtitle_text_view);

        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ScoreView);
        int textColor = array.getColor(R.styleable.ScoreView_textColor, Color.BLACK);
        Drawable image = array.getDrawable(R.styleable.ScoreView_imageSrc);
        String subtitle = array.getString(R.styleable.ScoreView_subtitleText);
        mSubtitles = array.getTextArray(R.styleable.ScoreView_android_entries);
        array.recycle();

        imageView.setImageDrawable(image);
        mSubtitleTextView.setTextColor(textColor);
        mSubtitleTextView.setText(subtitle);
        mValueTextView.setTextColor(textColor);
    }

    public void setValue(int value) {
        mValueTextView.setText(String.valueOf(value));
        if(mSubtitles != null) {
            int mod = value % 10;
            int div = value / 10;
            if(mod == 0 || div == 1)
                mSubtitleTextView.setText(mSubtitles[0]);
            else if(mod == 1)
                mSubtitleTextView.setText(mSubtitles[1]);
            else if(mod >= 2 && mod <= 4)
                mSubtitleTextView.setText(mSubtitles[2]);
            else
                mSubtitleTextView.setText(mSubtitles[0]);

        }
    }

}
