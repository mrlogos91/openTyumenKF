package com.opentmn.opentmn.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kost on 24.01.17.
 */

public class VisibleResults implements Serializable {

    @SerializedName("games")
    private List<Integer> games;

    public List<Integer> getGames() {
        return games;
    }
}
