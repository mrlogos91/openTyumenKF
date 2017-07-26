package com.opentmn.opentmn.network.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kost on 15.01.17.
 */

public class Error implements Serializable {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("desc")
    private String desc;

    @SerializedName("meta")
    private Meta meta;


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDesc() {
        return desc;
    }

    public Meta getMeta() {
        return meta;
    }
}
