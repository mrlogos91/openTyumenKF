package com.opentmn.opentmn.screens.invite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.opentmn.opentmn.MainActivity;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.SocialUser;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.screens.dialogs.InputDialog;
import com.opentmn.opentmn.screens.dialogs.InviteDialog;
import com.opentmn.opentmn.screens.dialogs.MessageDialog;
import com.opentmn.opentmn.screens.profile.ProfileActivity;
import com.opentmn.opentmn.widget.Toolbar;

import java.util.List;

import rx.Observable;
import com.opentmn.opentmn.R;

/**
 * Created by kost on 22.01.17.
 */

public class InviteActivity extends BaseActivity implements InviteView, InviteAdapter.OnItemClickListener {


    public static void start(Context context) {
        Intent intent = new Intent(context, InviteActivity.class);
        context.startActivity(intent);
    }

    private InvitePresenter mPresenter;
    private InviteAdapter mAdapter;
    private EditText mSearchEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new InviteAdapter(this, this);
        recyclerView.setAdapter(mAdapter);

        mSearchEditText = (EditText) findViewById(R.id.search_edit_text);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationImageRes(Toolbar.NAVIGATION_BACK_ICON);
        toolbar.setOnNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mPresenter = new InvitePresenter(this);
        mPresenter.init();
    }

    @Override
    public Observable<TextViewTextChangeEvent> getTextChangedObservable() {
        return RxTextView.textChangeEvents(mSearchEditText);
    }

    @Override
    public void showUsers(List<SocialUser> users) {
        mAdapter.setUsers(users);
    }

    @Override
    public void onInviteClick(SocialUser socialUser) {
        InviteDialog giftDialog = InviteDialog.getInstance();
        giftDialog.show(getSupportFragmentManager(), "gift");
        giftDialog.setOnDialogButtonClickListener(new InviteDialog.OnDialogButtonClickListener() {
            @Override
            public void onConfirmClick(DialogFragment dialogFragment, String text) {
                if(text.length() == 0)
                    return;
                dialogFragment.dismiss();
                mPresenter.onInviteClick(getApplicationContext(), socialUser, text);
            }
        });

    }

    @Override
    public void onUserClick(User user) {
        mPresenter.onUserClick(user);
    }

    @Override
    public void onDeleteFriendClick(User user) {
        mPresenter.onDeleteFriendClick(user);
    }

    @Override
    public void onPlayClick(User user) {
        mPresenter.onPlayClick(user);
    }

    @Override
    public void onAddFriendClick(User user) {
        mPresenter.onAddFriendClick(user);
    }

    @Override
    public void showMessageSentDialog() {
        MessageDialog messageDialog = MessageDialog.getInstance("ИНФОРМАЦИЯ", "Приглашение отправлено!");
        messageDialog.show(getSupportFragmentManager(), "invite");
    }

    @Override
    public void startProfile(User user) {
        ProfileActivity.startActivity(this, user);
    }

    @Override
    public void showGameCreatedDialog(Game game) {
        MessageDialog messageDialog = MessageDialog.getInstance("Игра создана", "Вы сможете начать игру как только ваш соперник подтвердит запрос");
        messageDialog.show(getSupportFragmentManager(), "game_created");
        messageDialog.setOnDismissListener(new MessageDialog.OnDismissListener() {
            @Override
            public void onDismiss() {
                MainActivity.startAfterGame(InviteActivity.this);
            }
        });
    }

    @Override
    public void showCaptchaDialog(String captchaImg, String captchaSid) {
        InputDialog inputDialog = InputDialog.getInstance(captchaImg);
        inputDialog.show(getSupportFragmentManager(), null);
        inputDialog.setOnDialogButtonClickListener(new InputDialog.OnDialogButtonClickListener() {
            @Override
            public void onConfirmClick(DialogFragment dialogFragment, String text) {
                if(text.length() == 0)
                    return;
                dialogFragment.dismiss();
                mPresenter.loadUsers(captchaSid, text);
            }
        });
    }
}
