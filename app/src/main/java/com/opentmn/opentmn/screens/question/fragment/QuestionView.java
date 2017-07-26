package com.opentmn.opentmn.screens.question.fragment;

import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.Question;
import com.opentmn.opentmn.screens.base.BaseView;

/**
 * Created by kost on 11.01.17.
 */

public interface QuestionView extends BaseView {

    void showCategoryName(String categoryName);

    void showQuestion(Question question);

    void disableButtons();

    void showRightAnswer(int index);

    void showWrongAnswer(int index);

    void showQuestionResult(int index, boolean success);

    void questionAnswered(int questionNumber, Game game);

    void showAnswersResults(String[] answers, int roundNumber);

    void showTimerProgress(int progress);

    void showHintView();
}
