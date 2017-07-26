package com.opentmn.opentmn.screens.history;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.widget.TextView;
import com.opentmn.opentmn.widget.Toolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import сom.opentmn.opentmn.R;

/**
 * Created by kost on 21.01.17.
 */

public class HistoryActivity extends BaseActivity implements HistoryView, HistoryAdapter.OnItemClickListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, HistoryActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindViews({R.id.wins_text_view, R.id.draws_text_view, R.id.loses_text_view})
    TextView[] mTabTextViews;

    private HistoryPresenter mPresenter;
    private HistoryAdapter mAdapter;
    private int mSelectedTab = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        mToolbar.setNavigationImageRes(Toolbar.NAVIGATION_BACK_ICON);
        mToolbar.setOnNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setupTabs();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HistoryAdapter(this, this);
        recyclerView.setAdapter(mAdapter);

        mPresenter = new HistoryPresenter(this);
        mPresenter.init();
    }

    private void setupTabs() {
        mTabTextViews[0].setBackgroundDrawable(null);
        mTabTextViews[1].setBackgroundDrawable(null);
        mTabTextViews[2].setBackgroundDrawable(null);
        mTabTextViews[0].setTextColor(getResources().getColor(R.color.history_text_green));
        mTabTextViews[1].setTextColor(getResources().getColor(R.color.history_text_yellow));
        mTabTextViews[2].setTextColor(getResources().getColor(R.color.history_text_red));
    }

    @OnClick(R.id.wins_text_view)
    void winsClick() {
        setupTabs();
        if(mSelectedTab == 0) {
            mSelectedTab = -1;
        } else {
            mSelectedTab = 0;
            mTabTextViews[0].setBackgroundResource(R.mipmap.game_history_substrate_1);
            mTabTextViews[0].setTextColor(Color.WHITE);
        }
        mPresenter.tabSelected(mSelectedTab);
    }

    @OnClick(R.id.draws_text_view)
    void drawsClick() {
        setupTabs();
        if(mSelectedTab == 1) {
            mSelectedTab = -1;
        } else {
            mSelectedTab = 1;
            mTabTextViews[1].setBackgroundResource(R.mipmap.game_history_substrate_2);
            mTabTextViews[1].setTextColor(Color.WHITE);
        }
        mPresenter.tabSelected(mSelectedTab);
    }

    @OnClick(R.id.loses_text_view)
    void losesClick() {
        setupTabs();
        if(mSelectedTab == 2) {
            mSelectedTab = -1;
        } else {
            mSelectedTab = 2;
            mTabTextViews[2].setBackgroundResource(R.mipmap.game_history_substrate_3);
            mTabTextViews[2].setTextColor(Color.WHITE);
        }
        mPresenter.tabSelected(mSelectedTab);
    }

    @Override
    public void showGames(List<Game> games) {
        mAdapter.setGames(games);
    }

    @Override
    public void onEndReached() {
        mPresenter.loadMore();
    }
}
