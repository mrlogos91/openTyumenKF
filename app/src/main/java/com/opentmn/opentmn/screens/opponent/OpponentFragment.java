package com.opentmn.opentmn.screens.opponent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.opentmn.opentmn.MainActivity;
import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.SocialUser;
import com.opentmn.opentmn.screens.base.BaseFragment;
import com.opentmn.opentmn.screens.category.CategoryActivity;
import com.opentmn.opentmn.screens.choose_opponent.OpponentChooseActivity;
import com.opentmn.opentmn.screens.dialogs.MessageDialog;
import com.opentmn.opentmn.screens.invite.InviteActivity;
import com.opentmn.opentmn.screens.lobby.LobbyActivity;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import butterknife.ButterKnife;
import butterknife.OnClick;
import сom.opentmn.opentmn.R;

import static com.opentmn.opentmn.Config.LOG_TAG;

/**
 * Created by kost on 04.01.17.
 */

public class OpponentFragment extends BaseFragment implements OpponentView {

    private OpponentPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_opponent, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        mPresenter = new OpponentPresenter(this);
    }

    @OnClick(R.id.invite_vk_button)
    void inviteVkButtonClick() {
        mPresenter.onInviteVkClick(getActivity());
    }

    @Override
    public void startVkInvite() {
        InviteActivity.start(getActivity());
    }

    @OnClick(R.id.play_friend_button)
    void playFriendClick() {
        OpponentChooseActivity.startActivity(getActivity(), 1);
    }

    @OnClick(R.id.play_random_button)
    void playRandomClick() {
        mPresenter.onRandomClick();
    }

    @OnClick(R.id.play_search_button)
    void playSearchClick() {
        OpponentChooseActivity.startActivity(getActivity(), 0);
    }

    @OnClick(R.id.play_training_button)
    void playTrainingClick() {
        mPresenter.onTrainingClick();
    }

    @Override
    public void startLobby(Game game) {
        LobbyActivity.startActivity(getActivity(), game, true);
    }

    @Override
    public void showGameCreatedDialog(Game game) {
        MessageDialog messageDialog = MessageDialog.getInstance(getString(R.string.alert_game_created_title), getString(R.string.alert_game_created_message));
        messageDialog.show(getActivity().getSupportFragmentManager(), null);
        messageDialog.setOnDismissListener(new MessageDialog.OnDismissListener() {
            @Override
            public void onDismiss() {
                MainActivity.startAfterGame(getActivity());
            }
        });
    }

    @Override
    public void startFriendList() {

    }

    @Override
    public void startUserList() {

    }
}
