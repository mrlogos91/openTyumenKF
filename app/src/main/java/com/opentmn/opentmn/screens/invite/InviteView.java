package com.opentmn.opentmn.screens.invite;

import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.SocialUser;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by kost on 22.01.17.
 */

public interface InviteView extends BaseView {

    void showUsers(List<SocialUser> users);

    Observable<TextViewTextChangeEvent> getTextChangedObservable();

    void showMessageSentDialog();

    void startProfile(User user);

    void showGameCreatedDialog(Game game);

    void showCaptchaDialog(String captchaImg, String captchaSid);
}
