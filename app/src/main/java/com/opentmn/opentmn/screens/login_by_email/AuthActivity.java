package com.opentmn.opentmn.screens.login_by_email;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.opentmn.opentmn.MainActivity;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.screens.menu.MenuActivity;
import com.opentmn.opentmn.screens.restore.RestoreActivity;
import com.opentmn.opentmn.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import сom.opentmn.opentmn.R;

/**
 * Created by Alexey Antonchik on 16.12.16.
 */

public class AuthActivity extends BaseActivity implements AuthView {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AuthActivity.class);
        context.startActivity(intent);
    }
    @OnClick(R.id.forgot) void forgotClick(){
        mAuthPresenter.clickForgot();
    }
    @OnClick(R.id.enter) void enterClick(){
        mAuthPresenter.auth(mEmailEdit.getText().toString(), mPassEdit.getText().toString());
    }
    @BindView(R.id.email_et)
    EditText mEmailEdit;
    @BindView(R.id.pass_et)
    EditText mPassEdit;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private AuthPresenter mAuthPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);
        ButterKnife.bind(this);
        mAuthPresenter = new AuthPresenter(this);
        mAuthPresenter.init();
    }


    @Override
    public void startMain() {
        MenuActivity.startActivity(this);
        finish();
    }

    @Override
    public void init() {
        mToolbar.setNavigationImageRes(Toolbar.NAVIGATION_BACK_ICON);
        mToolbar.setOnNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void startRecovery() {
        RestoreActivity.startActivity(this);
    }

}
