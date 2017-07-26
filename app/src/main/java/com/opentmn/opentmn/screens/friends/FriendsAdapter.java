package com.opentmn.opentmn.screens.friends;

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
import com.opentmn.opentmn.screens.rating.RatingAdapter;

import java.util.ArrayList;
import java.util.List;

import com.opentmn.opentmn.R;

/**
 * Created by kost on 01.01.17.
 */

public class FriendsAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private Context mContext;
    private List<User> mFriends;
    private int mLeftMargin;
    private RatingAdapter.OnItemClickListener mOnItemClickListener;
    private User mUser;

    public FriendsAdapter(Context context, RatingAdapter.OnItemClickListener onItemClickListener) {
        mContext = context;
        mFriends = new ArrayList<>();
        mOnItemClickListener = onItemClickListener;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_friend, parent, false);
        return new UserViewHolder(view, mLeftMargin);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = mFriends.get(position);
        holder.setupWithFriendUser(user, mOnItemClickListener, mUser.getId() == user.getId());
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    public void setFriends(List<User> users) {
        mFriends = users;
        notifyDataSetChanged();
    }
}
