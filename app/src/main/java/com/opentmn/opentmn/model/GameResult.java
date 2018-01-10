package com.opentmn.opentmn.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kost on 11.01.17.
 */

public class GameResult implements Serializable {

    @SerializedName("winner")
    private boolean winner;

    @SerializedName("rating")
    private int rating;

    @SerializedName("rating_added")
    private int ratingAdded;

    @SerializedName("enemy_name")
    private String enemyName = "";

    @SerializedName("is_draw")
    private int isDraw;

    @SerializedName("money_added")
    private int moneyAdded;

    @SerializedName("enemy_id")
    private int enemyId;

    @SerializedName("type")
    private int type;

    @SerializedName("game_id")
    private int gameId;

    @SerializedName("count_games")
    private int countGames;

    public boolean isWinner() {
        return winner;
    }

    public int getRating() {
        return rating;
    }

    public int getRatingAdded() {
        return ratingAdded;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public int getIsDraw() {
        return isDraw;
    }

    public int getMoneyAdded() {
        return moneyAdded;
    }

    public int getEnemyId() {
        return enemyId;
    }

    public int getType() {
        return type;
    }

    public int getGameId() {
        return gameId;
    }

    public int getCountGames() {
        return countGames;
    }

}
