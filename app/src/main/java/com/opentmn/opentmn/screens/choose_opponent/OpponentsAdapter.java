package com.opentmn.opentmn.screens.choose_opponent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.UserViewHolder;
import com.opentmn.opentmn.screens.rating.RatingAdapter;

import java.util.ArrayList;
import java.util.List;

import com.opentmn.opentmn.R;

/**
 * Created by kost on 12.01.17.
 */

public class OpponentsAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private RatingAdapter.OnItemClickListener mOnItemClickListener;
    private User mUser;

    public OpponentsAdapter(Context context, RatingAdapter.OnItemClickListener onItemClickListener) {
        mContext = context;
        mUsers = new ArrayList<>();
        mOnItemClickListener = onItemClickListener;
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_opponent, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.setupWithOpponent(user, mOnItemClickListener, mUser.getId() == user.getId());
        if(position == getItemCount() - 1) {
            mOnItemClickListener.onEndReached();
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void setUsers(List<User> users) {
        mUsers = users;
        notifyDataSetChanged();
    }
}
