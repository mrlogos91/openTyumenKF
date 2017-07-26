package com.opentmn.opentmn.screens.history;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseViewHolder;
import com.opentmn.opentmn.screens.base.GameViewHolder;

import java.util.ArrayList;
import java.util.List;

import com.opentmn.opentmn.R;

/**
 * Created by kost on 21.01.17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final static int ITEM_TYPE_EMPTY = 0;
    private final static int ITEM_TYPE_GAME = 1;

    private Context mContext;
    private List<Game> mGames;
    private User mUser;
    private OnItemClickListener mOnItemClickListener;

    public HistoryAdapter(Context context, OnItemClickListener onItemClickListener) {
        mContext = context;
        mGames = new ArrayList<>();
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_TYPE_EMPTY) {
            return new BaseViewHolder(new View(mContext), mContext.getResources().getDimensionPixelSize(R.dimen.my_games_top_margin));
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_game, parent, false);
            return new GameViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if(holder.getItemViewType() == ITEM_TYPE_GAME) {
            Game game = mGames.get(position - 1);
            GameViewHolder gameViewHolder = (GameViewHolder) holder;
            gameViewHolder.setupWithGame(game, mUser);
        }
        if(position > 0 && position == getItemCount() - 1) {
            mOnItemClickListener.onEndReached();
        }
    }

    @Override
    public int getItemCount() {
        return mGames.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_TYPE_EMPTY : ITEM_TYPE_GAME;
    }

    public void setGames(List<Game> games) {
        mGames = games;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onEndReached();
    }
}
