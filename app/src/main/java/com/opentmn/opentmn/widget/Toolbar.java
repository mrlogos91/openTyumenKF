package com.opentmn.opentmn.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.opentmn.opentmn.R;

/**
 * Created by kost on 23.12.16.
 */

public class Toolbar extends FrameLayout {

    public static int NAVIGATION_BACK_ICON = R.mipmap.app_bar_arrow_back;
    public static int NAVIGATION_MENU_ICON = R.mipmap.app_bar_btn_menu;
    public static int ICON_INFO = R.mipmap.app_bar_ic_info;
    public static int ICON_EDIT = R.mipmap.app_bar_edit;

    private TextView mTitleTextView;
    private ImageView mNavigationImageView;
    private FrameLayout mNavClickLayout;
    private ImageView mRightImageView;
    private FrameLayout mRightClickLayout;

    public Toolbar(Context context) {
        super(context);
    }

    public Toolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs);
    }

    public Toolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs);
    }

    private void setup(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_toolbar, this);
        mTitleTextView = (TextView) findViewById(R.id.action_bar_title_text_view);
        mNavigationImageView = (ImageView) findViewById(R.id.action_bar_navigation_image_view);
        mNavClickLayout = (FrameLayout) findViewById(R.id.nav_click_layout);
        mRightImageView = (ImageView) findViewById(R.id.action_bar_right_image_view);
        mRightClickLayout = (FrameLayout) findViewById(R.id.right_click_layout);

        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Toolbar);
        String title = array.getString(R.styleable.Toolbar_title);
        if(title != null)
            setTitle(title);
        array.recycle();
    }

    public void setTitle(String title) {
        mTitleTextView.setText(title);
    }

    public void setNavigationImageRes(int res) {
        mNavigationImageView.setImageResource(res);
    }

    public void setRightImageRes(int res, OnClickListener onClickListener) {
        if(res > 0)
            mRightImageView.setImageResource(res);
        else
            mRightImageView.setImageDrawable(null);
        mRightClickLayout.setOnClickListener(onClickListener);
    }

    public void setNavigationImageVisible(boolean visible){
        if(visible) {
            mNavigationImageView.setVisibility(VISIBLE);
        }else {
            mNavigationImageView.setVisibility(INVISIBLE);
        }
    }

    public void setOnNavigationClickListener(OnClickListener onClickListener) {
        mNavClickLayout.setOnClickListener(onClickListener);
    }

    public void setOnRightClickListener(OnClickListener onClickListener) {
        mRightClickLayout.setOnClickListener(onClickListener);
    }

}
