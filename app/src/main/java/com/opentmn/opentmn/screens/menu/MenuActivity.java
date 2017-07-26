package com.opentmn.opentmn.screens.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.opentmn.opentmn.Config;
import com.opentmn.opentmn.MainActivity;
import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.screens.base.BaseFragment;
import com.opentmn.opentmn.screens.login.LoginActivity;
import com.opentmn.opentmn.screens.profile.ProfileFragment;
import com.opentmn.opentmn.utils.RxSchedulers;
import com.opentmn.opentmn.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscription;
import сom.opentmn.opentmn.R;

/**
 * Created by kost on 13.01.17.
 */

public class MenuActivity extends BaseActivity implements MenuView {

    private final static String EXTRA_IS_GAMES = "is_games";

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MenuActivity.class);
        context.startActivity(intent);
    }

    public static void startAfterGame(Context context) {
        Intent intent = new Intent(context, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(EXTRA_IS_GAMES, true);
        context.startActivity(intent);
    }

    private MenuPresenter mPresenter;

    @BindView(R.id.ava_image_view)
    ImageView mAvaImageView;
    @BindView(R.id.name_text_view)
    TextView mNameTextView;
    @BindView(R.id.score_text_view)
    TextView mScoreTextView;
    @BindView(R.id.score_points_text_view)
    TextView mScorePointsTextView;
    @BindView(R.id.nav_view)
    FrameLayout mNavLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isGames = getIntent().getBooleanExtra(EXTRA_IS_GAMES, false);
        setContentView(R.layout.activity_menu);

        ButterKnife.bind(this);
        mPresenter = new MenuPresenter(this);
        mPresenter.init();

        if(isGames) {
            MainActivity.startActivity(this, R.id.nav_play, getString(R.string.my_games_title));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void showUser(User user) {
        mNameTextView.setText(user.getName());
        mScoreTextView.setText(String.valueOf(user.getCoins()));
        mScorePointsTextView.setText(String.valueOf(user.getRating()));
        if (user.getAvatar() != null)
            Glide.with(this).load(Config.SERVER + user.getAvatar()).bitmapTransform(new CropCircleTransformation(this)).placeholder(R.mipmap.ava_placeholder_b).into(mAvaImageView);
    }

    @OnClick(R.id.ava_layout)
    void onAvaClick() {
        showFragment(R.id.nav_profile, getString(R.string.menu_item_profile));
    }

    @OnClick(R.id.friends_view)
    void onFriendsClick() {
        showFragment(R.id.nav_friends, getString(R.string.menu_item_friends));
    }

    @OnClick(R.id.rating_view)
    void onRatingClick() {
        showFragment(R.id.nav_rate, getString(R.string.menu_item_rating));
    }

    @OnClick(R.id.shop_view)
    void onShopClick() {
        showFragment(R.id.nav_shop, getString(R.string.menu_item_shop));
    }

    @OnClick(R.id.play_button)
    void onPlayClick() {
        showFragment(R.id.nav_play, getString(R.string.my_games_title));
    }

    @OnClick(R.id.add_question_button)
    void onAddQuestionClick() {
        showFragment(R.id.nav_question, getString(R.string.menu_item_add_question));
    }

    @OnClick(R.id.rules_button)
    void onRulesClick() {
        showFragment(R.id.nav_rules, getString(R.string.menu_item_rules));
    }

    @OnClick(R.id.logout_button)
    void onLogoutClick() {
        logout();
    }

    private void showFragment(int id, String title) {
        MainActivity.startActivity(this, id, title);
    }

    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        User user = RepositoryProvider.provideKeyValueStorage().getUser();
        Subscription subscription = RepositoryProvider.provideRepository().delToken(user.getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(this::showProgress)
                .doAfterTerminate(this::hideProgress)
                .subscribe(response -> {
                    RepositoryProvider.provideKeyValueStorage().clear();
                    startActivity(intent);
                    finish();
                }, throwable -> {
                    RepositoryProvider.provideKeyValueStorage().clear();
                    startActivity(intent);
                    finish();
                });
        addSubscription(subscription);
    }
}
