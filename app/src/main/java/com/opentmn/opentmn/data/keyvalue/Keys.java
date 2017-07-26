package com.opentmn.opentmn.data.keyvalue;

import android.support.annotation.StringDef;

/**
 * Created by Alexey Antonchik on 12.12.16.
 */
@StringDef({Keys.UID,
        Keys.TOKEN,
        Keys.USER})
public @interface Keys {
    String UID = "UID";
    String TOKEN = "TOKEN";
    String USER = "USER_2";
    String SOCIAL_USER = "SOCIAL_USER";
    String GAME_ID = "GAME_ID";
    String PUSH_DATA = "PUSH_DATA";
    String SAVED_PUSH_DATA = "SAVED_PUSH_DATA";
}
