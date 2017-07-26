package com.opentmn.opentmn.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alexey Antonchik on 11.12.16.
 */

public class Category implements Serializable {

    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }
}
