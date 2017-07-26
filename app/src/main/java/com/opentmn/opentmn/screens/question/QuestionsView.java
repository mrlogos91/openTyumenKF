package com.opentmn.opentmn.screens.question;

import com.opentmn.opentmn.model.Category;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.GameResult;
import com.opentmn.opentmn.model.Question;
import com.opentmn.opentmn.screens.base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kost on 26.12.16.
 */

public interface QuestionsView extends BaseView {

    void showQuestions(List<Question> questions);

    void nextRound(Game game, ArrayList<Category> categories);

    void showResults(Game game, GameResult result);

    void showQuestion(int number);

    void setGame(Game game);

    void showMessageDialog(String title, String subtitle);

    void startLobby(Game game);

    void leave();

}
