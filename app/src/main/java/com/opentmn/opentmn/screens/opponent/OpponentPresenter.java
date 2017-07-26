package com.opentmn.opentmn.screens.opponent;

import android.app.Activity;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.GameType;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.utils.RxSchedulers;
import com.vk.sdk.VKSdk;

import rx.Subscription;

import static com.opentmn.opentmn.Config.VK_SCOPE;

/**
 * Created by kost on 14.01.17.
 */

public class OpponentPresenter {

    private OpponentView mOpponentView;
    private MyTyumenRepository mRepository;
    private User mUser;

    public OpponentPresenter(OpponentView opponentView) {
        mOpponentView = opponentView;
        mRepository = RepositoryProvider.provideRepository();
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
    }

    public void onTrainingClick() {
        String token = RepositoryProvider.provideKeyValueStorage().getUser().getToken();
        Subscription subscription = mRepository.createGame(GameType.TRAINING, token)
                .compose(RxSchedulers.async())
                .doOnSubscribe(mOpponentView::showProgress)
                .doOnNext(data -> mOpponentView.hideProgress())
                .doOnError(err -> mOpponentView.hideProgress())
                .subscribe(data -> {
                    if(data.isSuccess()) {
                        Game game = data.getData();
                        mOpponentView.startLobby(game);
                    } else {
                        mOpponentView.showApiResponseError(data, () -> onTrainingClick());
                    }
                }, throwable -> {
                    mOpponentView.showNetworkError(() -> onTrainingClick());
                });
        mOpponentView.addSubscription(subscription);
    }

    public void onRandomClick() {
        String token = RepositoryProvider.provideKeyValueStorage().getUser().getToken();
        Subscription subscription = mRepository.createGame(GameType.RANDOM, token)
                .compose(RxSchedulers.async())
                .doOnSubscribe(mOpponentView::showProgress)
                .doOnNext(data -> mOpponentView.hideProgress())
                .doOnError(err -> mOpponentView.hideProgress())
                .subscribe(data -> {
                    if(data.isSuccess()) {
                        Game game = data.getData();
                        mOpponentView.showGameCreatedDialog(game);
                    } else {
                        mOpponentView.showApiResponseError(data, () -> onRandomClick());
                    }
                }, throwable -> {
                    mOpponentView.showNetworkError(() -> onRandomClick());
                });
        mOpponentView.addSubscription(subscription);
    }

    public void onInviteVkClick(Activity activity) {
        if(RepositoryProvider.provideKeyValueStorage().getSocialUser() != null)
            mOpponentView.startVkInvite();
        else
            VKSdk.login(activity, VK_SCOPE);

    }
}
