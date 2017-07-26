package com.opentmn.opentmn.screens.my_games;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.GameStatus;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseViewHolder;
import com.opentmn.opentmn.screens.base.GameViewHolder;
import com.opentmn.opentmn.screens.base.TitleViewHolder;
import com.opentmn.opentmn.screens.profile.viewholder.ButtonViewHolder;

import java.util.ArrayList;
import java.util.List;

import com.opentmn.opentmn.R;

/**
 * Created by kost on 14.01.17.
 */

public class MyGamesAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int ITEM_TYPE_EMPTY = 0;
    private static final int ITEM_TYPE_BUTTON = 1;
    private static final int ITEM_TYPE_HEADER = 2;
    private static final int ITEM_TYPE_GAME = 3;
    private static final int ITEM_TYPE_WAIT = 4;
    private static final int ITEM_TYPE_INVITE = 5;
    private static final int ITEM_TYPE_NO_FOLLOWER = 6;

    private Context mContext;
    private List<Game> mGames;
    private OnItemClickListener mOnItemClickListener;
    private User mUser;

    public MyGamesAdapter(Context context, OnItemClickListener onItemClickListener) {
        mContext = context;
        mOnItemClickListener = onItemClickListener;
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
        mGames = new ArrayList<>();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_TYPE_EMPTY) {
            return new BaseViewHolder(new View(mContext), mContext.getResources().getDimensionPixelSize(R.dimen.my_games_top_margin));
        } else if(viewType == ITEM_TYPE_BUTTON) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_button, parent, false);
            return new ButtonViewHolder(view);
        } else if(viewType == ITEM_TYPE_HEADER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_title, parent, false);
            return new TitleViewHolder(view);
        } else if(viewType == ITEM_TYPE_GAME) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_live_game, parent, false);
            return new GameViewHolder(view);
        } else if(viewType == ITEM_TYPE_WAIT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_game_wait, parent, false);
            return new GameViewHolder(view);
        } else if(viewType == ITEM_TYPE_INVITE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_game_invite, parent, false);
            return new GameViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_game, parent, false);
            return new GameViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if(holder.getItemViewType() == ITEM_TYPE_BUTTON ) {
            ButtonViewHolder buttonViewHolder = (ButtonViewHolder) holder;
            if(position == 1) {
                buttonViewHolder.setupNewGameButton();
                buttonViewHolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onNewGameClick();
                    }
                });
            } else {
                buttonViewHolder.setupFinishedGamesButton();
                buttonViewHolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onFinishedGamesClick();
                    }
                });
            }
        } else if(holder.getItemViewType() == ITEM_TYPE_HEADER) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.setTitle(mGames.get(position - 2).getName());
        } else if(holder.getItemViewType() == ITEM_TYPE_GAME) {
            GameViewHolder gameViewHolder = (GameViewHolder) holder;
            Game game = mGames.get(position - 2);
            gameViewHolder.setupWithGame(game, mUser);
            gameViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onGameClick(game);
                }
            });
        } else if(holder.getItemViewType() == ITEM_TYPE_WAIT) {
            GameViewHolder gameViewHolder = (GameViewHolder) holder;
            Game game = mGames.get(position - 2);
            gameViewHolder.setupWithWaitGame(game);
            gameViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onGameClick(game);
                }
            });
        } else if(holder.getItemViewType() == ITEM_TYPE_INVITE) {
            GameViewHolder gameViewHolder = (GameViewHolder) holder;
            Game game = mGames.get(position - 2);
            gameViewHolder.setupWithInviteGame(game, mOnItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return 3 + mGames.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return ITEM_TYPE_EMPTY;
        } else if(position == 1) {
            return ITEM_TYPE_BUTTON;
        } else if(position == getItemCount() - 1) {
            return ITEM_TYPE_BUTTON;
        } else {
            Game game = mGames.get(position - 2);
            if(game.getId() == -1)
                return ITEM_TYPE_HEADER;
            else if(game.getStatus(mUser) == GameStatus.WAIT)
                return ITEM_TYPE_WAIT;
            else if(game.getStatus(mUser) == GameStatus.INVITE)
                return ITEM_TYPE_INVITE;
            else if(game.getStatus(mUser) == GameStatus.NO_FOLLOWER)
                return ITEM_TYPE_WAIT;
            else
                return ITEM_TYPE_GAME;
        }
    }

    public void setGames(List<Game> games) {
        mGames = games;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onGameClick(Game game);
        void onNewGameClick();
        void onFinishedGamesClick();
        void onAcceptClick(Game game);
        void onDenyClick(Game game);
    }
}
