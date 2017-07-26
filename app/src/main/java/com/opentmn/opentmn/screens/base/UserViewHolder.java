package com.opentmn.opentmn.screens.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.opentmn.opentmn.Config;
import com.opentmn.opentmn.model.SocialUser;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.blacklist.BlackListAdapter;
import com.opentmn.opentmn.screens.choose_opponent.OpponentsAdapter;
import com.opentmn.opentmn.screens.invite.InviteAdapter;
import com.opentmn.opentmn.screens.rating.RatingAdapter;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import com.opentmn.opentmn.R;

/**
 * Created by kost on 03.01.17.
 */

public class UserViewHolder extends RecyclerView.ViewHolder {
    private static int[] PLACE_IMAGE_RES = new int[]{R.mipmap.rating_place_1, R.mipmap.rating_place_2, R.mipmap.rating_place_3, R.mipmap.rating_place_4, R.mipmap.rating_place_5, R.mipmap.rating_place_6, R.mipmap.rating_place_7, R.mipmap.rating_place_8, R.mipmap.rating_place_9};
    private TextView mNumberTextView;
    private TextView mNameTextView;
    private TextView mScoreTextView;
    private ImageView mPlaceImageView;
    private Button mAddDelFriendButton;
    private Button mPlayButton;
    private ImageView mAvaImageView;
    private HorizontalScrollView mScrollView;
    private View mClickView;

    public UserViewHolder(View itemView) {
        super(itemView);
        mNumberTextView = (TextView) itemView.findViewById(R.id.number_text_view);
        mNameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
        mScoreTextView = (TextView) itemView.findViewById(R.id.score_text_view);
        mPlaceImageView = (ImageView) itemView.findViewById(R.id.place_back_image_view);
        mAddDelFriendButton = (Button) itemView.findViewById(R.id.add_friend_button);
        mPlayButton = (Button) itemView.findViewById(R.id.play_button);
        mAvaImageView = (ImageView) itemView.findViewById(R.id.ava_image_view);
        mScrollView = (HorizontalScrollView) itemView.findViewById(R.id.scroll_view);
        mClickView = itemView.findViewById(R.id.click_view);
    }

    public UserViewHolder(View itemView, int leftMargin) {
        this(itemView);

        ViewGroup cardLayout = (ViewGroup) itemView.findViewById(R.id.card_layout);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cardLayout.getLayoutParams();
        params.leftMargin = leftMargin;
    }


