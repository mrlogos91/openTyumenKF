package com.opentmn.opentmn.screens.registration;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.utils.RxSchedulers;

import rx.Subscription;

/**
 * Created by Alexey Antonchik on 27.12.16.
 */

public class RegistrationPresenter  {

    private RegistrationView mRegistrationView;
  private User mUser;

    public RegistrationPresenter(RegistrationView registrationView) {
        mRegistrationView = registrationView;
    }

    public void init(){
        mRegistrationView.init();
    }
    void setName(String email, String name){
      mUser = RepositoryProvider.provideKeyValueStorage().getUser();
      Subscription subscriptionName = RepositoryProvider.provideRepository().updateProfile(mUser.getToken(), null, null, null, name, email, null)
          .compose(RxSchedulers.async())
          .doOnSubscribe(mRegistrationView::showProgress)
          .doAfterTerminate(mRegistrationView::hideProgress)
          .subscribe(response -> {
            if(response.isSuccess()) {
              mUser.setName(name);
              mUser.setEmail(email);
              mRegistrationView.startMain();
            } else {
              mRegistrationView.showApiResponseError(response, null);
            }
          }, throwable -> {
            mRegistrationView.hideProgress();
            mRegistrationView.showNetworkError(null);
          });
      mRegistrationView.addSubscription(subscriptionName);
    }
    void auth(String email, String pass, String name){

        //TODO валидация email и pass

        Subscription subscription = RepositoryProvider.provideRepository().registration(email.trim(), pass.trim())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mRegistrationView::showProgress)
                .doAfterTerminate(mRegistrationView::hideProgress)
                .subscribe(data -> {
                    if(data.isSuccess()){
                        RepositoryProvider.provideKeyValueStorage().setUser(data.getData());
                        setName(email, name);
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
