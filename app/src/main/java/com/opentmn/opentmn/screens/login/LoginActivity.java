package com.opentmn.opentmn.screens.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.gson.JsonParser;
import com.opentmn.opentmn.model.Alias;
import com.opentmn.opentmn.model.UsersCount;
import com.opentmn.opentmn.screens.dialogs.InputDialog;
import com.opentmn.opentmn.screens.menu.MenuActivity;
import com.opentmn.opentmn.screens.page.PageActivity;
import com.opentmn.opentmn.screens.registration.RegistrationActivity;
import com.opentmn.opentmn.widget.Toolbar;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.opentmn.opentmn.R;

import com.opentmn.opentmn.MainActivity;
import com.opentmn.opentmn.model.SocialType;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.screens.login_by_email.AuthActivity;
import com.opentmn.opentmn.utils.FBAuth;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import ru.ok.android.sdk.Odnoklassniki;
import ru.ok.android.sdk.OkAuthListener;
import ru.ok.android.sdk.OkListener;
import ru.ok.android.sdk.util.OkAuthType;
import ru.ok.android.sdk.util.OkScope;

import static com.opentmn.opentmn.Config.LOG_TAG;
import static com.opentmn.opentmn.Config.OK_APP_ID;
import static com.opentmn.opentmn.Config.OK_APP_KEY;
import static com.opentmn.opentmn.Config.REDIRECT_URL;
import static com.opentmn.opentmn.Config.VK_SCOPE;

/**
 * Created by Alexey Antonchik on 07.12.16.
 */

public class LoginActivity extends BaseActivity implements LoginView {

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    Odnoklassniki mOdnoklassnik;
    private LoginPresenter mLoginPresenter;
    private CallbackManager mCallbackManager;
    private static final String html =
        "<font color='#9C8470'>Авторизуясь, вы соглашаетесь с</font>" +
            "<font color='#61A3BB'> правилами игры</font><br>" +
            "<font color='#9C8470'> и </font>" +
            "<font color='#61A3BB'>обработки персональной информации</font>";

    @OnClick(R.id.vkButton)
    void vkClick() {
        mLoginPresenter.clickVK();
    }

    @OnClick(R.id.fbButton)
    void fbClick() {
        mLoginPresenter.clickFB();
    }

    //    @OnClick(R.id.okButton) void  okClick(){
//        mLoginPresenter.clickOK();
//    }
    @OnClick(R.id.enter)
    void enterEmail() {
        mLoginPresenter.clickEnter();
    }

    @OnClick(R.id.reg)
    void regClick() {
        mLoginPresenter.clickRegistration();
    }

    @OnClick(R.id.rules)
    void rulesClick() {
        mLoginPresenter.clickShowRules();
    }

