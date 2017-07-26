package com.opentmn.opentmn.screens.profile.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.opentmn.opentmn.Config;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseViewHolder;
import com.opentmn.opentmn.screens.profile.ProfileAdapter;
import com.opentmn.opentmn.widget.Button;
import com.opentmn.opentmn.widget.ScoreView;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import сom.opentmn.opentmn.R;

/**
 * Created by kost on 31.12.16.
 */

public class ProfileHeaderViewHolder extends BaseViewHolder {

    private ImageView mAvaImageView;
    private TextView mNameTextView;
    private ScoreView mFriendsScoreView;
    private ScoreView mPlaceScoreView;
    private ScoreView mPointsScoreView;
    private ScoreView mCoinsScoreView;
    private Button mPlayButton;
    private Button mAddFriendButton;

    public ProfileHeaderViewHolder(View itemView) {
        super(itemView);

        mAvaImageView = (ImageView) itemView.findViewById(R.id.ava_image_view);
        mNameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
        mFriendsScoreView = (ScoreView) itemView.findViewById(R.id.friends_score_view);
        mPlaceScoreView = (ScoreView) itemView.findViewById(R.id.place_score_view);
        mPointsScoreView = (ScoreView) itemView.findViewById(R.id.points_score_view);
        mCoinsScoreView = (ScoreView) itemView.findViewById(R.id.coins_score_view);
        mPlayButton = (Button) itemView.findViewById(R.id.play_button);
        mAddFriendButton = (Button) itemView.findViewById(R.id.add_friend_button);
    }

    public void setupWithUser(User user, boolean isMyProfile, ProfileAdapter.OnItemClickListener onItemClickListener) {
        mNameTextView.setText(user.getName());
        mFriendsScoreView.setValue(user.getFriends());
        mPlaceScoreView.setValue(user.getPosition());
        mPointsScoreView.setValue(user.getRating());
        Context context = itemView.getContext();
        if(user.getAvatar() != null)
            Glide.with(context).load(Config.SERVER + user.getAvatar()).bitmapTransform(new CropCircleTransformation(context)).placeholder(R.mipmap.ava_placeholder).into(mAvaImageView);
        if(!isMyProfile) {
            mPlayButton.setVisibility(View.VISIBLE);
            mPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onPlayClick(user);
                }
            });
        } else {
            mPlayButton.setVisibility(View.GONE);
            mFriendsScoreView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onFriendsClick();
                }
            });
        }
        if(isMyProfile || user.isFriend()) {
            mAddFriendButton.setVisibility(View.GONE);
        } else {
            mAddFriendButton.setVisibility(View.VISIBLE);
            mAddFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onAddFriendClick();
                }
            });
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPointsScoreView.getLayoutParams();
        if(isMyProfile) {
            mCoinsScoreView.setValue(user.getCoins());
            mCoinsScoreView.setVisibility(View.VISIBLE);
            params.topMargin = 0;
            params.bottomMargin = context.getResources().getDimensionPixelSize(R.dimen.profile_score_view_vertical_margin);
            params.leftMargin = context.getResources().getDimensionPixelSize(R.dimen.profile_score_view_margin);
        } else {
            mCoinsScoreView.setVisibility(View.GONE);
            params.topMargin = context.getResources().getDimensionPixelSize(R.dimen.profile_score_view_vertical_margin);
            params.bottomMargin = 0;
            params.leftMargin = context.getResources().getDimensionPixelSize(R.dimen.profile_score_view_no_coins_margin);
        }
    }
}
