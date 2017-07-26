package com.opentmn.opentmn.screens.lobby;

import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.GameResult;
import com.opentmn.opentmn.model.PushData;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseView;

/**
 * Created by kost on 14.01.17.
 */

public interface LobbyView extends BaseView {

    void showGame(Game game, int status);

    void startGame(Game game, int roundNumber);

    void startCategories(Game game, int roundNumber);

    void showCategoryNotSelectedDialog();

    void startResult(Game game, GameResult gameResult);

    void showSurrenderConfirmDialog();

    void showUserAddedDialog(User user);

    void showEnemyRoundNotFinishedDialog();

    void showDisabledButton();

    void showEnabledButton();

    void showStickerDialog(PushData pushData);

    void showTrainingDialog();
}
