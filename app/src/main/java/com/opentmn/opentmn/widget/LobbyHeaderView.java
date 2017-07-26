package com.opentmn.opentmn.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.opentmn.opentmn.Config;
import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.GameType;
import com.opentmn.opentmn.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import сom.opentmn.opentmn.R;

/**
 * Created by kost on 14.01.17.
 */

public class LobbyHeaderView extends FrameLayout {

    @BindView(R.id.name_text_view)
    TextView mNameTextView;
    @BindView(R.id.ava_image_view)
    ImageView mAvaImageView;
    @BindView(R.id.enemy_name_text_view)
    TextView mEnemyNameTextView;
    @BindView(R.id.enemy_ava_image_view)
    ImageView mEnemyAvaImageView;
    @BindView(R.id.score_text_view)
    TextView mScoreTextView;
    @BindView(R.id.score_layout)
    ViewGroup mScoreLayout;
    @BindView(R.id.add_friend_creator_button)
    Button mAddFriendCreatorButton;
    @BindView(R.id.add_friend_follower_button)
    Button mAddFriendFollowerButton;

    public LobbyHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LobbyHeaderView(Context context) {
        super(context);

        setupView(context);
    }

    public LobbyHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setupView(context);
    }

    private void setupView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_lobby_header, this);
        ButterKnife.bind(this);
    }

    public void setupWithGame(Game game, OnClickListener onClickListener) {
        User user = RepositoryProvider.provideKeyValueStorage().getUser();
        boolean isCreator = game.isCreator(user);
        User first = isCreator ? game.getCreator() : game.getFollower();
        User second = isCreator ? game.getFollower() : game.getCreator();
        mNameTextView.setText(first.getName());
        if(first.getAvatar() != null && first.getAvatar().length() > 0)
            Glide.with(getContext()).load(Config.SERVER + first.getAvatar()).bitmapTransform(new CropCircleTransformation(getContext())).placeholder(R.mipmap.ava_placeholder_b).into(mAvaImageView);
        else
            mAvaImageView.setImageResource(R.mipmap.ava_placeholder_b);

        mEnemyNameTextView.setText(second.getName());
        if(game.getGameTypeId() == GameType.TRAINING)
            mEnemyAvaImageView.setImageResource(R.mipmap.placeholder_computer);
        else if(second.getAvatar() != null && second.getAvatar().length() > 0)
            Glide.with(getContext()).load(Config.SERVER + second.getAvatar()).bitmapTransform(new CropCircleTransformation(getContext())).placeholder(R.mipmap.ava_placeholder_b).into(mEnemyAvaImageView);
        else
            mEnemyAvaImageView.setImageResource(R.mipmap.ava_placeholder_b);

        mScoreTextView.setText(String.valueOf(first.getAnswersCount() + ":" + String.valueOf(second.getAnswersCount())));

        int myNum = first.getAnswersCount();
        int opNum = second.getAnswersCount();
        if(myNum == opNum) {
            mScoreLayout.setBackgroundResource(R.mipmap.lobby_score_2);
            mScoreTextView.setTextColor(getContext().getResources().getColor(R.color.lobby_score_yellow));
        } else if(myNum > opNum) {
            mScoreLayout.setBackgroundResource(R.mipmap.lobby_score_1);
            mScoreTextView.setTextColor(getContext().getResources().getColor(R.color.lobby_score_green));
        } else {
            mScoreLayout.setBackgroundResource(R.mipmap.lobby_score_3);
            mScoreTextView.setTextColor(getContext().getResources().getColor(R.color.lobby_score_red));
        }

        mAddFriendCreatorButton.setVisibility(GONE);
        if(game.getGameTypeId() == GameType.TRAINING) {
            mAddFriendFollowerButton.setVisibility(GONE);
        } else {
            if(!second.isFriend()) {
                mAddFriendFollowerButton.setVisibility(VISIBLE);
                mAddFriendFollowerButton.setOnClickListener(onClickListener);
            } else {
                mAddFriendFollowerButton.setVisibility(GONE);
            }
        }
    }

}
