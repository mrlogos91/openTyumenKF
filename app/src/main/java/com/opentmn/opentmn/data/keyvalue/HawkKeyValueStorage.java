package com.opentmn.opentmn.data.keyvalue;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.opentmn.opentmn.model.PushData;
import com.opentmn.opentmn.model.SocialUser;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

import com.opentmn.opentmn.model.User;

import static com.opentmn.opentmn.Config.LOG_TAG;
import static com.opentmn.opentmn.data.keyvalue.Keys.PUSH_DATA;
import static com.opentmn.opentmn.data.keyvalue.Keys.UID;

/**
 * Created by Alexey Antonchik on 12.12.16.
 */


public class HawkKeyValueStorage implements KeyValueStorage{

    public HawkKeyValueStorage(@NonNull Context context) {
        Hawk.init(context)
                .setEncryption(new NoEncryption()).build();
    }

    @Override
    public  String getUIID() {
        String uid = Hawk.get(UID, "");
        if(uid.length() == 0){
            uid = generateUID();
        }
        Log.d(LOG_TAG, "get uid: " + uid);
        return uid;
    }

    @Override
    public void setUIID(String uid) {
        Hawk.put(UID, uid);
    }

    @Nullable
    @Override
    public User getUser() {
        return Hawk.get(Keys.USER, null);
    }

    @Override
    public void setUser(@Nullable User user) {
        Hawk.put(Keys.USER, user);
    }

    public  String generateUID() {
        SecureRandom random = new SecureRandom();
        String uid = new BigInteger(260, random).toString(64);
        setUIID(uid);
        Log.d(LOG_TAG, "set uid: " + uid);
        return uid;
    }

    @Override
    public void setSocialUser(@Nullable SocialUser user) {
        Hawk.put(Keys.SOCIAL_USER, user);
    }

    @Nullable
    @Override
    public SocialUser getSocialUser() {
        return Hawk.get(Keys.SOCIAL_USER, null);
    }

    public int getGameId() {
        return Hawk.get(Keys.GAME_ID, NO_GAME_ID);
    }

    public void setGameId(int id) {
        Hawk.put(Keys.GAME_ID, id);
    }

    @Override
    public PushData getPushData() {
        return Hawk.get(Keys.PUSH_DATA, null);
    }

    @Override
    public void setPushData(PushData pushData) {
        Hawk.put(Keys.PUSH_DATA, pushData);
    }

    @Override
    public PushData getSavedPushData() {
        return Hawk.get(Keys.SAVED_PUSH_DATA, null);
    }

    @Override
    public void setSavedPushData(PushData pushData) {
        Hawk.put(Keys.SAVED_PUSH_DATA, pushData);
    }

    @Override
    public void clear() {
        setUser(null);
        setSocialUser(null);
        setPushData(null);
        setSavedPushData(null);
        setGameId(KeyValueStorage.NO_GAME_ID);
    }
}
