package com.opentmn.opentmn.screens.my_games;

import android.util.Log;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.keyvalue.KeyValueStorage;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.GameResult;
import com.opentmn.opentmn.model.GameStatus;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.model.VisibleResults;
import com.opentmn.opentmn.network.model.ApiResponseModel;
import com.opentmn.opentmn.utils.RxSchedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by kost on 14.01.17.
 */

public class MyGamesPresenter {

    private static final String TAG = "MyGamesPresenter";

    private MyGamesView mMyGamesView;
    private MyTyumenRepository mRepository;
    private User mUser;

    public MyGamesPresenter(MyGamesView myGamesView) {
        mMyGamesView = myGamesView;
        mRepository = RepositoryProvider.provideRepository();
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
    }

    public void init() {
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
        String token = mUser.getToken();
        mMyGamesView.showUser(mUser);
        Subscription subscription = Observable.interval(0, 10, TimeUnit.SECONDS)
                .flatMap(new Func1<Long, Observable<ApiResponseModel<List<Game>>>>() {
                    @Override
                    public Observable<ApiResponseModel<List<Game>>> call(Long aLong) {
                        return mRepository.games(0, 1, 100, token).compose(RxSchedulers.async());
                    }
                })
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        List<Game> games = response.getData();
                        mMyGamesView.showGames(getFiltered(games));
                    } else {

                    }
                }, throwable -> {

                });
        mMyGamesView.addSubscription(subscription);
        updateProfile();
        loadVisibleResults();

    }

    private void loadGames() {
        mRepository.games(0, 1, 100, mUser.getToken())
                .compose(RxSchedulers.async())
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        List<Game> games = response.getData();
                        mMyGamesView.showGames(getFiltered(games));
                    } else {

                    }
                }, throwable -> {

                });
    }

    private List<Game> getFiltered(List<Game> games) {
        Log.d(TAG, "count before filter: " + String.valueOf(games.size()));
        List<Game> currentGames = new ArrayList<Game>();
        List<Game> inviteGames = new ArrayList<Game>();
        List<Game> waitGames = new ArrayList<Game>();
        List<Game> noFollowerGames = new ArrayList<Game>();
        for(Game game: games) {
            int status = game.getStatus(mUser);
            if(status == GameStatus.CURRENT) {
                currentGames.add(game);
            } else if(status == GameStatus.WAIT) {
                waitGames.add(game);
            } else if(status == GameStatus.INVITE) {
                inviteGames.add(game);
            } else if(status == GameStatus.NO_FOLLOWER) {
                noFollowerGames.add(game);
            }
        }
        List<Game> resultGames = new ArrayList<>();
        if(currentGames.size() > 0) {
            resultGames.add(new Game("Текущие игры"));
            resultGames.addAll(currentGames);
        }
        if(inviteGames.size() > 0) {
            resultGames.add(new Game("Приглашения сыграть"));
            resultGames.addAll(inviteGames);
        }
        if(waitGames.size() > 0 || noFollowerGames.size() > 0) {
            resultGames.add(new Game("Ожидаю соперника"));
            resultGames.addAll(waitGames);
            resultGames.addAll(noFollowerGames);
        }
        return resultGames;
    }

    public void onNewGameClick() {
        mMyGamesView.startNewGame();
    }

    public void onFinishedGamesClick() {
        mMyGamesView.startHistory();
    }

    public void onGameClick(Game game) {
        int status = game.getStatus(mUser);
        if(game.getStatus(mUser) == GameStatus.CURRENT)
            mMyGamesView.startLobby(game);
        else if(status == GameStatus.WAIT || status == GameStatus.NO_FOLLOWER) {
            Subscription subscription = mRepository.game(game.getId(), mUser.getToken())
                    .compose(RxSchedulers.async())
                    .doOnSubscribe(mMyGamesView::showProgress)
                    .doOnNext(data -> mMyGamesView.hideProgress())
                    .doOnError(error -> mMyGamesView.hideProgress())
                    .subscribe(response -> {
                        if(response.isSuccess()) {
                            Game game1 = response.getData();
                            if(game1.getFollower().getIsApproved() == 1) {
                                mMyGamesView.startLobby(game1);
                            } else if(status == GameStatus.WAIT) {
                                mMyGamesView.showWaitDialog(game1);
                            } else if(status == GameStatus.NO_FOLLOWER) {
                                mMyGamesView.showNoFollowerDialog(game1);
                            }
                        } else {
                            mMyGamesView.showApiResponseError(response, () -> onGameClick(game));
                        }
                    }, throwable -> {
                        mMyGamesView.showNetworkError(() -> onGameClick(game));
                    });
            mMyGamesView.addSubscription(subscription);
        }
    }

    public void onAcceptClick(Game game) {
        mMyGamesView.showAcceptGameDialog(game);
    }

    public void onDenyClick(Game game) {
        Subscription subscription = mRepository.updateGame(game.getId(), 0, mUser.getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mMyGamesView::showProgress)
                .doAfterTerminate(mMyGamesView::hideProgress)
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        loadGames();
                    } else {
                        mMyGamesView.showApiResponseError(response, () -> onDenyClick(game));
                    }
                }, throwable -> {
                    mMyGamesView.showNetworkError(() -> onDenyClick(game));
                });
        mMyGamesView.addSubscription(subscription);
    }


    public void onGameConfirmClick(Game game) {
        Subscription subscription = mRepository.updateGame(game.getId(), 1, mUser.getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mMyGamesView::showProgress)
                .doOnNext(data -> mMyGamesView.hideProgress())
                .doOnError(error -> mMyGamesView.hideProgress())
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        Game game1 = response.getData();
                        mMyGamesView.startLobby(game1);
                    } else {
                        mMyGamesView.showApiResponseError(response, () -> onGameConfirmClick(game));
                    }
                }, throwable -> {
                    mMyGamesView.showNetworkError(() -> onGameConfirmClick(game));
                });
        mMyGamesView.addSubscription(subscription);
    }

    private void updateProfile() {
        String token = mUser.getToken();
        Subscription subscription = mRepository.profile(token)
                .compose(RxSchedulers.async())
                .subscribe(data -> {
                    if (data.isSuccess()) {
                        User user = data.getData();
                        KeyValueStorage keyValueStorage = RepositoryProvider.provideKeyValueStorage();
                        user.setToken(mUser.getToken());
                        keyValueStorage.setUser(user);
                        mUser = user;
                        mMyGamesView.showUser(mUser);
                    }

                }, throwable -> {
                });
        mMyGamesView.addSubscription(subscription);
    }

    private void loadVisibleResults() {
        Subscription subscription = mRepository.visibleResults(mUser.getToken())
                .compose(RxSchedulers.async())
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        VisibleResults results = response.getData();
                        if(results.getGames().size() > 0) {
                            loadResult(results.getGames().get(0));
                        }
                    }
                }, throwable -> {

                });
        mMyGamesView.addSubscription(subscription);
    }

    private void loadResult(int id) {
        Subscription subscription = mRepository.result(id, 0, mUser.getToken())
                .compose(RxSchedulers.async())
                .subscribe(response -> {
                   if(response.isSuccess()) {
                       GameResult gameResult = response.getData();
                       mMyGamesView.startGameResult(gameResult);
                   }
                }, throwable -> {

                });
        mMyGamesView.addSubscription(subscription);
    }
}
