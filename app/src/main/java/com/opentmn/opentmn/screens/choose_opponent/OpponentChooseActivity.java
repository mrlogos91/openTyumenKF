package com.opentmn.opentmn.screens.choose_opponent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.opentmn.opentmn.MainActivity;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.screens.category.CategoryActivity;
import com.opentmn.opentmn.screens.dialogs.MessageDialog;
import com.opentmn.opentmn.screens.invite.InviteActivity;
import com.opentmn.opentmn.screens.menu.MenuActivity;
import com.opentmn.opentmn.screens.profile.ProfileActivity;
import com.opentmn.opentmn.screens.rating.RatingAdapter;
import com.opentmn.opentmn.widget.Toolbar;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import com.opentmn.opentmn.R;

/**
 * Created by kost on 12.01.17.
 */

public class OpponentChooseActivity extends BaseActivity implements OpponentChooseView, RatingAdapter.OnItemClickListener {

    private final static String ONLY_FRIENDS_EXTRA = "only_friends";

    public static void startActivity(Context context, int onlyFriends) {
        Intent intent = new Intent(context, OpponentChooseActivity.class);
        intent.putExtra(ONLY_FRIENDS_EXTRA, onlyFriends);
        context.startActivity(intent);
    }

    private OpponentsAdapter mAdapter;
    private OpponentChoosePresenter mPresenter;
    private EditText mSearchEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opponent_choose);

        mSearchEditText = (EditText) findViewById(R.id.search_edit_text);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationImageRes(Toolbar.NAVIGATION_BACK_ICON);
        toolbar.setOnNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new OpponentsAdapter(this, this);
        recyclerView.setAdapter(mAdapter);

        int onlyFriends = getIntent().getIntExtra(ONLY_FRIENDS_EXTRA, 0);
        mPresenter = new OpponentChoosePresenter(this, onlyFriends);
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
    public Observable<TextViewTextChangeEvent> getTextChangedObservable() {
        return RxTextView.textChangeEvents(mSearchEditText);
    }

    @Override
    public void showGameCreatedDialog(Game game) {
        MessageDialog messageDialog = MessageDialog.getInstance("Игра создана", "Вы сможете начать игру как только ваш соперник подтвердит запрос");
        messageDialog.show(getSupportFragmentManager(), "game_created");
        messageDialog.setOnDismissListener(new MessageDialog.OnDismissListener() {
            @Override
            public void onDismiss() {
                MainActivity.startAfterGame(OpponentChooseActivity.this);
            }
        });
    }

    @Override
    public void onAddFriendClick(User user) {

    }

    @Override
    public void onPlayClick(User user) {
        mPresenter.onPlayClick(user);
    }

    @Override
    public void onDeleteFriendClick(User user) {

    }

    @Override
    public void onEndReached() {
        mPresenter.loadMore();
    }

    @Override
    public void onUserClick(User user) {
        mPresenter.onUserClick(user);
    }

    @Override
    public void startProfile(User user) {
        ProfileActivity.startActivity(this, user);
    }

    @Override
    public void startVkInvite() {
        InviteActivity.start(this);
    }
}
