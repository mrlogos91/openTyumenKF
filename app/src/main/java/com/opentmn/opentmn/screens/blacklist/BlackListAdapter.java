package com.opentmn.opentmn.screens.blacklist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.UserViewHolder;

import java.util.ArrayList;
import java.util.List;

import сom.opentmn.opentmn.R;

/**
 * Created by kost on 24.01.17.
 */

public class BlackListAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private List<User> mUsers;

    public BlackListAdapter(Context context, OnItemClickListener onItemClickListener) {
        mContext = context;
        mOnItemClickListener = onItemClickListener;
        mUsers = new ArrayList<>();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_black_list, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.setupBlockedUser(user, mOnItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void setUsers(List<User> users) {
        mUsers = users;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onBlockClick(User user);
        void onUserClick(User user);
        void onEndReached();
    }
}
