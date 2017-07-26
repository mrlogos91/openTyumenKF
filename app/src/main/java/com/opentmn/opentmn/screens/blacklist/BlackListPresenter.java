package com.opentmn.opentmn.screens.blacklist;

import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.network.model.ApiResponseModel;
import com.opentmn.opentmn.utils.RxSchedulers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by kost on 18.01.17.
 */

public class BlackListPresenter {

    private final static int PER_PAGE = 100;

    private BlackListView mBlackListView;
    private MyTyumenRepository mRepository;
    private User mUser;
    private List<User> mUsers;

    public BlackListPresenter(BlackListView blackListView) {
        mBlackListView = blackListView;
        mRepository = RepositoryProvider.provideRepository();
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
    }

    public void init() {
        loadMore();
        Subscription subscription = mBlackListView.getTextChangedObservable()
                .skip(1)
                .debounce(1, TimeUnit.SECONDS)
                .flatMap(new Func1<TextViewTextChangeEvent, Observable<ApiResponseModel<List<User>>>>() {
                    @Override
                    public Observable<ApiResponseModel<List<User>>> call(TextViewTextChangeEvent textViewTextChangeEvent) {
                        String searchString = textViewTextChangeEvent.text().toString();
                        if(searchString.length() == 0)
                            searchString = null;
                        return mRepository.users(mUser.getToken(), 1, PER_PAGE, searchString, 0, 1).compose(RxSchedulers.async());
                    }
                })
                .subscribe(data -> {
                    if(data.isSuccess()){
                        mUsers = data.getData();
                        mBlackListView.showUsers(mUsers);
                    } else {
                        mBlackListView.showApiResponseError(data, null);
                    }

                }, throwable -> {
                    mBlackListView.showNetworkError(null);
                });
        mBlackListView.addSubscription(subscription);
    }

    public void loadMore() {
        Subscription subscription = mRepository.users(mUser.getToken(), 1, 100, null, 0, 1)
                .compose(RxSchedulers.async())
                .doOnSubscribe(mBlackListView::showProgress)
                .doAfterTerminate(mBlackListView::hideProgress)
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        mUsers = response.getData();
                        mBlackListView.showUsers(mUsers);
                    } else {
                        mBlackListView.showApiResponseError(response, () -> init());
                    }
                }, throwable -> {
                    mBlackListView.showNetworkError(() -> init());
                });
        mBlackListView.addSubscription(subscription);
    }

    public void onBlockClick(User user) {
        Subscription subscription = mRepository.delBlock(user.getId(), mUser.getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mBlackListView::showProgress)
                .doAfterTerminate(mBlackListView::hideProgress)
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        mUsers.remove(user);
                        mBlackListView.showUsers(mUsers);
                    } else {
                        mBlackListView.showApiResponseError(response, () -> onBlockClick(user));
                    }
                }, throwable ->  {
                    mBlackListView.showNetworkError(() -> onBlockClick(user));
                });
        mBlackListView.addSubscription(subscription);
    }
}
