package com.opentmn.opentmn.screens.shop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.opentmn.opentmn.Config;
import com.opentmn.opentmn.model.Gift;
import com.opentmn.opentmn.screens.base.BaseViewHolder;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import сom.opentmn.opentmn.R;

/**
 * Created by kost on 03.01.17.
 */

public class GiftViewHolder extends BaseViewHolder {

    private ImageView mPicImageView;
    private TextView mNameTextView;
    private TextView mCountTextView;
    private TextView mCoinsTextView;

    public GiftViewHolder(View itemView) {
        super(itemView);

        mPicImageView = (ImageView) itemView.findViewById(R.id.pic_image_view);
        mNameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
        mCountTextView = (TextView) itemView.findViewById(R.id.count_text_view);
        mCoinsTextView = (TextView) itemView.findViewById(R.id.coins_text_view);
    }

    public void setupWithGift(Gift gift, GiftsAdapter.OnItemClickListener onItemClickListener) {
        Context context = itemView.getContext();
        if(gift.getImage() != null)
            Glide.with(context).load(Config.SERVER + gift.getImage()).bitmapTransform(new RoundedCornersTransformation(context, context.getResources().getDimensionPixelSize(R.dimen.gift_image_corner_radius), 0)).into(mPicImageView);
        mNameTextView.setText(gift.getName());
        mCountTextView.setText(String.valueOf(gift.getCount()) + " шт.");
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(gift);
            }
        });
        mCoinsTextView.setText(String.valueOf(gift.getPrice()));
    }

}
