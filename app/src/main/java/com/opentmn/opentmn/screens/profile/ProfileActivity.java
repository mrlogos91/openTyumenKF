package com.opentmn.opentmn.screens.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.widget.Toolbar;

import com.opentmn.opentmn.R;

/**
 * Created by kost on 31.12.16.
 */

public class ProfileActivity extends BaseActivity {

    public static final String EXTRA_USER_KEY = "user";

    public static void startActivity(Context context, User user) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(EXTRA_USER_KEY, user);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        User user = (User) getIntent().getSerializableExtra(EXTRA_USER_KEY);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationImageRes(Toolbar.NAVIGATION_BACK_ICON);
        toolbar.setOnNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, ProfileFragment.getInstance(user))
                .commit();
    }
}
