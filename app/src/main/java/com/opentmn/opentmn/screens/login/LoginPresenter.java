package com.opentmn.opentmn.screens.login;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.SocialType;
import com.opentmn.opentmn.model.SocialUser;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.model.UsersCount;
import com.opentmn.opentmn.utils.RxSchedulers;
import rx.Subscription;

/**
 * Created by Alexey Antonchik on 15.12.16.
 */

public class LoginPresenter {

    private LoginView mLoginView;
    private MyTyumenRepository mRepository;

    public LoginPresenter(LoginView loginView) {
        mLoginView = loginView;
        mRepository = RepositoryProvider.provideRepository();
    }

    public void init() {
        User user = RepositoryProvider.provideKeyValueStorage().getUser();
        if(user != null && !TextUtils.isEmpty(user.getToken())) {
            mLoginView.startMain();
            return;
        }

        Subscription subscription = mRepository.usersCount()
                .compose(RxSchedulers.async())
                .subscribe(response -> {
                    if(response.isSuccess()){
                        UsersCount usersCount = response.getData();
                        mLoginView.showUsersCount(usersCount);
                    }else {
                        mLoginView.showApiResponseError(response, null);
                    }

                }, throwable -> {
                    mLoginView.showNetworkError(null);
                });
        mLoginView.addSubscription(subscription);
    }

    public void clickVK(){
        mLoginView.authVK();
    }

    public void clickFB(){
        mLoginView.authFB();
    }

    public void clickEnter(){
        mLoginView.openEmailAuth();
    }

    public void clickRegistration(){
        mLoginView.openEmailRegistration();
    }

    public void clickShowRules(){
        mLoginView.showRules();
    }

    public void auth(@NonNull String socialType, @NonNull  String id, @NonNull  String token, String captchaSid, String captchaKey) {

        Subscription subscription = mRepository.authenticate(socialType, id, token, captchaSid, captchaKey)
                .compose(RxSchedulers.async())
                .doOnSubscribe(mLoginView::showProgress)
                .doAfterTerminate(mLoginView::hideProgress)
                .subscribe(data -> {
                    if(data.isSuccess()) {
                        RepositoryProvider.provideKeyValueStorage().setUser(data.getData());
                        if (socialType == SocialType.VK) {
                            RepositoryProvider.provideKeyValueStorage().setSocialUser(new SocialUser(id, token));
                        } else {
                            RepositoryProvider.provideKeyValueStorage().setSocialUser(null);
                        }
                        mLoginView.startMain();
                        //auth(socialType, id, token, captchaSid, captchaKey);
                    } else if(data.getError() != null && (data.getError().getCode() == 14 || data.getError().getCode() == -14 || data.getError().getCode() == 12) && data.getError().getMeta() != null && data.getError().getMeta().getCaptchaSid() != null && data.getError().getMeta().getCaptchaImg() != null) {
                        mLoginView.showCaptchaDialog(socialType, id, token, data.getError().getMeta().getCaptchaImg(), data.getError().getMeta().getCaptchaSid());
                    } else {
                        mLoginView.showApiResponseError(data, () -> auth(socialType, id, token, captchaSid, captchaKey));
                    }

                }, throwable -> {
                    mLoginView.hideProgress();
                    mLoginView.showNetworkError(() -> auth(socialType, id, token, captchaSid, captchaKey));
                });
        mLoginView.addSubscription(subscription);
    }

}
