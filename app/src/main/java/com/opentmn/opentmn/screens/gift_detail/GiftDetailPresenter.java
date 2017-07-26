package com.opentmn.opentmn.screens.gift_detail;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.Gift;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.utils.RxSchedulers;

import rx.Subscription;

/**
 * Created by kost on 25.01.17.
 */

public class GiftDetailPresenter {

    private GiftDetailView mView;
    private MyTyumenRepository mRepository;
    private Gift mGift;
    private User mUser;

    public GiftDetailPresenter(GiftDetailView giftDetailView, Gift gift) {
        mView = giftDetailView;
        mRepository = RepositoryProvider.provideRepository();
        mGift = gift;
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
    }

    public void init() {
        mView.showGift(mGift);
        mView.showCoins(mUser.getCoins());
    }

    public void onBuyClick() {
        if(mUser.getCoins() < mGift.getPrice()) {
            mView.showNoMoneyDialog();
        } else {
            mView.showBuyDialog();
        }
    }

    public void onEmailConfirmClick(String email) {
        Subscription subscription = mRepository.orders(mUser.getToken(), mGift.getId(), email)
                .compose(RxSchedulers.async())
                .doOnSubscribe(mView::showProgress)
                .doAfterTerminate(mView::hideProgress)
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        User user = response.getData();
                        mUser.setCoins(user.getCoins());
                        RepositoryProvider.provideKeyValueStorage().setUser(mUser);
                        mView.showCoins(mUser.getCoins());
                        mView.showSuccessDialog();
                    } else {
                        mView.showApiResponseError(response, () -> onEmailConfirmClick(email));
                    }
                }, throwable -> {
                    mView.showNetworkError(() -> onEmailConfirmClick(email));
                });
        mView.addSubscription(subscription);
    }
}
