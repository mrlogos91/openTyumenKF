package com.opentmn.opentmn.screens.profile.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.opentmn.opentmn.model.Category;

import com.opentmn.opentmn.R;

/**
 * Created by kost on 31.12.16.
 */

public class FavoriteCategoryViewHolder extends RecyclerView.ViewHolder {

    private ImageView mBackgroundImageView;
    private TextView mNameTextView;

    public FavoriteCategoryViewHolder(View itemView) {
        super(itemView);

        mNameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
        mBackgroundImageView = (ImageView) itemView.findViewById(R.id.background_image_view);
    }

    public void setupWithCategory(Category category, int index) {
        mNameTextView.setText(category.getName());
        Context context = itemView.getContext();
        switch (index){
            case 0:
                mBackgroundImageView.setImageResource(R.mipmap.fav_cat_1);
                mNameTextView.setTextColor(context.getResources().getColor(R.color.favorite_category_1));
                break;
            case 1:
                mBackgroundImageView.setImageResource(R.mipmap.fav_cat_2);
                mNameTextView.setTextColor(context.getResources().getColor(R.color.favorite_category_2));
                break;
            case 2:
                mBackgroundImageView.setImageResource(R.mipmap.fav_cat_3);
                mNameTextView.setTextColor(context.getResources().getColor(R.color.favorite_category_3));
                break;
        }
    }
}
