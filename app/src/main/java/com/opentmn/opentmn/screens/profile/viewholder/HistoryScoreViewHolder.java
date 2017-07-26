package com.opentmn.opentmn.screens.profile.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.opentmn.opentmn.Config;
import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import com.opentmn.opentmn.R;

/**
 * Created by kost on 31.12.16.
 */

public class HistoryScoreViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.wins_text_view)
    TextView mWinsTextView;
    @BindView(R.id.draws_text_view)
    TextView mDrawsTextView;
    @BindView(R.id.loses_text_view)
    TextView mLosesTextView;

    @BindView(R.id.name_text_view)
    TextView mNameTextView;
    @BindView(R.id.enemy_name_text_view)
    TextView mEnemyNameTextView;
    @BindView(R.id.score_view)
    View mScoreView;
    @BindView(R.id.score_text_view)
    TextView mScoreTextView;
    @BindView(R.id.ava_image_view)
    ImageView mAvaImageView;
    @BindView(R.id.enemy_ava_image_view)
    ImageView mEnemyAvaImageView;
    @BindView(R.id.substrate_view)
    View mSubstrateView;

    public HistoryScoreViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void setupWithUser(User user) {
        mWinsTextView.setText(String.valueOf(user.getWins()));
        mDrawsTextView.setText(String.valueOf(user.getDraws()));
        mLosesTextView.setText(String.valueOf(user.getLouses()));
        User myUser = RepositoryProvider.provideKeyValueStorage().getUser();
        if(myUser.getId() == user.getId()) {
            mScoreView.setVisibility(View.GONE);
        } else {
            mNameTextView.setText(myUser.getName());
            mEnemyNameTextView.setText(user.getName());
            mScoreTextView.setText(String.valueOf(user.getTokenWins()) + ":" + String.valueOf(user.getTokenLoses()));
            Context context = itemView.getContext();
            if(myUser.getAvatar() != null)
                Glide.with(context).load(Config.SERVER + myUser.getAvatar()).bitmapTransform(new CropCircleTransformation(context)).placeholder(R.mipmap.ava_placeholder_l).into(mAvaImageView);
            else
                mAvaImageView.setImageResource(R.mipmap.ava_placeholder_l);
            if(user.getAvatar() != null)
                Glide.with(context).load(Config.SERVER + user.getAvatar()).bitmapTransform(new CropCircleTransformation(context)).placeholder(R.mipmap.ava_placeholder_l).into(mEnemyAvaImageView);
            else
                mEnemyAvaImageView.setImageResource(R.mipmap.ava_placeholder_l);
            if(user.getTokenWins() > user.getTokenLoses()) {
                mSubstrateView.setBackgroundResource(R.mipmap.profile_sub_g_2);
            } else if(user.getTokenWins() < user.getTokenLoses()) {
                mSubstrateView.setBackgroundResource(R.mipmap.profile_sub_g_1);
            } else {
                mSubstrateView.setBackgroundResource(R.mipmap.substrate_bordered_orange);
            }
        }
    }

}
