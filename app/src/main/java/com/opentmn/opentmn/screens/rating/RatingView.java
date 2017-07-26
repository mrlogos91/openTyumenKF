package com.opentmn.opentmn.screens.rating;

import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseView;

import java.util.List;

import butterknife.OnTextChanged;
import rx.Observable;

/**
 * Created by kost on 24.12.16.
 */

public interface RatingView extends BaseView {

    void showUsers(List<User> users);

    void startProfile(User user);

    Observable<TextViewTextChangeEvent> getTextChangedObservable();

    void showUserAddedDialog(User user);

    void showUserDeletedDialog(User user);

    void showGameCreatedDialog(Game game);
}
