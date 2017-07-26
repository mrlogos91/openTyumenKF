package com.opentmn.opentmn.screens.rules;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;

import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import сom.opentmn.opentmn.R;

/**
 * Created by Alexey Antonchik on 26.12.16.
 */

public class ServiceRulesActivity extends BaseActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ServiceRulesActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.webView)
    WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        ButterKnife.bind(this);
        mToolBar.setNavigationImageRes(Toolbar.NAVIGATION_BACK_ICON);
        mToolBar.setOnNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mWebView.loadUrl("http://opentmn.ru/privacy-policy");
        mWebView.getSettings();
        mWebView.setBackgroundColor(Color.TRANSPARENT);
    }
}
