package com.opentmn.opentmn.screens.shop;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.opentmn.opentmn.MainActivity;
import com.opentmn.opentmn.model.Gift;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseFragment;
import com.opentmn.opentmn.screens.gift_detail.GiftDetailActivity;
import com.opentmn.opentmn.widget.Toolbar;

import java.util.List;

import сom.opentmn.opentmn.R;

/**
 * Created by kost on 03.01.17.
 */

public class GiftsFragment extends BaseFragment implements GiftsView, GiftsAdapter.OnItemClickListener {

    private GiftsPresenter mPresenter;
    private GiftsAdapter mAdapter;

    private android.support.v7.widget.Toolbar mHeaderLayout;
    private TextView mCoinsTextView;
    private int mHeaderHeight;
    private boolean mAnimating = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHeaderLayout = (android.support.v7.widget.Toolbar) view.findViewById(R.id.header_layout);
        mHeaderHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.shop_header_height);
        mCoinsTextView = (TextView) view.findViewById(R.id.coins_text_view);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new GiftsAdapter(getActivity(), this);
        recyclerView.setAdapter(mAdapter);
        setupHeaderAnimation(recyclerView);
        mPresenter = new GiftsPresenter(this);
        mPresenter.init();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    private void setupHeaderAnimation(RecyclerView recyclerView) {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mAnimating)
                    return;
                if (dy > 0 && mHeaderLayout.getY() > -mHeaderHeight) {
                    mHeaderLayout.setY((int) mHeaderLayout.getY() - dy);
                } else if (dy < 0 && mHeaderLayout.getY() != 0) {
                    mAnimating = true;
                    mHeaderLayout.animate().setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            mAnimating = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    }).y(0);
                }
            }
        });
    }

    @Override
    public void showGifts(List<Gift> gifts) {
        mAdapter.setGifts(gifts);
    }

    @Override
    public void onItemClick(Gift gift) {
        mPresenter.onGiftClick(gift);
    }

    @Override
    public void showGiftDetail(Gift gift) {
        GiftDetailActivity.startActivity(getActivity(), gift);
    }

    @Override
    public void showCoins(int coins) {
        mCoinsTextView.setText(String.valueOf(coins));
    }

    @Override
    public void showUser(User user) {
        mCoinsTextView.setText(String.valueOf(user.getCoins()));
    }

    @Override
    public void onEndReach() {
        mPresenter.loadMore();
    }
}
