package com.opentmn.opentmn.network.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import butterknife.BindView;
import сom.opentmn.opentmn.R;

/**
 * Created by kost on 23.01.17.
 */

public class Meta implements Serializable {

    @SerializedName("captcha_img")
    private String captchaImg;

    @SerializedName("captcha_sid")
    private String captchaSid;

    public String getCaptchaImg() {
        return captchaImg;
    }

    public String getCaptchaSid() {
        return captchaSid;
    }
}
