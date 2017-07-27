package com.opentmn.opentmn.screens.friends;

import android.app.Activity;

import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.network.model.ApiResponseModel;
import com.opentmn.opentmn.utils.RxSchedulers;
import com.vk.sdk.VKSdk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

import static com.opentmn.opentmn.Config.VK_SCOPE;

/**
 * Created by kost on 07.01.17.
 */

public class FriendsPresenter {

    private final static int PER_PAGE = 20;

    private FriendsView mFriendsView;
    private MyTyumenRepository mRepository;
    private List<User> mUsers;
    private int mCurrentPage = 0;
    private boolean mListEnded;
    private String mToken;
    private String mSearchString;

    public FriendsPresenter(FriendsView friendsView) {
        mFriendsView = friendsView;
        mRepository = RepositoryProvider.provideRepository();
        mUsers = new ArrayList<>();
        mListEnded = false;
        mToken = RepositoryProvider.provideKeyValueStorage().getUser().getToken();
    }

    public void init() {
        loadMore();
        Subscription subscription = mFriendsView.getTextChangedObservable()
                .skip(1)
                .debounce(1, TimeUnit.SECONDS)
                .flatMap(new Func1<TextViewTextChangeEvent, Observable<ApiResponseModel<List<User>>>>() {
                    @Override
                    public Observable<ApiResponseModel<List<User>>> call(TextViewTextChangeEvent textViewTextChangeEvent) {
                        mSearchString = textViewTextChangeEvent.text().toString();
                        return mRepository.users(mToken, 1, PER_PAGE, null, 1, 0).compose(RxSchedulers.async());
                    }
                })
                .subscribe(data -> {
                    if(data.isSuccess()){
                        List<User> users = data.getData();
                        mCurrentPage = 1;
                        if(mSearchString.length() == 0) {
                          mUsers = users;
                        } else {
                          for (User user : users) {
                            if (user.getName().toLowerCase().contains(mSearchString.toLowerCase())) {
                              mUsers.add(user);
                            }
                          }
                        }
                        mListEnded = users.size() == 0;
                        mFriendsView.showFriends(mUsers);
                    } else {
                        mFriendsView.showApiResponseError(data, null);
                    }

                }, throwable -> {
                    mFriendsView.showNetworkError(null);
                });
        mFriendsView.addSubscription(subscription);
    }

    public void loadMore() {
        if(mListEnded)
            return;
        Subscription subscription = mRepository.users(mToken, mCurrentPage + 1, PER_PAGE, mSearchString, 1, 0)
                .compose(RxSchedulers.async())
                .subscribe(data -> {
                    if(data.isSuccess()){
                        List<User> users = data.getData();
                        if(users.size() > 0) {
                            mCurrentPage += 1;
                            mUsers.addAll(users);
                            mFriendsView.showFriends(mUsers);
                        } else {
                            mListEnded = true;
                        }
                    } else {
                        mFriendsView.showApiResponseError(data, null);
                    }

                }, throwable -> {
                    mFriendsView.showNetworkError(null);
                });
        mFriendsView.addSubscription(subscription);
    }

    public void onUserClick(User user) {
        mFriendsView.startProfile(user);
    }

    public void onDeleteFriendClick(User user) {
        Subscription subscription = mRepository.deleteFriend(user.getId(), mToken)
                .compose(RxSchedulers.async())
                .doOnSubscribe(mFriendsView::showProgress)
                .doOnNext(data -> mFriendsView.hideProgress())
                .doOnError(error -> mFriendsView.hideProgress())
                .subscribe(data -> {
                    if(data.isSuccess()){
                        mUsers.remove(user);
                        mFriendsView.showUserDeletedDialog(user);
                        mFriendsView.showFriends(mUsers);
                    } else {
                        mFriendsView.showApiResponseError(data, () -> onDeleteFriendClick(user));
                    }

                }, throwable -> {
                    mFriendsView.showNetworkError(() -> onDeleteFriendClick(user));
                });
        mFriendsView.addSubscription(subscription);
    }

    public void onPlayClick(User user) {
        Subscription subscription = mRepository.createGameAgainstUser(user.getId(), mToken)
                .compose(RxSchedulers.async())
                .doOnSubscribe(mFriendsView::showProgress)
                .doOnNext(data -> mFriendsView.hideProgress())
                .doOnError(err -> mFriendsView.hideProgress())
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        Game game = response.getData();
                        mFriendsView.showGameCreatedDialog(game);
                        mFriendsView.showFriends(mUsers);
                    } else {
                        mFriendsView.showApiResponseError(response, () -> onPlayClick(user));
                    }
                }, throwable -> {
                    mFriendsView.showNetworkError(() -> onPlayClick(user));
                });
        mFriendsView.addSubscription(subscription);
    }

    public void onInviteButtonClick(Activity activity) {
        if(RepositoryProvider.provideKeyValueStorage().getSocialUser() != null)
            mFriendsView.startVkInvite();
        else
            VKSdk.login(activity, VK_SCOPE);
    }
}
