package com.opentmn.opentmn.screens.history;

import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.screens.base.BaseView;

import java.util.List;

/**
 * Created by kost on 21.01.17.
 */

public interface HistoryView extends BaseView {

    void showGames(List<Game> games);

}
