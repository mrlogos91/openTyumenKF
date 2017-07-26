package com.opentmn.opentmn.network.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alexey Antonchik on 11.12.16.
 */

public class ApiResponseModel<T> implements Serializable {

    @SerializedName("status")
    private boolean mStatus;

    @SerializedName("data")
    private T mData;

    @SerializedName("error")
    private Error mError;

    public boolean isSuccess(){
        return mStatus;
    }

    public T getData() {
        return mData;
    }

    public Error getError() {
        return mError;
    }
}
