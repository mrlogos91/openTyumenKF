package com.opentmn.opentmn.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kost on 06.01.17.
 */

public class UsersCount {

    @SerializedName("count")
    private int count = 0;

    @SerializedName("desc")
    private String  desc = "";

    public String getDesc() {
        return desc;
    }

    public int getCount() {
        return count;
    }
}
