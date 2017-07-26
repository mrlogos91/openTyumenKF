package com.opentmn.opentmn.screens.choose_opponent;

import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.network.model.ApiResponseModel;
import com.opentmn.opentmn.utils.RxSchedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by kost on 12.01.17.
 */

public class OpponentChoosePresenter {

    private final static int PER_PAGE = 20;

    private OpponentChooseView mOpponentChooseView;
    private MyTyumenRepository mRepository;
    private List<User> mUsers;
    private int mCurrentPage = 0;
    private boolean mListEnded;
    private int mOnlyFriends;
    private String mToken;
    private String mSearchString;

    public OpponentChoosePresenter(OpponentChooseView opponentChooseView, int onlyFriends) {
        mOpponentChooseView = opponentChooseView;
        mRepository = RepositoryProvider.provideRepository();
        mUsers = new ArrayList<>();
        mListEnded = false;
        mOnlyFriends = onlyFriends;
        mToken = RepositoryProvider.provideKeyValueStorage().getUser().getToken();
    }

    public void init() {
        loadMore();
        Subscription subscription = mOpponentChooseView.getTextChangedObservable()
                .skip(1)
                .debounce(1, TimeUnit.SECONDS)
                .flatMap(new Func1<TextViewTextChangeEvent, Observable<ApiResponseModel<List<User>>>>() {
                    @Override
                    public Observable<ApiResponseModel<List<User>>> call(TextViewTextChangeEvent textViewTextChangeEvent) {
                        mSearchString = textViewTextChangeEvent.text().toString();
                        if(mSearchString.length() == 0)
                            mSearchString = null;
                        return mRepository.users(mToken, 1, PER_PAGE, mSearchString, mOnlyFriends, 0).compose(RxSchedulers.async());
                    }
                })
                .subscribe(data -> {
                    if(data.isSuccess()){
                        List<User> users = data.getData();
                        mCurrentPage = 1;
                        mUsers = users;
                        mListEnded = users.size() == 0;
                        mOpponentChooseView.showUsers(mUsers);
                    } else {
                        mOpponentChooseView.showApiResponseError(data, null);
                    }

                }, throwable -> {
                    mOpponentChooseView.showNetworkError(null);
                });
        mOpponentChooseView.addSubscription(subscription);
    }

    public void loadMore() {
        if(mListEnded)
            return;
        Subscription subscription = mRepository.users(mToken, mCurrentPage + 1, PER_PAGE, mSearchString, mOnlyFriends, 0)
                .compose(RxSchedulers.async())
                .subscribe(data -> {
                    if(data.isSuccess()){
                        List<User> users = data.getData();
                        if(users.size() > 0) {
                            mCurrentPage += 1;
                            mUsers.addAll(users);
                            mOpponentChooseView.showUsers(mUsers);
                        } else {
                            mListEnded = true;
                        }
                    } else {
                        mOpponentChooseView.showApiResponseError(data, () -> loadMore());
                    }

                }, throwable -> {
                    mOpponentChooseView.showNetworkError(() -> loadMore());
                });
        mOpponentChooseView.addSubscription(subscription);
    }

    public void onPlayClick(User user) {
        Subscription subscription = mRepository.createGameAgainstUser(user.getId(), mToken)
                .compose(RxSchedulers.async())
                .doOnSubscribe(mOpponentChooseView::showProgress)
                .doOnNext(data -> mOpponentChooseView.hideProgress())
                .doOnError(err -> mOpponentChooseView.hideProgress())
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        Game game = response.getData();
                        mOpponentChooseView.showGameCreatedDialog(game);
                    } else {
                        mOpponentChooseView.showApiResponseError(response, () -> onPlayClick(user));
                    }
                }, throwable -> {
                    mOpponentChooseView.showNetworkError(() -> onPlayClick(user));
                });
        mOpponentChooseView.addSubscription(subscription);
    }

    public void onUserClick(User user) {
        mOpponentChooseView.startProfile(user);
    }

    public void onInviteVkClick() {
        mOpponentChooseView.startVkInvite();
    }

}