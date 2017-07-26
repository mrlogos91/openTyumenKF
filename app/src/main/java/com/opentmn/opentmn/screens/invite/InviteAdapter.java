package com.opentmn.opentmn.screens.invite;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.opentmn.opentmn.model.SocialUser;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.UserViewHolder;

import java.util.ArrayList;
import java.util.List;

import com.opentmn.opentmn.R;

/**
 * Created by kost on 23.01.17.
 */

public class InviteAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private final static int ITEM_TYPE_SOCIAL = 0;
    private final static int ITEM_TYPE_USER = 1;

    private Context mContext;
    private List<SocialUser> mUsers;
    private OnItemClickListener mOnItemClickListener;
    private int mLeftMargin;

    public InviteAdapter(Context context, OnItemClickListener onItemClickListener) {
        mContext = context;
        mUsers = new ArrayList<>();
        mOnItemClickListener = onItemClickListener;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int cardWidth = context.getResources().getDimensionPixelSize(R.dimen.friends_card_width);
        mLeftMargin = (size.x - cardWidth) / 2;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_TYPE_SOCIAL) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_invite, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_friend, parent, false);
            return new UserViewHolder(view, mLeftMargin);
        }
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        SocialUser socialUser = mUsers.get(position);
        if(holder.getItemViewType() == ITEM_TYPE_SOCIAL) {
            holder.setupWithSocialUser(socialUser, mOnItemClickListener);
        } else {
            User user = socialUser.getProfile();
            holder.setupWithUser(user, mOnItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void setUsers(List<SocialUser> users) {
        mUsers = users;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        SocialUser user = mUsers.get(position);
        if(user.getProfile() == null)
            return ITEM_TYPE_SOCIAL;
        else
            return ITEM_TYPE_USER;
    }

    public interface OnItemClickListener {
        void onInviteClick(SocialUser socialUser);
        void onUserClick(User user);
        void onDeleteFriendClick(User user);
        void onPlayClick(User user);
        void onAddFriendClick(User user);
    }
}
