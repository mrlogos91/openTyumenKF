package com.opentmn.opentmn.screens.shop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.opentmn.opentmn.model.Gift;
import com.opentmn.opentmn.screens.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import сom.opentmn.opentmn.R;

/**
 * Created by kost on 03.01.17.
 */

public class GiftsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int ITEM_TYPE_EMPTY = 0;
    private static final int ITEM_TYPE_GIFT = 1;
    private Context mContext;
    private List<Gift> mGifts;
    private OnItemClickListener mOnItemClickListener;

    public GiftsAdapter(Context context, OnItemClickListener onItemClickListener) {
        mContext = context;
        mGifts = new ArrayList<>();
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_TYPE_GIFT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_gift, parent, false);
            return new GiftViewHolder(view);
        } else {
            return new BaseViewHolder(new View(mContext), mContext.getResources().getDimensionPixelSize(R.dimen.gifts_list_top_margin));
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if(position > 0) {
            Gift gift = mGifts.get(position - 1);
            GiftViewHolder giftViewHolder = (GiftViewHolder) holder;
            giftViewHolder.setupWithGift(gift, mOnItemClickListener);
        }
        if(position > 0 && position == mGifts.size())
            mOnItemClickListener.onEndReach();
    }

    @Override
    public int getItemCount() {
        return mGifts.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return ITEM_TYPE_EMPTY;
        else
            return ITEM_TYPE_GIFT;
    }

    public void setGifts(List<Gift> gifts) {
        mGifts = gifts;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Gift gift);
        void onEndReach();
    }
}
