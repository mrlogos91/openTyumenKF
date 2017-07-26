package com.opentmn.opentmn.screens.question.fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.Answer;
import com.opentmn.opentmn.model.AnswerAlias;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.Question;
import com.opentmn.opentmn.model.Round;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.utils.RxSchedulers;

import java.util.List;

import rx.Subscription;

/**
 * Created by kost on 11.01.17.
 */

public class QuestionPresenter {

    private QuestionView mQuestionView;
    private Game mGame;
    private int mQuestionNumber;
    private int mRoundNumber;
    private Question mQuestion;
    private Round mRound;
    private CountDownTimer mCountDownTimer;
    private Handler mHandler;
    private User mUser;
    private MyTyumenRepository mRepository;

    public QuestionPresenter(QuestionView questionView, Game game, int roundNumber, int questionNumber) {
        mQuestionView = questionView;
        mGame = game;
        mQuestionNumber = questionNumber;
        mRoundNumber = roundNumber;
        mRound = mGame.getRounds().get(mRoundNumber);
        mQuestion = mRound.getQuestions().get(mQuestionNumber);
        mHandler = new Handler();
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
        mRepository = RepositoryProvider.provideRepository();
    }

    public void init() {
        mQuestionView.showCategoryName(mRound.getCategory().getName());
        mQuestionView.showQuestion(mQuestion);
        List<Question> questions = mRound.getQuestions();
        String[] answersResults = new String[3];
        boolean isCreator = mGame.isCreator(mUser);
        for(int i = 0; i < questions.size(); i++) {
            answersResults[i] = isCreator ? questions.get(i).getCreatorAnswer() : questions.get(i).getFollowerAnswer();
        }
        mQuestionView.showAnswersResults(answersResults, mQuestionNumber);
        mQuestionView.showTimerProgress(20000);
    }

    public void startTimer() {
        mCountDownTimer = new CountDownTimer(20000, 50) {

            @Override
            public void onTick(long millisUntilFinished) {
                long progress = millisUntilFinished;
                int progressInt = (int) progress;
                mQuestionView.showTimerProgress(progressInt);
            }

            @Override
            public void onFinish() {
                mQuestionView.showTimerProgress(0);
                onTimerEnd();
            }
        };
        mCountDownTimer.start();
    }

    private void onTimerEnd() {
        mQuestionView.disableButtons();
        mQuestion.setUserAnswer(AnswerAlias.ANSWER_NONE);
        mQuestionView.showQuestionResult(mQuestionNumber, false);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mQuestionView.showHintView();
            }
        }, 3000);
    }

    public void stopTimer() {
        if(mCountDownTimer != null)
            mCountDownTimer.cancel();
    }

    public void onAnswerClicked(int index) {
        stopTimer();
        mQuestionView.disableButtons();
        Answer answer = mQuestion.getAnswers().get(index);
        mQuestion.setUserAnswer(answer.getAlias());
        if(answer.getAlias().equals(AnswerAlias.ANSWER_TRUE)) {
            mQuestionView.showRightAnswer(index);
            mQuestionView.showQuestionResult(mQuestionNumber, true);
        } else {
            mQuestionView.showWrongAnswer(index);
            for(int i = 0; i < mQuestion.getAnswers().size(); i++) {
                if(mQuestion.getAnswers().get(i).getAlias().equals(AnswerAlias.ANSWER_TRUE)) {
                    mQuestionView.showRightAnswer(i);
                }
            }
            mQuestionView.showQuestionResult(mQuestionNumber, false);
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mQuestionView.showHintView();
            }
        }, 3000);
    }

    public void sendAnswer(Question question) {
        Subscription subscription = mRepository.sendAnswer(mRound.getId(), question.getId(), question.getUserAnswer(), mUser.getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mQuestionView::showProgress)
                .doOnNext(response -> mQuestionView.hideProgress())
                .doOnError(error -> mQuestionView.hideProgress())
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        Game game = response.getData();
                        mQuestionView.questionAnswered(mQuestionNumber, game);
                    } else {
                        mQuestionView.showApiResponseError(response, () -> sendAnswer(question));
                    }
                }, throwable -> {
                    mQuestionView.showNetworkError(() -> sendAnswer(question));
                });
        //mQuestionView.addSubscription(subscription);
    }

    public void onQuestionViewClick() {
        if(mHandler != null)
            mHandler.removeCallbacksAndMessages(null);
        sendAnswer(mQuestion);
        //mQuestionView.questionAnswered(mQuestionNumber, mQuestion.getUserAnswer());
    }

    public void setGame(Game game) {
        Log.d("QuestionPresenter", "setGame");
        Log.d("QuestionPresenter", "round: " + String.valueOf(mRoundNumber));
        mGame = game;
        mRound = mGame.getRounds().get(mRoundNumber);
        mQuestion = mRound.getQuestions().get(mQuestionNumber);
    }


}
