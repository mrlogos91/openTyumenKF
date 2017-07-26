package com.opentmn.opentmn.screens.friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.SocialUser;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.screens.invite.InviteActivity;
import com.opentmn.opentmn.widget.Toolbar;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import сom.opentmn.opentmn.R;

import static com.opentmn.opentmn.Config.LOG_TAG;

/**
 * Created by kost on 24.01.17.
 */

public class FriendsActivity extends BaseActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FriendsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationImageRes(Toolbar.NAVIGATION_BACK_ICON);
        toolbar.setOnNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new FriendsFragment())
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                String email =  res.email;
                String id =  res.userId;
                String token =  res.accessToken;
                Log.d(LOG_TAG, email + " " + id + " " + token);

                SocialUser socialUser = new SocialUser(id, token);
                RepositoryProvider.provideKeyValueStorage().setSocialUser(socialUser);
                InviteActivity.start(FriendsActivity.this);
            }
            @Override
            public void onError(VKError error) {
                //showToast(getString(R.string.login_error));
                Log.d(LOG_TAG, "User didn't pass Authorization");
                // User didn't pass Authorization
            }
        })){

        }
    }
}