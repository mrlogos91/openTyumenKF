package com.opentmn.opentmn.screens.profile;

import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseView;

/**
 * Created by kost on 04.01.17.
 */

public interface ProfileView extends BaseView {

    void showUser(User user);

    void showGameCreatedDialog(Game game);

    void startBlackList();
}
