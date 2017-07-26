package com.opentmn.opentmn;

import com.vk.sdk.VKScope;

/**
 * Created by Alexey Antonchik on 11.12.16.
 */

public final class Config {

    //NETWORK
    public final static String SERVER = "http://opentmn.ru";
    public final static String API_ENDPOINT = SERVER + "/api/v2/";
    public final static String DEVICE_TYPE = "android";


    //Socials
    public final static String VK_APP_KEY = "0YTAiJeRetS92sMyHmCL";
    public final static String VK_APP_ID = "5755635";
    public final static String FB_APP_ID = "339670136401972";
    public final static String FB_SCOPE = "public_profile, email, user_friends";
    public final static String[] VK_SCOPE = new String[] {VKScope.EMAIL, VKScope.WALL, VKScope.OFFLINE, VKScope.FRIENDS, VKScope.MESSAGES};

    //LOGS
    public static final String LOG_TAG = "MYTYUMEN_LOGS";
}
