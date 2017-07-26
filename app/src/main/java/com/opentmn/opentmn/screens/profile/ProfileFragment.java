package com.opentmn.opentmn.screens.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.opentmn.opentmn.MainActivity;
import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseFragment;
import com.opentmn.opentmn.screens.blacklist.BlackListActivity;
import com.opentmn.opentmn.screens.dialogs.MessageDialog;
import com.opentmn.opentmn.screens.friends.FriendsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.opentmn.opentmn.R;

/**
 * Created by kost on 31.12.16.
 */

public class ProfileFragment extends BaseFragment implements ProfileView, ProfileAdapter.OnItemClickListener {

    public static final int REQ_CODE_EDIT = 1;

    private ProfilePresenter mPresenter;
    private ProfileAdapter mAdapter;

    public static ProfileFragment getInstance(User user) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(ProfileActivity.EXTRA_USER_KEY, user);
        profileFragment.setArguments(args);
        return profileFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ProfileAdapter(getActivity(), this);
        recyclerView.setAdapter(mAdapter);

        User user = null;
        Bundle args = getArguments();
        if(args != null)
            user = (User) args.getSerializable(ProfileActivity.EXTRA_USER_KEY);
        mPresenter = new ProfilePresenter(this, user);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.init();
    }

    @Override
    public void showUser(User user) {
        mAdapter.setUser(user);
    }

    @Override
    public void onPlayClick(User user) {
        mPresenter.onPlayClick(user);
    }

    @Override
    public void onBlackListClick() {
        mPresenter.onBlackListClick();
    }

    @Override
    public void onBlockClick() {
        mPresenter.onBlockClick();
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
    public void startBlackList() {
        BlackListActivity.start(getActivity());
    }

    @Override
    public void onFriendsClick() {
        FriendsActivity.startActivity(getActivity());
    }

    @Override
    public void onAddFriendClick() {
        mPresenter.onAddFriendClick();
    }
}
