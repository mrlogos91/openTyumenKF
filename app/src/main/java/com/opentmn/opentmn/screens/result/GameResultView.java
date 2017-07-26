package com.opentmn.opentmn.screens.result;

import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.GameResult;
import com.opentmn.opentmn.screens.base.BaseView;

/**
 * Created by kost on 11.01.17.
 */

public interface GameResultView extends BaseView {

    void showResult(GameResult gameResult);

    void showMyGames();

    void startLobby(Game game);

    void showVkShareDialog(String text, String link);

    void showFbShareDialog(String text, String link);

    void showSmileSentDialog();

    void showStickerErrorDialog();

}
