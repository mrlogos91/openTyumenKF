package com.opentmn.opentmn.screens.friends;

import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by kost on 07.01.17.
 */

public interface FriendsView extends BaseView {

    void showFriends(List<User> users);

    Observable<TextViewTextChangeEvent> getTextChangedObservable();

    void startProfile(User user);

    void showUserDeletedDialog(User user);

    void showGameCreatedDialog(Game game);

    void startVkInvite();
}
