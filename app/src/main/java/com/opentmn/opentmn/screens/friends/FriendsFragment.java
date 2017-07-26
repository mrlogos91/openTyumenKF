package com.opentmn.opentmn.screens.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.opentmn.opentmn.MainActivity;
import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.SocialUser;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseFragment;
import com.opentmn.opentmn.screens.dialogs.MessageDialog;
import com.opentmn.opentmn.screens.invite.InviteActivity;
import com.opentmn.opentmn.screens.menu.MenuActivity;
import com.opentmn.opentmn.screens.profile.ProfileActivity;
import com.opentmn.opentmn.screens.rating.RatingAdapter;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import сom.opentmn.opentmn.R;

import static com.opentmn.opentmn.Config.LOG_TAG;

/**
 * Created by kost on 01.01.17.
 */

public class FriendsFragment extends BaseFragment implements FriendsView, RatingAdapter.OnItemClickListener {

    private FriendsPresenter mPresenter;
    private FriendsAdapter mAdapter;
    private EditText mSearchEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        mSearchEditText = (EditText) view.findViewById(R.id.search_edit_text);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new FriendsAdapter(getActivity(), this);
        recyclerView.setAdapter(mAdapter);

        mPresenter = new FriendsPresenter(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.init();
    }

    @OnClick(R.id.invite_vk_button)
    void inviteVkButtonClick() {
        mPresenter.onInviteButtonClick(getActivity());
    }

    @Override
    public void showFriends(List<User> users) {
        mAdapter.setFriends(users);
    }

    @Override
    public Observable<TextViewTextChangeEvent> getTextChangedObservable() {
        return RxTextView.textChangeEvents(mSearchEditText);
    }

    @Override
    public void onUserClick(User user) {
        mPresenter.onUserClick(user);
    }

    @Override
    public void onAddFriendClick(User user) {

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
    public void onEndReached() {
        //TODO: pagination
    }

    @Override
    public void startProfile(User user) {
        ProfileActivity.startActivity(getActivity(), user);
    }

    @Override
    public void showUserDeletedDialog(User user) {

    }

    @Override
    public void showGameCreatedDialog(Game game) {
        MessageDialog messageDialog = MessageDialog.getInstance(getString(R.string.alert_game_created_title), getString(R.string.alert_game_created_message));
        messageDialog.show(getActivity().getSupportFragmentManager(), "game_created");
        messageDialog.setOnDismissListener(new MessageDialog.OnDismissListener() {
            @Override
            public void onDismiss() {
                MainActivity.startAfterGame(getActivity());
            }
        });
    }

    @Override
    public void startVkInvite() {
        InviteActivity.start(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                String email =  res.email;
                String id =  res.userId;
                String token =  res.accessToken;
                Log.d(LOG_TAG, email + " " + id + " " + token);

                SocialUser socialUser = new SocialUser(id, token);
                RepositoryProvider.provideKeyValueStorage().setSocialUser(socialUser);
                mPresenter.onInviteButtonClick(getActivity());
            }
            @Override
            public void onError(VKError error) {
                //showToast(getString(R.string.login_error));
                Log.d(LOG_TAG, "User didn't pass Authorization");
                // User didn't pass Authorization
            }
        })){

        }
    }

}
