package com.opentmn.opentmn.screens.login_by_email;

import com.opentmn.opentmn.screens.base.BaseView;

/**
 * Created by Alexey Antonchik on 16.12.16.
 */

public interface AuthView extends BaseView {

    void startMain();

    void init();

    void startRecovery();
}
