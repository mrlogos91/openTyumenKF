package com.opentmn.opentmn.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kost on 26.01.17.
 */

public class PushData implements Serializable {

    @SerializedName("user_avatar")
    private String userAvatar;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("smile_id")
    private String smileId;

    public String getUserAvatar() {
        return userAvatar;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getSmileId() {
        return smileId;
    }
}