    public void setupWithRatingUser(User user, RatingAdapter.OnItemClickListener onItemClickListener, boolean isMy) {
        int userPosition = user.getPosition();
        mNumberTextView.setText(String.valueOf(userPosition));
        mNameTextView.setText(user.getName());
        mScoreTextView.setText(String.valueOf(user.getRating()));
        Context context = itemView.getContext();
        if(user.getAvatar() != null)
            Glide.with(context).load(Config.SERVER + user.getAvatar()).bitmapTransform(new CropCircleTransformation(context)).placeholder(R.mipmap.ava_placeholder_l).into(mAvaImageView);
        else
            mAvaImageView.setImageResource(R.mipmap.ava_placeholder_l);
        if(userPosition < 10) {
            mPlaceImageView.setImageResource(PLACE_IMAGE_RES[userPosition - 1]);
        } else {
            mPlaceImageView.setImageResource(R.mipmap.rating_place_other);
        }
        mClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onUserClick(user);
            }
        });
        if(user.isFriend()) {
            mAddDelFriendButton.setBackgroundResource(R.drawable.button_delete_friend_selector);
            mAddDelFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onDeleteFriendClick(user);
                }
            });
        } else {
            mAddDelFriendButton.setBackgroundResource(R.drawable.button_add_friend_selector);
            mAddDelFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onAddFriendClick(user);
                }
            });
        }
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onPlayClick(user);
            }
        });
        mScrollView.smoothScrollTo(0, 0);
        if(isMy)
            mPlayButton.setVisibility(View.GONE);
        else
            mPlayButton.setVisibility(View.VISIBLE);
        mAddDelFriendButton.setVisibility(isMy ? View.GONE : View.VISIBLE);
    }

    public void setupWithFriendUser(User user, RatingAdapter.OnItemClickListener onItemClickListener, boolean isMy) {
        int userPosition = user.getPosition();
        mNumberTextView.setText(String.valueOf(userPosition));
        mNameTextView.setText(user.getName());
        mScoreTextView.setText(String.valueOf(user.getRating()));
        Context context = itemView.getContext();
        if(user.getAvatar() != null)
            Glide.with(context).load(Config.SERVER + user.getAvatar()).bitmapTransform(new CropCircleTransformation(context)).placeholder(R.mipmap.ava_placeholder_l).into(mAvaImageView);
        else
            mAvaImageView.setImageResource(R.mipmap.ava_placeholder_l);
        mClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onUserClick(user);
            }
        });
        mAddDelFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onDeleteFriendClick(user);
            }
        });
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onPlayClick(user);
            }
        });
        mScrollView.smoothScrollTo(0, 0);
    }

    public void setupWithOpponent(User user, RatingAdapter.OnItemClickListener onItemClickListener, boolean isMy) {
        int userPosition = user.getPosition();
        mNumberTextView.setText(String.valueOf(userPosition));
        mNameTextView.setText(user.getName());
        mScoreTextView.setText(String.valueOf(user.getRating()));
        Context context = itemView.getContext();
        if(user.getAvatar() != null)
            Glide.with(context).load(Config.SERVER + user.getAvatar()).bitmapTransform(new CropCircleTransformation(context)).placeholder(R.mipmap.ava_placeholder_l).into(mAvaImageView);
        else
            mAvaImageView.setImageResource(R.mipmap.ava_placeholder_l);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onPlayClick(user);
            }
        });
        mClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onUserClick(user);
            }
        });
        if(isMy)
            mPlayButton.setVisibility(View.GONE);
        else
            mPlayButton.setVisibility(View.VISIBLE);
    }

    public void setupWithSocialUser(SocialUser socialUser, InviteAdapter.OnItemClickListener onItemClickListener) {
        mNameTextView.setText(socialUser.getName());
        if(socialUser.getPhoto() != null) {
            Context context = itemView.getContext();
            Glide.with(context).load(socialUser.getPhoto()).bitmapTransform(new CropCircleTransformation(context)).placeholder(R.mipmap.ava_placeholder_l).into(mAvaImageView);
        } else
            mAvaImageView.setImageResource(R.mipmap.ava_placeholder_l);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onInviteClick(socialUser);
            }
        });

    }

    public void setupBlockedUser(User user, BlackListAdapter.OnItemClickListener onItemClickListener) {
        mNameTextView.setText(user.getName());
        mNumberTextView.setText(String.valueOf(user.getPosition()));
        mNameTextView.setText(user.getName());
        mScoreTextView.setText(String.valueOf(user.getRating()));
        Context context = itemView.getContext();
        if(user.getAvatar() != null)
            Glide.with(context).load(Config.SERVER + user.getAvatar()).bitmapTransform(new CropCircleTransformation(context)).placeholder(R.mipmap.ava_placeholder_l).into(mAvaImageView);
        else
            mAvaImageView.setImageResource(R.mipmap.ava_placeholder_l);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onBlockClick(user);
            }
        });
        mClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onUserClick(user);
            }
        });
    }

    public void setupWithUser(User user, InviteAdapter.OnItemClickListener onItemClickListener) {
        int userPosition = user.getPosition();
        mNumberTextView.setText(String.valueOf(userPosition));
        mNameTextView.setText(user.getName());
        mScoreTextView.setText(String.valueOf(user.getRating()));
        Context context = itemView.getContext();
        if(user.getAvatar() != null)
            Glide.with(context).load(Config.SERVER + user.getAvatar()).bitmapTransform(new CropCircleTransformation(context)).placeholder(R.mipmap.ava_placeholder_l).into(mAvaImageView);
        else
            mAvaImageView.setImageResource(R.mipmap.ava_placeholder_l);
        mClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onUserClick(user);
            }
        });
        if(user.isFriend()) {
            mAddDelFriendButton.setBackgroundResource(R.drawable.button_delete_friend_selector);
            mAddDelFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onDeleteFriendClick(user);
                }
            });
        } else {
            mAddDelFriendButton.setBackgroundResource(R.drawable.button_add_friend_selector);
            mAddDelFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onAddFriendClick(user);
                }
            });
        }
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onPlayClick(user);
            }
        });
        mScrollView.smoothScrollTo(0, 0);
    }
}
