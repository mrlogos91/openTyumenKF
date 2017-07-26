package com.opentmn.opentmn.screens.profile;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.keyvalue.KeyValueStorage;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.network.model.ApiResponseModel;
import com.opentmn.opentmn.utils.RxSchedulers;

import java.util.List;

import rx.Observable;
import rx.Subscription;

/**
 * Created by kost on 04.01.17.
 */

public class ProfilePresenter {

    private ProfileView mProfileView;
    private MyTyumenRepository mRepository;
    private User mUser;
    private boolean mIsMyAccount;

    public ProfilePresenter(ProfileView profileView, User user) {
        mProfileView = profileView;
        mRepository = RepositoryProvider.provideRepository();
        if(user == null) {
            mIsMyAccount = true;
            mUser = RepositoryProvider.provideKeyValueStorage().getUser();
        } else {
            mIsMyAccount = false;
            mUser = user;
        }
    }

    public void init() {
        mProfileView.showUser(mUser);
        loadUser();
    }

    public void loadUser() {
        if(mIsMyAccount) {
            String token = mUser.getToken();
            Subscription subscription = mRepository.profile(token)
                    .compose(RxSchedulers.async())
                    .doOnSubscribe(mProfileView::showProgress)
                    .doOnNext(data -> mProfileView.hideProgress())
                    .doOnError(error -> mProfileView.hideProgress())
                    .subscribe(data -> {
                        if (data.isSuccess()) {
                            mUser = data.getData();
                            KeyValueStorage keyValueStorage = RepositoryProvider.provideKeyValueStorage();
                            mUser.setToken(keyValueStorage.getUser().getToken());
                            keyValueStorage.setUser(mUser);
                            mProfileView.showUser(mUser);
                        } else {
                            mProfileView.showApiResponseError(data, () -> loadUser());
                        }

                    }, throwable -> {
                        mProfileView.showNetworkError(() -> loadUser());
                    });
            mProfileView.addSubscription(subscription);
        } else {
            String token = RepositoryProvider.provideKeyValueStorage().getUser().getToken();
            Subscription subscription = mRepository.userWithId(mUser.getId(), token)
                    .compose(RxSchedulers.async())
                    .doOnSubscribe(mProfileView::showProgress)
                    .doOnNext(data -> mProfileView.hideProgress())
                    .doOnError(error -> mProfileView.hideProgress())
                    .subscribe(data -> {
                        if (data.isSuccess()) {
                            List<User> users = data.getData();
                            if(users.size() > 0) {
                                mUser = users.get(0);
                                mProfileView.showUser(mUser);
                            }
                        } else {
                            mProfileView.showApiResponseError(data, () -> loadUser());
                        }

                    }, throwable -> {
                        mProfileView.showNetworkError(() -> loadUser());
                    });
            mProfileView.addSubscription(subscription);
        }
    }

    public void onPlayClick(User user) {
        String token = RepositoryProvider.provideKeyValueStorage().getUser().getToken();
        Subscription subscription = mRepository.createGameAgainstUser(user.getId(), token)
                .compose(RxSchedulers.async())
                .doOnSubscribe(mProfileView::showProgress)
                .doOnNext(data -> mProfileView.hideProgress())
                .doOnError(err -> mProfileView.hideProgress())
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        Game game = response.getData();
                        mProfileView.showGameCreatedDialog(game);
                    } else {
                        mProfileView.showApiResponseError(response, () -> onPlayClick(user));
                    }
                }, throwable -> {
                    mProfileView.showNetworkError(() -> onPlayClick(user));
                });
        mProfileView.addSubscription(subscription);
    }

    public void onBlackListClick() {
        mProfileView.startBlackList();
    }

    public void onBlockClick() {
        String token = RepositoryProvider.provideKeyValueStorage().getUser().getToken();
        Observable<ApiResponseModel<Void>> observable;
        if(mUser.isBlock()) {
            observable = mRepository.delBlock(mUser.getId(), token);
        } else {
            observable = mRepository.block(mUser.getId(), token);
        }
        Subscription subscription = observable
                .compose(RxSchedulers.async())
                .doOnSubscribe(mProfileView::showProgress)
                .doAfterTerminate(mProfileView::hideProgress)
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        mUser.setBlock(!mUser.isBlock());
                        mProfileView.showUser(mUser);
                    } else {
                        mProfileView.showApiResponseError(response, () -> onBlockClick());
                    }
                }, throwable ->  {
                    mProfileView.showNetworkError(() -> onBlockClick());
                });
        mProfileView.addSubscription(subscription);
    }

    public void onAddFriendClick() {
        String token = RepositoryProvider.provideKeyValueStorage().getUser().getToken();
        Subscription subscription = mRepository.addFriend(mUser.getId(), token)
                .compose(RxSchedulers.async())
                .doOnSubscribe(mProfileView::showProgress)
                .doAfterTerminate(mProfileView::hideProgress)
                .subscribe(data -> {
                    if(data.isSuccess()){
                        mUser.setFriend(true);
                        mProfileView.showUser(mUser);
                    } else {
                        mProfileView.showApiResponseError(data, () -> onAddFriendClick());
                    }

                }, throwable -> {
                    mProfileView.showNetworkError(() -> onAddFriendClick());
                });
        mProfileView.addSubscription(subscription);
    }

}
