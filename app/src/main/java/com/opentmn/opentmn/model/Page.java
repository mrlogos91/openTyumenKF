package com.opentmn.opentmn.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kost on 12.01.17.
 */

public class Page {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("body")
    private String body;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
