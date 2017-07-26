package com.opentmn.opentmn.screens.menu;

import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.opentmn.opentmn.screens.base.BaseViewHolder;

import сom.opentmn.opentmn.R;


/**
 * Created by kost on 12.12.16.
 */

public class DrawerViewHolder extends BaseViewHolder {

    private ImageView mImageView;
    private TextView mTextView;

    public DrawerViewHolder(View itemView) {
        super(itemView);
        mImageView = (ImageView) itemView.findViewById(R.id.image_view);
        mTextView = (TextView) itemView.findViewById(R.id.text_view);
    }

    public void setupWithMenuItem(MenuItem menuItem) {
        mImageView.setImageDrawable(menuItem.getIcon());
        mTextView.setText(menuItem.getTitle());
    }
}
