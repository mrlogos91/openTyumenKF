package com.opentmn.opentmn.screens.registration;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.utils.RxSchedulers;

import rx.Subscription;

/**
 * Created by Alexey Antonchik on 27.12.16.
 */

public class RegistrationPresenter  {

    private RegistrationView mRegistrationView;


    public RegistrationPresenter(RegistrationView registrationView) {
        mRegistrationView = registrationView;
    }

    public void init(){
        mRegistrationView.init();
    }

    void auth(String email, String pass){

        //TODO валидация email и pass

        Subscription subscription = RepositoryProvider.provideRepository().registration(email.trim(), pass.trim())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mRegistrationView::showProgress)
                .doAfterTerminate(mRegistrationView::hideProgress)
                .subscribe(data -> {
                    if(data.isSuccess()){
                        RepositoryProvider.provideKeyValueStorage().setUser(data.getData());
                        mRegistrationView.startMain();
                    }else {
                        mRegistrationView.showApiResponseError(data, null);
                    }

                }, throwable -> {
                    mRegistrationView.hideProgress();
                    mRegistrationView.showNetworkError(null);
                });
        mRegistrationView.addSubscription(subscription);
    }
}
