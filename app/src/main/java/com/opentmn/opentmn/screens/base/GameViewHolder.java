package com.opentmn.opentmn.screens.base;

import android.content.Context;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.opentmn.opentmn.Config;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.GameStatus;
import com.opentmn.opentmn.model.GameType;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.my_games.MyGamesAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import сom.opentmn.opentmn.R;

/**
 * Created by kost on 14.01.17.
 */

public class GameViewHolder extends BaseViewHolder {

    @Nullable
    @BindView(R.id.name_text_view)
    TextView mNameTextView;

    @BindView(R.id.enemy_name_text_view)
    TextView mEnemyNameTextView;

    @Nullable
    @BindView(R.id.ava_image_view)
    ImageView mAvaImageView;

    @BindView(R.id.enemy_ava_image_view)
    ImageView mEnemyAvaImageView;

    @Nullable
    @BindView(R.id.score_text_view)
    TextView mScoreTextView;

    @Nullable
    @BindView(R.id.status_text_view)
    TextView mStatusTextView;

    @Nullable
    @BindView(R.id.accept_view)
    View mAcceptView;

    @Nullable
    @BindView(R.id.deny_view)
    View mDenyView;

    @Nullable
    @BindView(R.id.substrate_view)
    View mSubstrateView;

    @Nullable
    @BindView(R.id.time_text_view)
    TextView mTimeTextView;

    @Nullable
    @BindView(R.id.game_substrate_view)
    View mGameSubstrateView;

