package com.opentmn.opentmn.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kost on 22.01.17.
 */

public class SocialUser implements Serializable {

    public SocialUser(){}

    public SocialUser(String socialId, String token) {
        this.socialId = socialId;
        this.socialToken = token;
    }

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("photo")
    private String photo;

    @SerializedName("profile")
    private User profile;

    private String socialId;

    private String socialToken;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getSocialId() {
        return socialId;
    }

    public String getSocialToken() {
        return socialToken;
    }

    public User getProfile() {
        return profile;
    }
}
