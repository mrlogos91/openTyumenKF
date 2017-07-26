package com.opentmn.opentmn.model;

import android.support.annotation.StringDef;

/**
 * Created by Alexey Antonchik on 12.12.16.
 */
@StringDef({SocialType.VK, SocialType.FB})
public @interface SocialType {

    String VK = "vk";
    String FB = "fb";
}
