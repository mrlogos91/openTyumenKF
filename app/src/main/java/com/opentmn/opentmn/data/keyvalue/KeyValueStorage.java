package com.opentmn.opentmn.data.keyvalue;

import android.support.annotation.Nullable;

import com.opentmn.opentmn.model.PushData;
import com.opentmn.opentmn.model.SocialUser;
import com.opentmn.opentmn.model.User;

/**
 * Created by Alexey Antonchik on 12.12.16.
 */

public interface KeyValueStorage {

    public static int NO_GAME_ID = -1;

    String getUIID();

    void  setUIID(String uid);

    @Nullable
    User getUser();

    void setUser(@Nullable User user);

    @Nullable
    SocialUser getSocialUser();

    void setSocialUser(@Nullable SocialUser user);

    int getGameId();

    void setGameId(int id);

    PushData getPushData();

    void setPushData(PushData pushData);

    PushData getSavedPushData();

    void setSavedPushData(PushData pushData);

    void clear();

}