    @BindView(R.id.rules)
    TextView mRulesTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fact_layout)
    View mFactLayout;
    @BindView(R.id.fact_text_view)
    TextView mFactTextView;
    @BindView(R.id.count_text_view)
    TextView mCountTextView;
    @BindView(R.id.count_layout)
    ViewGroup mCountLayout;
    @BindView(R.id.logo_image_view)
    ImageView mLogoImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mCallbackManager = CallbackManager.Factory.create();
        mLoginPresenter = new LoginPresenter(this);
        mLoginPresenter.init();
        FBAuth fbAuth = new FBAuth(this, new FBAuth.AuthInterface() {
            @Override
            public void successAuth(String id, String token, String email) {
                mLoginPresenter.auth(SocialType.FB, id, token, null, null);
            }
        }, mCallbackManager);
        fbAuth.initAuth();
        mRulesTextView.setText(Html.fromHtml(html), TextView.BufferType.SPANNABLE);
        mToolbar.setNavigationImageVisible(false);
        mFactLayout.setVisibility(View.INVISIBLE);
        mCountLayout.setVisibility(View.INVISIBLE);
        mLogoImageView.setVisibility(View.VISIBLE);
        mOdnoklassnik = Odnoklassniki.createInstance(this, OK_APP_ID, OK_APP_KEY);
        findViewById(R.id.okButton)
            .setOnClickListener(new LoginClickListener(OkAuthType.WEBVIEW_OAUTH));
    }

    @Override
    public void authVK() {
        VKSdk.login(LoginActivity.this, VK_SCOPE);
    }

    @Override
    public void authFB() {
        List<String> permissions = new ArrayList<String>();
        permissions.add("email");
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, permissions);
    }

    @Override
    public void authOK() {
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://connect.ok.ru/oauth/authorize?client_id=1253849856&scope=VALUABLE_ACCESS;LONG_ACCESS_TOKEN&response_type=token&redirect_uri=ok1253849856://authorize&layout=m&state=odnoklassniki"));
//        startActivityForResult(browserIntent, 1);
    }

    @Override
    public void openEmailAuth() {
        AuthActivity.startActivity(this);
    }

    @Override
    public void openEmailRegistration() {
        RegistrationActivity.startActivity(this);
    }

    @Override
    public void showRules() {
        PageActivity.startActivity(this, "Правила", Alias.GAME_SERVICE);
    }

    @Override
    public void startMain() {
        MenuActivity.startActivity(this);
        finish();
    }

    @Override
    public void showUsersCount(UsersCount usersCount) {
        mCountTextView.setText(String.valueOf(usersCount.getCount()));
        String desc = usersCount.getDesc();
        mFactTextView.setText(desc);
        mFactLayout.setVisibility(View.VISIBLE);
        mCountLayout.setVisibility(View.VISIBLE);
        if (desc != null && desc.length() > 0) {
            mLogoImageView.setVisibility(View.GONE);
            mFactLayout.setVisibility(View.VISIBLE);
        } else {
            mFactLayout.setVisibility(View.GONE);
            mLogoImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                String email = res.email;
                String id = res.userId;
                String token = res.accessToken;
                Log.d(LOG_TAG, email + " " + id + " " + token);

                mLoginPresenter.auth(SocialType.VK, id, token, null, null);

            }

            @Override
            public void onError(VKError error) {
                //showToast(getString(R.string.login_error));
                Log.d(LOG_TAG, "User didn't pass Authorization");
                // User didn't pass Authorization
            }
        })) {

            mCallbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (Odnoklassniki.getInstance().isActivityRequestOAuth(requestCode)) {
            Odnoklassniki.getInstance().onAuthActivityResult(requestCode, resultCode, data, getAuthListener());
        } else if (Odnoklassniki.getInstance().isActivityRequestViral(requestCode)) {
            Odnoklassniki.getInstance().onActivityResultResult(requestCode, resultCode, data, getToastListener());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void showCaptchaDialog(String socialType, String id, String token, String captchaImage,
        String captchaSid) {
        InputDialog inputDialog = InputDialog.getInstance(captchaImage);
        inputDialog.show(getSupportFragmentManager(), "captcha");
        inputDialog.setOnDialogButtonClickListener(new InputDialog.OnDialogButtonClickListener() {
            @Override
            public void onConfirmClick(DialogFragment dialogFragment, String text) {
                if (text.length() == 0)
                    return;
                dialogFragment.dismiss();
                mLoginPresenter.auth(socialType, id, token, captchaSid, text);
            }
        });
    }

    public OkListener getAuthListener() {
        return new OkListener() {
            @Override
            public void onSuccess(final JSONObject json) {
//                String id = res.userId;
                String token = null;
                try {
                    token = json.getString("access_token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(LoginActivity.this, json.toString(), Toast.LENGTH_LONG).show();
                String finalToken = token;
                mOdnoklassnik.requestAsync("users.getCurrentUser", null, null, new OkListener() {
                    @Override
                    public void onSuccess(JSONObject json) {
                        String id = null;
                        try {
                            id = json.getString("uid");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(LoginActivity.this, "Get current user result: " + json.toString(), Toast.LENGTH_SHORT).show();
                        mLoginPresenter.auth(SocialType.OK, id, finalToken, null, null);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(LoginActivity.this, "Get current user failed: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        };
    }

    public OkListener getToastListener() {
        return new OkAuthListener() {
            @Override
            public void onSuccess(final JSONObject json) {
                try {
                    Toast.makeText(LoginActivity.this,
                        String.format("access_token: %s", json.getString("access_token")),
                        Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                showForm();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(LoginActivity.this,
                    String.format("%s: %s", "Error", error),
                    Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(String error) {
                Toast.makeText(LoginActivity.this,
                    String.format("%s: %s", "Canceled", error),
                    Toast.LENGTH_SHORT).show();
            }
        };
    }

    private class LoginClickListener implements OnClickListener {

        private OkAuthType authType;

        public LoginClickListener(OkAuthType authType) {
            this.authType = authType;
        }

        @Override
        public void onClick(final View view) {
            mOdnoklassnik.requestAuthorization(LoginActivity.this, REDIRECT_URL, authType,
                OkScope.VALUABLE_ACCESS);
        }
    }
}
