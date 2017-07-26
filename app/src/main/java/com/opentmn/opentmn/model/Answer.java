package com.opentmn.opentmn.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kost on 26.12.16.
 */

public class Answer implements Serializable {

    public Answer(){}

    public Answer(Answer answer) {
        alias = answer.getAlias();
        name = answer.getName();
    }

    @SerializedName("alias")
    private String alias = "";

    @SerializedName("name")
    private String name = "";

    public String getAlias() {
        return alias;
    }

    public String getName() {
        return name;
    }
}
