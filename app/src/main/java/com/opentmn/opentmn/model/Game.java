package com.opentmn.opentmn.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kost on 26.12.16.
 */

public class Game implements Serializable {

    public Game(){}

    public Game(String name) {
        id = -1;
        this.name = name;
    }

    @SerializedName("id")
    private int id = 0;

    @SerializedName("game_type_id")
    private int gameTypeId = 0;

    @SerializedName("creator")
    private User creator;

    @SerializedName("follower")
    private User follower;

    @SerializedName("is_finished")
    private int isFinished;

    @SerializedName("rounds")
    private List<Round> rounds = new ArrayList<>();

    @SerializedName("winner")
    private int winner;

    @SerializedName("date_create")
    private long dateCreate;

    private int status = -1;

    private String name;

    public int getId() {
        return id;
    }

    public int getGameTypeId() {
        return gameTypeId;
    }

    public User getCreator() {
        return creator;
    }

    public User getFollower() {
        return follower;
    }

    public int getIsFinished() {
        return isFinished;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public String getName() {
        return name;
    }

    public int getWinner() {
        return winner;
    }

    public long getDateCreate() {
        return dateCreate;
    }

    public int getStatus(User user) {
        if(status != -1) return status;
        if(follower.getId() == -1 && creator.getId() != -1) {
            status = GameStatus.NO_FOLLOWER;
        } else if(creator.getId() == -1) {
            status = GameStatus.BROKEN;
        } else if(follower.getIsApproved() == 1) {
            status = GameStatus.CURRENT;
        } else if(creator.getId() == user.getId()) {
            status = GameStatus.WAIT;
        } else {
            status = GameStatus.INVITE;
        }
        return status;
    }

    public boolean isCreator(User user) {
        if(creator.getId() == user.getId())
            return true;
        else return false;
    }

    public boolean isAllAnswered(User user) {
        if(rounds.size() < 3)
            return false;
        boolean isCreator = isCreator(user);
        int myRounds = getRoundFinishedCount(user, false);
        int enemyRounds = getRoundFinishedCount(isCreator ? follower : creator, true);
        if(myRounds == 3 && enemyRounds == 3)
            return true;
        else
            return false;
    }

    public boolean isActive(User user) {
        boolean isCreator = isCreator(user);
        if(gameTypeId == GameType.TRAINING) {
            if(rounds.size() == 3) {
                for(Round round : rounds) {
                    if(round.countAnswered(isCreator) == 0) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        } else {
            int myRounds = getRoundFinishedCount(user, false);
            int enemyRounds = getRoundFinishedCount(isCreator ? follower : creator, true);
            if(myRounds < enemyRounds || myRounds < rounds.size()) {
                return true;
            } else if(isCreator && rounds.size() == 1) {
                return true;
            } else if(!isCreator && rounds.size() == 2) {
                return true;
            } else {
                return false;
            }
        }
    }

    public int getRoundFinishedCount(User user, boolean all) {
        int count = 0;
        boolean isCreator = isCreator(user);
        for(Round round: rounds) {
            if(round.isRoundFinished(isCreator, all))
                count++;
        }
        return count;
    }

    public int getActiveStatus(User user) {
        if(isAllAnswered(user)) {
            return GameStatus.ALL_ANSWERED;
        } else {
            boolean isCreator = isCreator(user);
            for(int i = 0; i < rounds.size(); i++) {
                if(!rounds.get(i).isRoundFinished(isCreator, false)) {
                    if(i > 0 && !rounds.get(i - 1).isRoundFinished(!isCreator, true)) {
                        return GameStatus.ENEMY_ROUND_1 + i - 1;
                        //return "Играет раунд " + String.valueOf(i);
                    } else {
                        return GameStatus.MY_TURN;
                    }
                }
            }
            if(rounds.size() == 1 && isCreator) {
                if(rounds.get(0).isRoundFinished(!isCreator, true))
                    return GameStatus.CHOOSE_CATEGORY;
                else
                    return GameStatus.ENEMY_ROUND_1;
            } else if(rounds.size() == 2 && getGameTypeId() == GameType.TRAINING) {
                return GameStatus.CHOOSE_CATEGORY;
            } else if(rounds.size() == 2 && !isCreator) {
                if(rounds.get(1).isRoundFinished(!isCreator, true))
                    return GameStatus.CHOOSE_CATEGORY;
                else
                    return GameStatus.ENEMY_ROUND_2;
            } else {
                int count = getRoundFinishedCount(user, false);
                int enemyCount = getRoundFinishedCount(isCreator ? follower : creator, true);
                if(enemyCount < count) {
                    return GameStatus.ENEMY_ROUND_1 + enemyCount;
                }
                return GameStatus.ENEMY_CHOOSE_CATEGORY;
            }
        }
        /*if(isAllAnswered(user)) {
            return "Игра закончена";
        } else {
            boolean isCreator = isCreator(user);
            for(int i = 0; i < rounds.size(); i++) {
                if(!rounds.get(i).isRoundFinished(isCreator, false)) {
                    if(i > 0 && !rounds.get(i - 1).isRoundFinished(!isCreator, true)) {
                        return "Играет раунд " + String.valueOf(i);
                    } else {
                        return "Ваш ход";
                    }
                }
            }
            if(rounds.size() == 1 && isCreator) {
                if(rounds.get(0).isRoundFinished(!isCreator, true))
                    return "Вы выбираете\nкатегорию";
                else
                    return "Играет раунд 1";
            } else if(rounds.size() == 2 && getGameTypeId() == GameType.TRAINING) {
                return "Вы выбираете\nкатегорию";
            } else if(rounds.size() == 2 && !isCreator) {
                if(rounds.get(1).isRoundFinished(!isCreator, true))
                    return "Вы выбираете\nкатегорию";
                else
                    return "Играет раунд 2";
            } else {
                int count = getRoundFinishedCount(user, false);
                int enemyCount = getRoundFinishedCount(isCreator ? follower : creator, true);
                if(enemyCount < count) {
                    return "Играет раунд " + String.valueOf(enemyCount + 1);
                }
                return "Выбирает категорию";
            }
        }*/
    }

    public boolean isChooseCategory(User user) {
        boolean isCreator = isCreator(user);
        if((isCreator && rounds.size() == 1) || (!isCreator && rounds.size() == 2)){
            return true;
        }
        return false;
    }
}
