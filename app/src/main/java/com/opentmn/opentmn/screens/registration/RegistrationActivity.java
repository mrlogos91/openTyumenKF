package com.opentmn.opentmn.screens.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.opentmn.opentmn.MainActivity;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.screens.menu.MenuActivity;
import com.opentmn.opentmn.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.opentmn.opentmn.R;

/**
 * Created by Alexey Antonchik on 27.12.16.
 */

public class RegistrationActivity extends BaseActivity implements RegistrationView {


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RegistrationActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.enter) void enterClick(){
        if(mCheckBox.isChecked()) {
            mRegistrationPresenter.auth(mEmailEdit.getText().toString(), mPassEdit.getText().toString(), mNameEdit.getText().toString());
        }else {
            //TODO
            showToast("Необходимо согласие на обработку персональных данных");
        }
    }
    @BindView(R.id.name_et)
    EditText mNameEdit;
    @BindView(R.id.email_et)
    EditText mEmailEdit;
    @BindView(R.id.pass_et)
    EditText mPassEdit;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.checkBox)
    CheckBox mCheckBox;


    private RegistrationPresenter mRegistrationPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        mRegistrationPresenter = new RegistrationPresenter(this);
        mRegistrationPresenter.init();

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
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
