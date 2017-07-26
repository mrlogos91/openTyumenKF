package com.opentmn.opentmn.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Alexey Antonchik on 11.12.16.
 */

public class User implements Serializable {

    @SerializedName("id")
    private int id = -1;
    @SerializedName("avatar")
    private String  avatar = "";
    @SerializedName("name")
    private String name = "";
    @SerializedName("email")
    private String email = "";
    @SerializedName("phone")
    private String phone = "";
    @SerializedName("rating")
    private int rating = 0;
    @SerializedName("wins")
    private int wins = 0;
    @SerializedName("louses")
    private int louses = 0;
    @SerializedName("draws")
    private int draws = 0;
    @SerializedName("count_answers")
    private int countAnswers = 0;
    @SerializedName("coins")
    private int coins = 0;
    @SerializedName("position")
    private int position = 0;
    @SerializedName("token")
    private String token = "";
    @SerializedName("is_approved")
    private int isApproved;
    @SerializedName("is_surrender")
    private int isSurrender;
    @SerializedName("answers_count")
    private int answersCount = 0;
    @SerializedName("friends")
    private int friends = 0;
    @SerializedName("is_friend")
    private boolean isFriend;
    @SerializedName("categories")
    private List<Category> categories;
    @SerializedName("is_block")
    private boolean isBlock;
    @SerializedName("token_wins")
    private int tokenWins = 0;
    @SerializedName("token_losses")
    private int tokenLoses = 0;
    @SerializedName("gender_id")
    private int genderId = 0;

    public User() {
    }

    @NonNull
    private static User test(){
        User user = new User();
        user.setId(3);
        user.setName("Тролололо");
        user.setAvatar("http://troll-iface.ucoz.ru/_pu/41/69877689.jpg");
        user.setPhone("+79026235903");
        user.setRating(100);
        user.setWins(100);
        user.setLouses(0);
        user.setDraws(0);
        user.setCoins(0);
        user.setToken("Токентокентокен");
        return user;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(@NonNull String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLouses() {
        return louses;
    }

    public void setLouses(int louses) {
        this.louses = louses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getToken() {
        return token;
    }

    public void setToken(@NonNull String token) {
        this.token = token;
    }

    public int getCountAnswers() {
        return countAnswers;
    }

    public int getPosition() {
        return position;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public int getIsSurrender() {
        return isSurrender;
    }

    public int getAnswersCount() {
        return answersCount;
    }

    public int getFriends() {
        return friends;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public void setBlock(boolean block) {
        isBlock = block;
    }

    public int getTokenWins() {
        return tokenWins;
    }

    public int getTokenLoses() {
        return tokenLoses;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }


}
