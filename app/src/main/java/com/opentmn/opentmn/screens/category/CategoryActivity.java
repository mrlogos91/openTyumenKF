package com.opentmn.opentmn.screens.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.widget.Toolbar;

import сom.opentmn.opentmn.R;

/**
 * Created by kost on 09.01.17.
 */

public class CategoryActivity extends BaseActivity {

    public static final String EXTRA_GAME_KEY = "game";
    public static final String EXTRA_ROUND_NUMBER_KEY = "round_number";

    public static void startActivity(Context context, Game game, int roundNumber) {
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra(EXTRA_GAME_KEY, game);
        intent.putExtra(EXTRA_ROUND_NUMBER_KEY, roundNumber);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Game game = (Game) getIntent().getSerializableExtra(EXTRA_GAME_KEY);
        int roundNumber = getIntent().getIntExtra(EXTRA_ROUND_NUMBER_KEY, 0);

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
                .replace(R.id.content_frame, CategoryFragment.getInstance(game, roundNumber))
                .commit();
    }
}
