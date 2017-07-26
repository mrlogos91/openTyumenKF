package com.opentmn.opentmn.screens.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.Category;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseViewHolder;
import com.opentmn.opentmn.screens.profile.viewholder.ButtonViewHolder;
import com.opentmn.opentmn.screens.profile.viewholder.FavoriteCategoryViewHolder;
import com.opentmn.opentmn.screens.profile.viewholder.HistoryScoreViewHolder;
import com.opentmn.opentmn.screens.profile.viewholder.ProfileHeaderViewHolder;
import com.opentmn.opentmn.screens.base.TitleViewHolder;

import java.util.List;

import com.opentmn.opentmn.R;

/**
 * Created by kost on 31.12.16.
 */

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_HEADER = 0;
    private static final int ITEM_TYPE_TITLE = 1;
    private static final int ITEM_HISTORY_SCORE = 2;
    private static final int ITEM_FAVORITE_CATEGORY = 3;
    private static final int ITEM_TYPE_EMPTY = 4;
    private static final int ITEM_TYPE_BUTTON = 5;

    private Context mContext;
    private User mUser;
    private boolean mIsMyProfile;
    private OnItemClickListener mOnItemClickListener;

    public ProfileAdapter(Context context, OnItemClickListener onItemClickListener) {
        mContext = context;
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_TYPE_HEADER) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_profile_header, parent, false);
            return new ProfileHeaderViewHolder(itemView);
        } else if(viewType == ITEM_HISTORY_SCORE) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_history_score, parent, false);
            return new HistoryScoreViewHolder(itemView);
        } else if(viewType == ITEM_FAVORITE_CATEGORY) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_category_favorite, parent, false);
            return new FavoriteCategoryViewHolder(itemView);
        } else if(viewType == ITEM_TYPE_EMPTY) {
            return new BaseViewHolder(new View(mContext), mContext.getResources().getDimensionPixelSize(R.dimen.toolbar_height));
        } else if(viewType == ITEM_TYPE_BUTTON) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_button, parent, false);
            return new ButtonViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_title, parent, false);
            return new TitleViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case ITEM_TYPE_TITLE:
                TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
                titleViewHolder.setTitle(position == 2 ? "История игр" : "Любимые категории");
                break;
            case ITEM_TYPE_HEADER:
                ProfileHeaderViewHolder profileHeaderViewHolder = (ProfileHeaderViewHolder) holder;
                profileHeaderViewHolder.setupWithUser(mUser, mIsMyProfile, mOnItemClickListener);
                break;
            case ITEM_HISTORY_SCORE:
                HistoryScoreViewHolder historyScoreViewHolder = (HistoryScoreViewHolder) holder;
                historyScoreViewHolder.setupWithUser(mUser);
                break;
            case ITEM_FAVORITE_CATEGORY:
                FavoriteCategoryViewHolder favoriteCategoryViewHolder = (FavoriteCategoryViewHolder) holder;
                int index = position - 5;
                favoriteCategoryViewHolder.setupWithCategory(mUser.getCategories().get(index), index);
                break;
            case ITEM_TYPE_BUTTON:
                ButtonViewHolder buttonViewHolder = (ButtonViewHolder) holder;
                buttonViewHolder.setupBlackListButton(mIsMyProfile, mUser, mOnItemClickListener);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(mUser == null)
            return 0;
        int count = 5;
        List<Category> categoryList = mUser.getCategories();
        if(categoryList != null && categoryList.size() > 0) {
            count = count + categoryList.size() + 1;
        }
        return count;

    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return ITEM_TYPE_EMPTY;
        else if(position == 1)
            return ITEM_TYPE_HEADER;
        else if(position == 3)
            return ITEM_HISTORY_SCORE;
        else if(position == getItemCount() - 1)
            return ITEM_TYPE_BUTTON;
        else if(position > 4)
            return ITEM_FAVORITE_CATEGORY;
        else
            return ITEM_TYPE_TITLE;
    }

    public void setUser(User user) {
        mUser = user;
        User myUser = RepositoryProvider.provideKeyValueStorage().getUser();
        mIsMyProfile = mUser.getId() == myUser.getId();
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onPlayClick(User user);
        void onBlackListClick();
        void onBlockClick();
        void onFriendsClick();
        void onAddFriendClick();
    }
}
