package com.opentmn.opentmn.screens.page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.widget.Toolbar;

import com.opentmn.opentmn.R;

/**
 * Created by kost on 12.01.17.
 */

public class PageActivity extends BaseActivity {

    public static final String EXTRA_TITLE_KEY = "title";
    public static final String EXTRA_ALIAS_KEY = "alias";

    public static void startActivity(Context context, String title, String alias) {
        Intent intent = new Intent(context, PageActivity.class);
        intent.putExtra(EXTRA_TITLE_KEY, title);
        intent.putExtra(EXTRA_ALIAS_KEY, alias);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationImageRes(Toolbar.NAVIGATION_BACK_ICON);
        toolbar.setOnNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        String title = intent.getStringExtra(EXTRA_TITLE_KEY);
        String alias = intent.getStringExtra(EXTRA_ALIAS_KEY);
        toolbar.setTitle(title);

        PageFragment pageFragment = PageFragment.getInstance(alias);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, pageFragment)
                .commit();
    }
}
