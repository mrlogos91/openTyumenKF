package com.opentmn.opentmn.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kost on 04.01.17.
 */

public class Gift implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name = "";

    @SerializedName("price")
    private int price;

    @SerializedName("count")
    private int count;

    @SerializedName("desc")
    private String desc = "";

    @SerializedName("image")
    private String image = "";

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }
}
