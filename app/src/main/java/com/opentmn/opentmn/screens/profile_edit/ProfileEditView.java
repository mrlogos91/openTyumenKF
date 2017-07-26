package com.opentmn.opentmn.screens.profile_edit;

import android.net.Uri;

import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseView;

/**
 * Created by kost on 24.01.17.
 */

public interface ProfileEditView extends BaseView {

    void showUser(User user);

    void showGendersDialog(int selected);

    void showDialog(String title, String message);

    void clearFields();

    void showUserAvatar(Uri uri);
}
