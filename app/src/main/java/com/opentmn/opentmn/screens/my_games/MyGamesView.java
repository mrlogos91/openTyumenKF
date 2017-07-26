package com.opentmn.opentmn.screens.my_games;

import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.GameResult;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseView;

import java.util.List;

/**
 * Created by kost on 14.01.17.
 */

public interface MyGamesView extends BaseView {

    void showGames(List<Game> games);

    void startNewGame();

    void startHistory();

    void startLobby(Game game);

    void showWaitDialog(Game game);

    void showAcceptGameDialog(Game game);

    void showUser(User user);

    void showSoonDialog();

    void showNoFollowerDialog();

    void startGameResult(GameResult gameResult);

}
