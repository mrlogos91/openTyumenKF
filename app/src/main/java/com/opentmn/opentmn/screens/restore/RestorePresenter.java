package com.opentmn.opentmn.screens.restore;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.utils.RxSchedulers;

import rx.Subscription;

/**
 * Created by kost on 24.01.17.
 */

public class RestorePresenter {

    private RestoreView mView;
    private MyTyumenRepository mRepository;

    public RestorePresenter(RestoreView restoreView) {
        mView = restoreView;
        mRepository = RepositoryProvider.provideRepository();
    }

    public void onRestoreClick(String email) {
        Subscription subscription = mRepository.restore(email)
                .compose(RxSchedulers.async())
                .doOnSubscribe(mView::showProgress)
                .doAfterTerminate(mView::hideProgress)
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        mView.showSuccessDialog();
                    } else {
                        mView.showApiResponseError(response, () -> onRestoreClick(email));
                    }
                }, throwable -> {
                    mView.showNetworkError(() -> onRestoreClick(email));
                });
        mView.addSubscription(subscription);
    }
}
