package com.opentmn.opentmn.screens.result;

import android.content.Context;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.GameResult;
import com.opentmn.opentmn.model.GameType;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.utils.RxSchedulers;

import rx.Subscription;
import com.opentmn.opentmn.R;

/**
 * Created by kost on 11.01.17.
 */

public class GameResultPresenter {

    private GameResultView mGameResultView;
    private Game mGame;
    private GameResult mGameResult;
    private MyTyumenRepository mRepository;
    private User mUser;
    private boolean mStickerSent;

    public GameResultPresenter(GameResultView gameResultView, Game game, GameResult gameResult) {
        mGameResultView = gameResultView;
        mGame = game;
        mGameResult = gameResult;
        mRepository = RepositoryProvider.provideRepository();
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
    }

    public void init() {
        mGameResultView.showResult(mGameResult);
    }

    public void onPlayAgainClick() {
        if(mGame == null || mGame.getGameTypeId() != GameType.TRAINING) {
            Subscription subscription = mRepository.createGameAgainstUser(mGameResult.getEnemyId(), mUser.getToken())
                    .compose(RxSchedulers.async())
                    .doOnSubscribe(mGameResultView::showProgress)
                    .doOnNext(data -> mGameResultView.hideProgress())
                    .doOnError(err -> mGameResultView.hideProgress())
                    .subscribe(response -> {
                        if (response.isSuccess()) {
                            Game game = response.getData();
                            mGameResultView.showMyGames();
                        } else {
                            mGameResultView.showApiResponseError(response, () -> onPlayAgainClick());
                        }
                    }, throwable -> {
                        mGameResultView.showNetworkError(() -> onPlayAgainClick());
                    });
            mGameResultView.addSubscription(subscription);
        } else {
            Subscription subscription = mRepository.createGame(GameType.TRAINING, mUser.getToken())
                    .compose(RxSchedulers.async())
                    .doOnSubscribe(mGameResultView::showProgress)
                    .doOnNext(data -> mGameResultView.hideProgress())
                    .doOnError(err -> mGameResultView.hideProgress())
                    .subscribe(response -> {
                        if (response.isSuccess()) {
                            Game game = response.getData();
                            mGameResultView.startLobby(game);
                        } else {
                            mGameResultView.showApiResponseError(response, () -> onPlayAgainClick());
                        }
                    }, throwable -> {
                        mGameResultView.showNetworkError(() -> onPlayAgainClick());
                    });
            mGameResultView.addSubscription(subscription);
        }
    }

    public void onVkShareClick(Context context) {
        String text;
        if(mGameResult.getIsDraw() == 1) {
            text = String.format(context.getString(R.string.social_share_draw), mGameResult.getEnemyName());
        } else if(mGameResult.isWinner()) {
            text = String.format(context.getString(R.string.social_share_win), mGameResult.getEnemyName());
        } else {
            text = String.format(context.getString(R.string.social_share_lose), mGameResult.getEnemyName());
        }
        String link = "http://opentmn.ru/share";
        mGameResultView.showVkShareDialog(text, link);
    }

    public void onFbShareClick(Context context) {
        String text;
        if(mGameResult.getIsDraw() == 1) {
            text = String.format(context.getString(R.string.social_share_draw), mGameResult.getEnemyName());
        } else if(mGameResult.isWinner()) {
            text = String.format(context.getString(R.string.social_share_win), mGameResult.getEnemyName());
        } else {
            text = String.format(context.getString(R.string.social_share_lose), mGameResult.getEnemyName());
        }
        String link = "http://opentmn.ru/share";
        mGameResultView.showFbShareDialog(text, link);
    }

    public void onSmileClick(int smileId) {
        if(!mStickerSent) {
            Subscription subscription = mRepository.sendSmile(mUser.getToken(), mGameResult.getGameId(), smileId)
                    .compose(RxSchedulers.async())
                    .doOnSubscribe(mGameResultView::showProgress)
                    .doAfterTerminate(mGameResultView::hideProgress)
                    .subscribe(response -> {
                        if (response.isSuccess()) {
                            mStickerSent = true;
                            mGameResultView.showSmileSentDialog();
                        } else {
                            mGameResultView.showApiResponseError(response, () -> onSmileClick(smileId));
                        }
                    }, throwable -> {
                        mGameResultView.showNetworkError(() -> onSmileClick(smileId));
                    });
            mGameResultView.addSubscription(subscription);
        } else {
            mGameResultView.showStickerErrorDialog();
        }
    }
}
