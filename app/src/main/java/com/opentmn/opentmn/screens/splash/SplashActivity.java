package com.opentmn.opentmn.screens.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.opentmn.opentmn.MainActivity;
import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.screens.login.LoginActivity;
import com.opentmn.opentmn.screens.menu.MenuActivity;

import сom.opentmn.opentmn.R;

/**
 * Created by Alexey Antonchik on 21.12.16.
 */

public class SplashActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(RepositoryProvider.provideKeyValueStorage().getUser() != null){
            MenuActivity.startActivity(this);
        } else {
            LoginActivity.start(this);
        }
        finish();
    }
}
