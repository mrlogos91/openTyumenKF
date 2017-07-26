package com.opentmn.opentmn.screens.blacklist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.screens.profile.ProfileActivity;
import com.opentmn.opentmn.widget.EditText;
import com.opentmn.opentmn.widget.Toolbar;

import java.util.List;

import rx.Observable;
import com.opentmn.opentmn.R;

/**
 * Created by kost on 18.01.17.
 */

public class BlackListActivity extends BaseActivity implements BlackListView, BlackListAdapter.OnItemClickListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, BlackListActivity.class);
        context.startActivity(intent);
    }

    private BlackListAdapter mAdapter;
    private BlackListPresenter mPresenter;
    private EditText mSearchEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationImageRes(Toolbar.NAVIGATION_BACK_ICON);
        toolbar.setOnNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BlackListAdapter(this, this);
        recyclerView.setAdapter(mAdapter);

        mSearchEditText = (EditText) findViewById(R.id.search_edit_text);

        mPresenter = new BlackListPresenter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.init();
    }

    @Override
    public void showUsers(List<User> users) {
        mAdapter.setUsers(users);
    }

    @Override
    public void onBlockClick(User user) {
        mPresenter.onBlockClick(user);
    }

    @Override
    public void onEndReached() {

    }

    @Override
    public void onUserClick(User user) {
        ProfileActivity.startActivity(this, user);
    }

    @Override
    public Observable<TextViewTextChangeEvent> getTextChangedObservable() {
        return RxTextView.textChangeEvents(mSearchEditText);
    }
}
