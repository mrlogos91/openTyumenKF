package com.opentmn.opentmn.screens.restore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.screens.dialogs.MessageDialog;
import com.opentmn.opentmn.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.opentmn.opentmn.R;

/**
 * Created by Alexey Antonchik on 27.12.16.
 */

public class RestoreActivity extends BaseActivity implements RestoreView {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RestoreActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.email_et)
    EditText mEmailEditText;

    private RestorePresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore);
        ButterKnife.bind(this);

        mToolbar.setNavigationImageRes(Toolbar.NAVIGATION_BACK_ICON);
        mToolbar.setOnNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mPresenter = new RestorePresenter(this);
    }

    @OnClick(R.id.enter)
    void onEnterClick() {
        mPresenter.onRestoreClick(mEmailEditText.getText().toString());
    }

    @Override
    public void showSuccessDialog() {
        MessageDialog messageDialog = MessageDialog.getInstance("ИНФОРМАЦИЯ", "На указанный e-mail отправлен новый пароль");
        messageDialog.show(getSupportFragmentManager(), "success");
        messageDialog.setOnDismissListener(new MessageDialog.OnDismissListener() {
            @Override
            public void onDismiss() {
                finish();
            }
        });
    }
}
