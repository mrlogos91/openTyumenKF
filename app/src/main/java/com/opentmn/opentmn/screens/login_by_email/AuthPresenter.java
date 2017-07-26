package com.opentmn.opentmn.screens.login_by_email;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.utils.RxSchedulers;

import rx.Subscription;

/**
 * Created by Alexey Antonchik on 16.12.16.
 */

public class AuthPresenter {


    private AuthView mAuthView;


    public AuthPresenter(AuthView authView) {
        mAuthView = authView;
        init();
    }

    public void init(){
        mAuthView.init();
    }

    public void clickForgot(){
        mAuthView.startRecovery();
    }

    void auth(String email, String pass){
        Subscription subscription = RepositoryProvider.provideRepository().authenticate(email.trim(), pass.trim())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mAuthView::showProgress)
                .doAfterTerminate(mAuthView::hideProgress)
                .subscribe(data -> {
                    if(data.isSuccess()){
                        RepositoryProvider.provideKeyValueStorage().setUser(data.getData());
                        RepositoryProvider.provideKeyValueStorage().setSocialUser(null);
                        mAuthView.startMain();
                    } else {
                        mAuthView.showApiResponseError(data, () -> auth(email, pass));
                    }

                }, throwable -> {
                    mAuthView.showNetworkError(() -> auth(email, pass));
                });
        mAuthView.addSubscription(subscription);
    }
}
