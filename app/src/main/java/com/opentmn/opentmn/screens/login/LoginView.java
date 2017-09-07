package com.opentmn.opentmn.screens.login;

import com.opentmn.opentmn.model.UsersCount;
import com.opentmn.opentmn.screens.base.BaseView;

/**
 * Created by Alexey Antonchik on 15.12.16.
 */

public interface LoginView extends BaseView {


    void authVK();

    void authFB();

    void authOK();

    void openEmailAuth();

    void openEmailRegistration();

    void showRules();

    void startMain();

    void showUsersCount(UsersCount usersCount);

    void showCaptchaDialog(String socialType, String id, String token, String captchaImage, String captchaSid);

}
