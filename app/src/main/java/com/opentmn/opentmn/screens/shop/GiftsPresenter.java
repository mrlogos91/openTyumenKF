package com.opentmn.opentmn.screens.shop;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.keyvalue.KeyValueStorage;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.Gift;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.utils.RxSchedulers;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by kost on 04.01.17.
 */

public class GiftsPresenter {

    private final static int PER_PAGE = 20;

    private GiftsView mGiftsView;
    private MyTyumenRepository mRepository;
    private User mUser;
    private List<Gift> mGifts;
    private int mCurrentPage = 0;
    private boolean mListEnded;

    public GiftsPresenter(GiftsView giftsView) {
        mGiftsView = giftsView;
        mRepository = RepositoryProvider.provideRepository();
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
        mGifts = new ArrayList<>();
    }

    public void init() {
        loadMore();
    }

    public void loadMore() {
        if(mListEnded)
            return;
        Subscription subscription = mRepository.gifts(mUser.getToken(), mCurrentPage + 1, PER_PAGE)
                .compose(RxSchedulers.async())
                .subscribe(data -> {
                    if(data.isSuccess()){
                        List<Gift> gifts = data.getData();
                        if(gifts.size() > 0) {
                            mCurrentPage += 1;
                            mGifts.addAll(gifts);
                            mGiftsView.showGifts(mGifts);
                        } else {
                            mListEnded = true;
                        }
                    } else {
                        mGiftsView.showApiResponseError(data, () -> loadMore());
                    }

                }, throwable -> {
                    mGiftsView.showNetworkError(() -> loadMore());
                });
        mGiftsView.addSubscription(subscription);
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
                        mGiftsView.showUser(mUser);
                    }

                }, throwable -> {
                });
        mGiftsView.addSubscription(subscription);
    }

    public void onGiftClick(Gift gift) {
        mGiftsView.showGiftDetail(gift);
    }

    public void onStart() {
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
        mGiftsView.showUser(mUser);
        updateProfile();
    }


}
