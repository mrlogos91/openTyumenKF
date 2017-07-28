package com.opentmn.opentmn.screens.rating;

import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.network.model.ApiResponseModel;
import com.opentmn.opentmn.utils.PuntoSwitcher;
import com.opentmn.opentmn.utils.RxSchedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by kost on 24.12.16.
 */

public class RatingPresenter {

    private final static int PER_PAGE = 20;

    private RatingView mRatingView;
    private MyTyumenRepository mRepository;
    private List<User> mUsers;
    private int mCurrentPage = 0;
    private boolean mListEnded;
    private User mUser;
    private String mSearchString;

    public RatingPresenter(RatingView ratingView) {
        mRatingView = ratingView;
        mRepository = RepositoryProvider.provideRepository();
        mUsers = new ArrayList<>();
        mListEnded = false;
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
    }

    public void init() {
        loadMore();
        Subscription subscription = mRatingView.getTextChangedObservable()
                .skip(1)
                .debounce(1, TimeUnit.SECONDS)
                .flatMap(new Func1<TextViewTextChangeEvent, Observable<ApiResponseModel<List<User>>>>() {
                    @Override
                    public Observable<ApiResponseModel<List<User>>> call(TextViewTextChangeEvent textViewTextChangeEvent) {
                        mSearchString = textViewTextChangeEvent.text().toString();
                        return mRepository.users(mUser.getToken(), 1, PER_PAGE,null, 0, 0).compose(RxSchedulers.async());
                    }
                })
                .subscribe(data -> {
                    if(data.isSuccess()){
                        List<User> users = data.getData();
                        mCurrentPage = 1;
                        mUsers = new ArrayList<>();
                        if(mSearchString.length() == 0) {
                          mUsers = users;
                        } else {
                          for (User user : users) {
                            if (user.getName().toLowerCase().contains(mSearchString.toLowerCase()) || user.getName().toLowerCase().contains(PuntoSwitcher.switchToRu(mSearchString).toLowerCase())) {
                              mUsers.add(user);
                            }
                          }
                        }
                        mListEnded = users.size() == 0;
                        mRatingView.showUsers(mUsers);
                    } else {
                        mRatingView.showApiResponseError(data, null);
                    }

                }, throwable -> {
                    mRatingView.showNetworkError(null);
                });
        mRatingView.addSubscription(subscription);
    }

    public void loadMore() {
        if(mListEnded)
            return;
        Subscription subscription = mRepository.users(mUser.getToken(), mCurrentPage + 1, PER_PAGE, mSearchString, 0, 0)
                .compose(RxSchedulers.async())
                .subscribe(data -> {
                    if(data.isSuccess()){
                        List<User> users = data.getData();
                        if(users.size() > 0) {
                            mCurrentPage += 1;
                            mUsers.addAll(users);
                            mRatingView.showUsers(mUsers);
                        } else {
                            mListEnded = true;
                        }
                    } else {
                        mRatingView.showApiResponseError(data, () -> loadMore());
                    }

                }, throwable -> {
                    mRatingView.showNetworkError(() -> loadMore());
                });
        mRatingView.addSubscription(subscription);
    }

    public void onAddFriendClick(User user) {
        Subscription subscription = mRepository.addFriend(user.getId(), mUser.getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mRatingView::showProgress)
                .doOnNext(data -> mRatingView.hideProgress())
                .doOnError(error -> mRatingView.hideProgress())
                .subscribe(data -> {
                    if(data.isSuccess()){
                        user.setFriend(true);
                        mRatingView.showUsers(mUsers);
                        mRatingView.showUserAddedDialog(user);
                    } else {
                        mRatingView.showApiResponseError(data, () -> onAddFriendClick(user));
                    }

                }, throwable -> {
                    mRatingView.showNetworkError(() -> onAddFriendClick(user));
                });
        mRatingView.addSubscription(subscription);
    }

    public void onDeleteFriendClick(User user) {
        Subscription subscription = mRepository.deleteFriend(user.getId(), mUser.getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mRatingView::showProgress)
                .doOnNext(data -> mRatingView.hideProgress())
                .doOnError(error -> mRatingView.hideProgress())
                .subscribe(data -> {
                    if(data.isSuccess()){
                        user.setFriend(false);
                        mRatingView.showUserDeletedDialog(user);
                        mRatingView.showUsers(mUsers);
                    } else {
                        mRatingView.showApiResponseError(data, () -> onDeleteFriendClick(user));
                    }

                }, throwable -> {
                    mRatingView.showNetworkError(() -> onDeleteFriendClick(user));
                });
        mRatingView.addSubscription(subscription);
    }

    public void onUserClick(User user) {
        mRatingView.startProfile(user);
    }

    public void onPlayClick(User user) {
        Subscription subscription = mRepository.createGameAgainstUser(user.getId(), mUser.getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mRatingView::showProgress)
                .doOnNext(data -> mRatingView.hideProgress())
                .doOnError(err -> mRatingView.hideProgress())
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        Game game = response.getData();
                        mRatingView.showGameCreatedDialog(game);
                        mRatingView.showUsers(mUsers);
                    } else {
                        mRatingView.showApiResponseError(response, () -> onPlayClick(user));
                    }
                }, throwable -> {
                    mRatingView.showNetworkError(() -> onPlayClick(user));
                });
        mRatingView.addSubscription(subscription);
    }

}
