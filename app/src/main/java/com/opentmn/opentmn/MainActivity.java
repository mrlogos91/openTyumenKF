package com.opentmn.opentmn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.keyvalue.KeyValueStorage;
import com.opentmn.opentmn.model.Alias;
import com.opentmn.opentmn.model.SocialUser;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.add_question.AddQuestionFragment;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.screens.base.BaseFragment;
import com.opentmn.opentmn.screens.friends.FriendsFragment;
import com.opentmn.opentmn.screens.invite.InviteActivity;
import com.opentmn.opentmn.screens.login.LoginActivity;
import com.opentmn.opentmn.screens.menu.MenuActivity;
import com.opentmn.opentmn.screens.my_games.MyGamesFragment;
import com.opentmn.opentmn.screens.page.PageActivity;
import com.opentmn.opentmn.screens.page.PageFragment;
import com.opentmn.opentmn.screens.profile.ProfileFragment;
import com.opentmn.opentmn.screens.profile_edit.ProfileEditActivity;
import com.opentmn.opentmn.screens.rating.RatingFragment;
import com.opentmn.opentmn.screens.shop.GiftsFragment;
import com.opentmn.opentmn.service.RegistrationIntentService;
import com.opentmn.opentmn.utils.RxSchedulers;
import com.opentmn.opentmn.widget.Toolbar;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscription;
import com.opentmn.opentmn.R;

import static com.opentmn.opentmn.Config.LOG_TAG;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String EXTRA_FRAGMENT_ID = "fragment_id";
    private static final String EXTRA_TITLE = "title";

    public static void startActivity(Context context, boolean singleTop) {
        Intent intent = new Intent(context, MainActivity.class);
        if(singleTop) intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void startAfterGame(Context context) {
        MenuActivity.startAfterGame(context);
    }

    public static void startActivity(Context context, int fragmentId, String title) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_FRAGMENT_ID, fragmentId);
        intent.putExtra(EXTRA_TITLE, title);
        context.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.background_image_view)
    ImageView mBackgroundImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //setupNavigationDrawer();
        setupActionBarDrawerToggle();
        //setupDrawerHeader();

        /*mToolbar.setTitle(getString(R.string.my_games_title));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new MyGamesFragment())
                .commit();*/
        checkGameId();

        int fragmentId = getIntent().getIntExtra(EXTRA_FRAGMENT_ID, 0);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        showFragment(fragmentId, title);
        Intent mIntent = new Intent(this, RegistrationIntentService.class);
        startService(mIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /*@OnClick(R.id.ava_layout)
    void onAvaClick() {
        BaseFragment fragment = new ProfileFragment();
        mToolbar.setRightImageRes(Toolbar.ICON_EDIT, mOnProfileEditClickListener);
        mToolbar.setTitle(getString(R.string.menu_item_profile));
        if(fragment != null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
    */

    /*private void setupNavigationDrawer() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mNavLayout.getLayoutParams().width = size.x;
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                InputMethodManager inputMethodManager = (InputMethodManager)  MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }*/

    private void setupActionBarDrawerToggle() {
        mToolbar.setNavigationImageRes(Toolbar.NAVIGATION_MENU_ICON);
        mToolbar.setOnNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        showFragment(id, item.getTitle().toString());
        return true;
    }

    private void showFragment(int id, String title) {
        BaseFragment fragment = null;
        mBackgroundImageView.setVisibility(View.VISIBLE);
        switch (id) {
            case R.id.nav_play:
                fragment = new MyGamesFragment();
                mToolbar.setRightImageRes(0, null);
                break;
            case R.id.nav_rate:
                fragment = new RatingFragment();
                mToolbar.setRightImageRes(Toolbar.ICON_INFO, mOnRateInfoClickListener);
                break;
            case R.id.nav_friends:
                fragment = new FriendsFragment();
                mToolbar.setRightImageRes(0, null);
                break;
            case R.id.nav_shop:
                fragment = new GiftsFragment();
                mToolbar.setRightImageRes(Toolbar.ICON_INFO, mOnShopInfoClickListener);
                break;
            case R.id.nav_question:
                fragment = new AddQuestionFragment();
                mToolbar.setRightImageRes(0, null);
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                mToolbar.setRightImageRes(Toolbar.ICON_EDIT, mOnProfileEditClickListener);
                break;
            case R.id.nav_rules:
                fragment = PageFragment.getInstance(Alias.GAME_RULES);
                mToolbar.setRightImageRes(0, null);
                mBackgroundImageView.setVisibility(View.GONE);
                break;
            case R.id.nav_exit:
                return;

        }


        mToolbar.setTitle(title);
        if(fragment != null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

    }

    private final View.OnClickListener mOnShopInfoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PageActivity.startActivity(MainActivity.this, getString(R.string.menu_item_rules), Alias.SHOP_RULES);
        }
    };

    private final View.OnClickListener mOnRateInfoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PageActivity.startActivity(MainActivity.this, getString(R.string.menu_item_rules), Alias.RATING);
        }
    };

    private final View.OnClickListener mOnProfileEditClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ProfileEditActivity.start(ProfileFragment.REQ_CODE_EDIT, MainActivity.this);
        }
    };

    private void checkGameId() {
        int gameId = RepositoryProvider.provideKeyValueStorage().getGameId();
        User user = RepositoryProvider.provideKeyValueStorage().getUser();
        if(gameId != KeyValueStorage.NO_GAME_ID && user != null) {
            RepositoryProvider.provideRepository().closeGame(gameId, user.getToken())
                    .compose(RxSchedulers.async())
                    .subscribe(response -> {
                        if(response.isSuccess()) {
                            RepositoryProvider.provideKeyValueStorage().setGameId(KeyValueStorage.NO_GAME_ID);
                        }
                    }, throwable ->  {

                    });
        }
    }

    /*private void logout() {
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
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                String email =  res.email;
                String id =  res.userId;
                String token =  res.accessToken;
                Log.d(LOG_TAG, email + " " + id + " " + token);

                SocialUser socialUser = new SocialUser(id, token);
                RepositoryProvider.provideKeyValueStorage().setSocialUser(socialUser);
                InviteActivity.start(MainActivity.this);
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
