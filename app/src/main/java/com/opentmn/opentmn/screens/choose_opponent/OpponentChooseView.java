package com.opentmn.opentmn.screens.choose_opponent;

import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by kost on 12.01.17.
 */

public interface OpponentChooseView extends BaseView {

    void showUsers(List<User> users);

    Observable<TextViewTextChangeEvent> getTextChangedObservable();

    void showGameCreatedDialog(Game game);

    void startProfile(User user);

    void startVkInvite();
}
