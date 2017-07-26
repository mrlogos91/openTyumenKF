package com.opentmn.opentmn.screens.rating;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.UserViewHolder;

import java.util.ArrayList;
import java.util.List;

import сom.opentmn.opentmn.R;

/**
 * Created by kost on 23.12.16.
 */

public class RatingAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private Context mContext;
    private List<User> mUserList;
    private int mLeftMargin;
    private OnItemClickListener mOnItemClickListener;
    private User mUser;

    public RatingAdapter(Context context, OnItemClickListener onItemClickListener) {
        mContext = context;
        mOnItemClickListener = onItemClickListener;
        mUserList = new ArrayList<>();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int cardWidth = context.getResources().getDimensionPixelSize(R.dimen.friends_card_width);
        mLeftMargin = (size.x - cardWidth) / 2;
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_rating, parent, false);
        return new UserViewHolder(itemView, mLeftMargin);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = mUserList.get(position);
        holder.setupWithRatingUser(user, mOnItemClickListener, mUser.getId() == user.getId());
        if(position == getItemCount() - 1) {
            mOnItemClickListener.onEndReached();
        }
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public void setUserList(List<User> userList) {
        mUserList = userList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onUserClick(User user);
        void onAddFriendClick(User user);
        void onDeleteFriendClick(User user);
        void onPlayClick(User user);
        void onEndReached();
    }
}
