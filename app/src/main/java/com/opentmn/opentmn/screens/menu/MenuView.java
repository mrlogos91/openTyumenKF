package com.opentmn.opentmn.screens.menu;

import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseView;

/**
 * Created by kost on 06.03.17.
 */

public interface MenuView extends BaseView {

    void showUser(User user);
}
