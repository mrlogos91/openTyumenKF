package com.opentmn.opentmn.screens.question;

import android.util.Log;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.keyvalue.KeyValueStorage;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.Question;
import com.opentmn.opentmn.model.Round;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.utils.RxSchedulers;

import java.util.List;

import rx.Subscription;

/**
 * Created by kost on 26.12.16.
 */

public class QuestionsPresenter {

    private static final String TAG = "QuestionsPresenter";

    private QuestionsView mQuestionsView;

    private Game mGame;
    private int mRoundNumber;
    private Round mRound;
    private List<Question> mQuestions;
    private MyTyumenRepository mRepository;
    private User mUser;

    public QuestionsPresenter(QuestionsView questionsView, Game game, int roundNumber) {
        mQuestionsView = questionsView;
        mGame = game;
        mRoundNumber = roundNumber;
        mRound = game.getRounds().get(mRoundNumber);
        mRepository = RepositoryProvider.provideRepository();
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
        RepositoryProvider.provideKeyValueStorage().setGameId(mGame.getId());
    }

    public void init() {
        Log.d(TAG, "init");
        Log.d(TAG, "roundNumber " + String.valueOf(mRoundNumber));
        List<Question> questions = mRound.getQuestions();
        Log.d(TAG, "questions " + String.valueOf(questions.size()));
        mQuestions = questions;
        mQuestionsView.showQuestions(questions);
        showRoundDialog();
        /*if(questions == null || questions.size() == 0) {
            Log.d(TAG, "questions null or 0");
            loadQuestions();
        } else {
            Log.d(TAG, "questions " + String.valueOf(questions.size()));
            mQuestions = questions;
            mQuestionsView.showQuestions(questions);
            showRoundDialog();
        }*/
    }

    private void showRoundDialog() {
        String title = mRound.getCategory().getName();
        String subtitle = "Раунд " + String.valueOf(mRoundNumber + 1);
        mQuestionsView.showMessageDialog(title, subtitle);
    }

    public void loadQuestions() {
        Subscription subscription = mRepository.questions(mRound.getCategory().getId(), mUser.getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mQuestionsView::showProgress)
                .doAfterTerminate(mQuestionsView::hideProgress)
                .subscribe(data -> {
                    if(data.isSuccess()){
                        mQuestions = data.getData();
                        if(mQuestions.size() == 2)
                            mQuestions.add(new Question(mQuestions.get(0)));
                        mRound.setQuestions(mQuestions);
                        mQuestionsView.showQuestions(mQuestions);
                        showRoundDialog();
                    } else {
                        mQuestionsView.showApiResponseError(data, null);
                    }

                }, throwable -> {
                    mQuestionsView.showNetworkError(null);
                });
        mQuestionsView.addSubscription(subscription);
    }

    /*public void startNextRoundOrFinish() {
        String token = RepositoryProvider.provideKeyValueStorage().getUser().getToken();
        if(mRoundNumber < 3) {
            Log.d(TAG, "new round");
            Subscription subscription = mRepository.createRound(mGame.getId(), category.getId(), token)
                    .compose(RxSchedulers.async())
                    .subscribe(data -> {
                        if (data.isSuccess()) {
                            mGame = data.getData();
                            mQuestionsView.nextRound(mGame, mCategoryList);
                        } else {
                            mQuestionsView.showApiResponseError(data, null);
                        }

                    }, throwable -> {
                        mQuestionsView.showNetworkError(null);
                    });
            mQuestionsView.addSubscription(subscription);
        } else {
            Log.d(TAG, "results");
            Subscription subscription = mRepository.result(mGame.getId(), token)
                    .compose(RxSchedulers.async())
                    .subscribe(data -> {
                        Log.d(TAG, "results data");
                        if(data.isSuccess()) {
                            GameResult gameResult = data.getData();
                            mQuestionsView.showResults(mGame, gameResult);
                        } else {
                            mQuestionsView.showNetworkError(null);
                        }
                    }, throwable -> {
                        Log.d(TAG, "throwable");
                        mQuestionsView.showNetworkError(null);
                    });
            mQuestionsView.addSubscription(subscription);
        }
    }*/

    public void questionAnswered(int questionNumber) {
        if (questionNumber < mRound.getQuestions().size() - 1) {
            mQuestionsView.setGame(mGame);
            mQuestionsView.showQuestions(mRound.getQuestions());
            mQuestionsView.showQuestion(questionNumber + 1);
        } else {
            RepositoryProvider.provideKeyValueStorage().setGameId(KeyValueStorage.NO_GAME_ID);
            mQuestionsView.startLobby(mGame);
        }
    }

    public void setGame(Game game) {
        mGame = game;
        mRound = game.getRounds().get(mRoundNumber);
    }

    public Game getGame() {
        return mGame;
    }

    public void onLeaveConfirmClick() {
        Subscription subscription = mRepository.closeGame(mGame.getId(), mUser.getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mQuestionsView::showProgress)
                .doAfterTerminate(mQuestionsView::hideProgress)
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        mQuestionsView.leave();
                    } else {
                        mQuestionsView.showApiResponseError(response, () -> onLeaveConfirmClick());
                    }
                }, throwable -> {
                    mQuestionsView.showNetworkError(() -> onLeaveConfirmClick());
                });
        mQuestionsView.addSubscription(subscription);
    }
}
