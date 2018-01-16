package com.opentmn.opentmn.screens.lobby;

import android.util.Log;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.keyvalue.KeyValueStorage;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.GameResult;
import com.opentmn.opentmn.model.GameStatus;
import com.opentmn.opentmn.model.GameType;
import com.opentmn.opentmn.model.PushData;
import com.opentmn.opentmn.model.Round;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.network.model.ApiResponseModel;
import com.opentmn.opentmn.utils.RxSchedulers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by kost on 14.01.17.
 */

public class LobbyPresenter {

    private final String TAG = "LobbyPresenter";

    private LobbyView mLobbyView;
    private Game mGame;
    private MyTyumenRepository mRepository;
    private KeyValueStorage mStorage;
    private User mUser;
    private boolean mIsNewGame;

    public LobbyPresenter(LobbyView lobbyView, Game game, boolean isNewGame) {
        mLobbyView = lobbyView;
        mGame = game;
        mRepository = RepositoryProvider.provideRepository();
        mStorage = RepositoryProvider.provideKeyValueStorage();
        mUser = mStorage.getUser();
        mIsNewGame = isNewGame;
    }

    public void onCreate() {
        if(mIsNewGame && mGame.getGameTypeId() == GameType.TRAINING) {
            mLobbyView.showTrainingDialog();
        }
    }

    private void setupGame(Boolean fromServer) {
        int status = mGame.getActiveStatus(mUser);
        if(status == GameStatus.ALL_ANSWERED || (status >= GameStatus.ENEMY_ROUND_1 && status <= GameStatus.ENEMY_ROUND_3) || status == GameStatus.ENEMY_CHOOSE_CATEGORY) {
            mLobbyView.showDisabledButton();
        } else {
            mLobbyView.showEnabledButton();
        }
        mLobbyView.showGame(mGame, status);
        if(status == GameStatus.ALL_ANSWERED || mGame.getIsFinished() == 1) {
            result(0);
        } else if(fromServer) {
            PushData pushData = mStorage.getSavedPushData();
            if(pushData != null) {
                mLobbyView.showStickerDialog(pushData);
                mStorage.setSavedPushData(pushData);
            }
        }
    }

    public void init() {
        setupGame(false);
        Subscription subscription = rx.Observable.interval(0, 10, TimeUnit.SECONDS)
                .compose(RxSchedulers.async())
                .flatMap(new Func1<Long, rx.Observable<ApiResponseModel<Game>>>() {
                    @Override
                    public rx.Observable<ApiResponseModel<Game>> call(Long aLong) {
                        Log.d(TAG, String.valueOf(aLong));
                        Observable<ApiResponseModel<Game>> observable = mRepository.game(mGame.getId(), mUser.getToken()).compose(RxSchedulers.async());
                        if(aLong == 0)
                            return observable
                                    .doOnSubscribe(mLobbyView::showProgress)
                                    .doAfterTerminate(mLobbyView::hideProgress);
                        else
                            return observable;
                    }
                })
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        mGame = response.getData();
                        setupGame(true);
                    } else {

                    }
                }, throwable -> {
                    mLobbyView.showNetworkError(() -> init());
                });
        mLobbyView.addSubscription(subscription);
    }

    public void onPlayClick() {
        Subscription subscription = mRepository.game(mGame.getId(), mUser.getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mLobbyView::showProgress)
                .doOnNext(data -> mLobbyView.hideProgress())
                .doOnError(error -> mLobbyView.hideProgress())
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        mGame = response.getData();
                        init();
                        startNextRoundOrFinish();
                    } else {
                        mLobbyView.showApiResponseError(response, () -> onPlayClick());
                    }
                }, throwable -> {
                    mLobbyView.showNetworkError(() -> onPlayClick());
                });
        mLobbyView.addSubscription(subscription);
    }

    private void startNextRoundOrFinish() {
        if(isGameFinished()) {
            result(0);
        } else {
            List<Round> rounds = mGame.getRounds();
            boolean isCreator = mGame.isCreator(mUser);
            for(int i = 0; i < rounds.size(); i++) {
                if(!rounds.get(i).isRoundFinished(isCreator, false)) {
                    if(i > 0 && !rounds.get(i - 1).isRoundFinished(!isCreator, true)) {
                        mLobbyView.showEnemyRoundNotFinishedDialog();
                        return;
                    } else {
                        mLobbyView.startGame(mGame, i);
                        return;
                    }
                }
            }
            if(rounds.size() == 1 && isCreator) {
                if(rounds.get(0).isRoundFinished(!isCreator, true))
                    mLobbyView.startCategories(mGame, 1);
                else
                    mLobbyView.showEnemyRoundNotFinishedDialog();
            } else if(rounds.size() == 2 && mGame.getGameTypeId() == GameType.TRAINING) {
                mLobbyView.startCategories(mGame, 2);
            } else if(rounds.size() == 2 && !isCreator) {
                if(rounds.get(1).isRoundFinished(!isCreator, true))
                    mLobbyView.startCategories(mGame, 2);
                else
                    mLobbyView.showEnemyRoundNotFinishedDialog();
            } else {
                int count = mGame.getRoundFinishedCount(mUser, false);
                int enemyCount = mGame.getRoundFinishedCount(isCreator ? mGame.getFollower() : mGame.getCreator(), true);
                if(count > enemyCount)
                    mLobbyView.showEnemyRoundNotFinishedDialog();
                else
                    mLobbyView.showCategoryNotSelectedDialog();
            }
        }
    }

    public void onSurrenderClick() {
        mLobbyView.showSurrenderConfirmDialog();
    }

    public void onSurrenderConfirmClick() {
        result(1);
    }

    private void result(int isSurrender) {
        Subscription subscription = mRepository.result(mGame.getId(), isSurrender, mUser.getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mLobbyView::showProgress)
                .doAfterTerminate(mLobbyView::hideProgress)
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        GameResult gameResult = response.getData();
                        mUser.setRating(gameResult.getRating());
                        RepositoryProvider.provideKeyValueStorage().setUser(mUser);
                        mLobbyView.startResult(mGame, gameResult);
                    } else {
                        mLobbyView.showApiResponseError(response, () -> result(isSurrender));
                    }
                }, throwable -> {
                    mLobbyView.showNetworkError(() -> result(isSurrender));
                });
        mLobbyView.addSubscription(subscription);
    }

    public void onAddFriendClick() {
        boolean isCreator = mGame.isCreator(mUser);
        User user;
        if(isCreator) {
            user = mGame.getFollower();
        } else {
            user = mGame.getCreator();
        }
        Subscription subscription = mRepository.addFriend(user.getId(), mUser.getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mLobbyView::showProgress)
                .doOnNext(data -> mLobbyView.hideProgress())
                .doOnError(error -> mLobbyView.hideProgress())
                .subscribe(data -> {
                    if(data.isSuccess()){
                        user.setFriend(true);
                        mLobbyView.showGame(mGame, mGame.getActiveStatus(mUser));
                        mLobbyView.showUserAddedDialog(user);
                    } else {
                        mLobbyView.showApiResponseError(data, () -> onAddFriendClick());
                    }

                }, throwable -> {
                    mLobbyView.showNetworkError(() -> onAddFriendClick());
                });
        mLobbyView.addSubscription(subscription);
    }

    private boolean isGameFinished() {
        return mGame.isAllAnswered(mUser);
    }
}
