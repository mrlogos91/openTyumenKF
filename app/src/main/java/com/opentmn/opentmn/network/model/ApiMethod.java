package com.opentmn.opentmn.network.model;

import android.support.annotation.StringDef;

/**
 * Created by Alexey Antonchik on 12.12.16.
 */

@StringDef({ApiMethod.AUTH,
ApiMethod.USERS,
ApiMethod.PASSWORD_RECOVERY})
public @interface ApiMethod {

    String AUTH = "sessions";
    String GAMES = "games";
    String ROUNDS = "rounds";
    String QUESTIONS = "questions";
    String ANSWERS = "answers";
    String USERS = "users";
    String PASSWORD_RECOVERY = "passwords/recovery";
    String CATEGORY = "categories";
    String GIFTS = "gifts";
    String FRIENDS = "friends";
    String RESULTS = "results";
    String PAGES = "pages";
    String SOCIALS = "socials";
    String VISIBLE_RESULTS = "visible_results";
    String BLOCKS = "blocks";
    String ORDERS = "orders";
    String SMILES = "smiles";
    String NOTIFICATIONS = "notifications";

}