    public GameViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void setupWithGame(Game game, User user) {
        boolean isCreator = game.isCreator(user);
        User first;
        User second;
        if(isCreator) {
            first = game.getCreator();
            second = game.getFollower();
        } else {
            first = game.getFollower();
            second = game.getCreator();
        }
        mNameTextView.setText(first.getName());
        mEnemyNameTextView.setText(second.getName());
        mScoreTextView.setText(String.valueOf(first.getAnswersCount() + ":" + String.valueOf(second.getAnswersCount())));

        Context context = itemView.getContext();
        if(first.getAvatar() != null && first.getAvatar().length() > 0)
            Glide.with(context).load(Config.SERVER + first.getAvatar()).bitmapTransform(new CropCircleTransformation(context)).placeholder(R.mipmap.ava_placeholder_l).into(mAvaImageView);
        else
            mAvaImageView.setImageResource(R.mipmap.ava_placeholder_l);
        if(game.getGameTypeId() == GameType.TRAINING)
            mEnemyAvaImageView.setImageResource(R.mipmap.placeholder_computer_small);
        else if(second.getAvatar() != null && second.getAvatar().length() > 0)
            Glide.with(context).load(Config.SERVER + second.getAvatar()).bitmapTransform(new CropCircleTransformation(context)).placeholder(R.mipmap.ava_placeholder_l).into(mEnemyAvaImageView);
        else
            mEnemyAvaImageView.setImageResource(R.mipmap.ava_placeholder_l);

        if(game.getIsFinished() == 1) {
            if(game.getWinner() == first.getId()) {
                mSubstrateView.setBackgroundResource(R.mipmap.profile_sub_g_2);
            } else if(game.getWinner() == second.getId()) {
                mSubstrateView.setBackgroundResource(R.mipmap.profile_sub_g_1);
            } else {
                mSubstrateView.setBackgroundResource(R.mipmap.substrate_bordered_orange);
            }
        } else {
            long difference = game.getDateCreate() + 12 * 60 * 60 - System.currentTimeMillis() / 1000;
            if(difference <= 0) {
                mTimeTextView.setText("Время вышло");
            } else {
                long hours = difference / 3600;
                difference = difference / 60;
                long minutes = difference % 60;
                StringBuilder sb = new StringBuilder();
                if (hours > 0)
                    sb.append(String.valueOf(hours) + " ч ");
                if (minutes > 0)
                    sb.append(String.valueOf(minutes) + " м");
                mTimeTextView.setText(sb);
            }


            int status = game.getActiveStatus(first);
            if(status == GameStatus.MY_TURN || status == GameStatus.CHOOSE_CATEGORY) {
                mGameSubstrateView.setBackgroundResource(R.mipmap.substrate_bordered_blue);
                mStatusTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.game_status_green));
            } else {
                mGameSubstrateView.setBackgroundResource(R.mipmap.profile_games_substrate);
                mStatusTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.game_status_red));
            }
            if(status == GameStatus.CHOOSE_CATEGORY || status == GameStatus.ENEMY_CHOOSE_CATEGORY) {
                mStatusTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.game_status_text_long));
            } else {
                mStatusTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.game_status_text));
            }
            switch (status) {
                case GameStatus.MY_TURN:
                    mStatusTextView.setText(context.getString(R.string.game_status_my_turn));
                    break;
                case GameStatus.CHOOSE_CATEGORY:
                    mStatusTextView.setText(context.getString(R.string.game_status_choose_category));
                    break;
                case GameStatus.ENEMY_CHOOSE_CATEGORY:
                    mStatusTextView.setText(context.getString(R.string.game_status_enemy_choose_category));
                    break;
                case GameStatus.ENEMY_ROUND_1:
                    mStatusTextView.setText(context.getString(R.string.game_status_enemy_play_round_1));
                    break;
                case GameStatus.ENEMY_ROUND_2:
                    mStatusTextView.setText(context.getString(R.string.game_status_enemy_play_round_2));
                    break;
                case GameStatus.ENEMY_ROUND_3:
                    mStatusTextView.setText(context.getString(R.string.game_status_enemy_play_round_3));
                    break;
                case GameStatus.ALL_ANSWERED:
                    mStatusTextView.setText(context.getString(R.string.game_status_all_answered));
                    break;
                default:
                    mStatusTextView.setText("");
            }
        }
    }

    public void setupWithWaitGame(Game game) {
        User follower = game.getFollower();
        if(follower.getId() > -1) {
            mEnemyNameTextView.setText(follower.getName());

            Context context = itemView.getContext();
            if (follower.getAvatar() != null && follower.getAvatar().length() > 0)
                Glide.with(context).load(Config.SERVER + follower.getAvatar()).bitmapTransform(new CropCircleTransformation(context)).placeholder(R.mipmap.ava_placeholder_l).into(mEnemyAvaImageView);
            else
                mEnemyAvaImageView.setImageResource(R.mipmap.ava_placeholder_l);
            mStatusTextView.setText("Не подтвердил");
        } else {
            mEnemyNameTextView.setText("Игрок");
            mEnemyAvaImageView.setImageResource(R.mipmap.ava_placeholder_l);
            mStatusTextView.setText("Ждем игрока");
        }

        long difference = game.getDateCreate() + 12 * 60 * 60 - System.currentTimeMillis() / 1000;
        if(difference <= 0) {
            mTimeTextView.setText("Время вышло");
        } else {
            long hours = difference / 3600;
            difference = difference / 60;
            long minutes = difference % 60;
            StringBuilder sb = new StringBuilder();
            if (hours > 0)
                sb.append(String.valueOf(hours) + " ч ");
            if (minutes > 0)
                sb.append(String.valueOf(minutes) + " м");
            mTimeTextView.setText(sb);
        }
    }

    public void setupWithInviteGame(Game game, MyGamesAdapter.OnItemClickListener onItemClickListener) {
        User creator = game.getCreator();
        mEnemyNameTextView.setText(creator.getName());

        Context context = itemView.getContext();
        if(creator.getAvatar() != null && creator.getAvatar().length() > 0)
            Glide.with(context).load(Config.SERVER + creator.getAvatar()).bitmapTransform(new CropCircleTransformation(context)).placeholder(R.mipmap.ava_placeholder_l).into(mEnemyAvaImageView);
        else
            mEnemyAvaImageView.setImageResource(R.mipmap.ava_placeholder_l);
        mAcceptView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onAcceptClick(game);
            }
        });
        mDenyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onDenyClick(game);
            }
        });
    }
}
