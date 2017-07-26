package com.opentmn.opentmn.screens.rating;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.opentmn.opentmn.MainActivity;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseFragment;
import com.opentmn.opentmn.screens.dialogs.MessageDialog;
import com.opentmn.opentmn.screens.profile.ProfileActivity;

import java.util.List;

import rx.Observable;
import сom.opentmn.opentmn.R;

/**
 * Created by kost on 23.12.16.
 */

public class RatingFragment extends BaseFragment implements RatingView, RatingAdapter.OnItemClickListener {

    private RatingPresenter mRatingPresenter;
    private RatingAdapter mAdapter;
    private EditText mSearchEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rating, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchEditText = (EditText) view.findViewById(R.id.search_edit_text);
        setupRecyclerView();
        mRatingPresenter = new RatingPresenter(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRatingPresenter.init();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new RatingAdapter(getActivity(), this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showUsers(List<User> users) {
        mAdapter.setUserList(users);
    }

    @Override
    public Observable<TextViewTextChangeEvent> getTextChangedObservable() {
        return RxTextView.textChangeEvents(mSearchEditText);
    }

    @Override
    public void onAddFriendClick(User user) {
        mRatingPresenter.onAddFriendClick(user);
    }

    @Override
    public void onDeleteFriendClick(User user) {
        mRatingPresenter.onDeleteFriendClick(user);
    }

    @Override
    public void onPlayClick(User user) {
        mRatingPresenter.onPlayClick(user);
    }

    @Override
    public void onUserClick(User user) {
        mRatingPresenter.onUserClick(user);
    }

    @Override
    public void startProfile(User user) {
        ProfileActivity.startActivity(getActivity(), user);
    }

    @Override
    public void onEndReached() {
        mRatingPresenter.loadMore();
    }

    @Override
    public void showUserAddedDialog(User user) {
    }

    @Override
    public void showUserDeletedDialog(User user) {
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
}
