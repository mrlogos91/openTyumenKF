package com.opentmn.opentmn.model;

/**
 * Created by kost on 14.01.17.
 */

public interface GameStatus {
    int CURRENT = 0;
    int INVITE = 1;
    int WAIT = 2;
    int BROKEN = 3;
    int NO_FOLLOWER = 4;

    int CHOOSE_CATEGORY = 5;
    int ENEMY_CHOOSE_CATEGORY = 6;
    int MY_TURN = 7;
    int ENEMY_ROUND_1 = 8;
    int ENEMY_ROUND_2 = 9;
    int ENEMY_ROUND_3 = 10;
    int ALL_ANSWERED = 11;
}
