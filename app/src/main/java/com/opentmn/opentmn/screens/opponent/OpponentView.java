package com.opentmn.opentmn.screens.opponent;

import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.screens.base.BaseView;

/**
 * Created by kost on 14.01.17.
 */

public interface OpponentView extends BaseView {

    void startLobby(Game game);

    void startFriendList();

    void startUserList();

    void showGameCreatedDialog(Game game);

    void startVkInvite();
}
