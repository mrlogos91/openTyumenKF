package com.opentmn.opentmn.screens.result;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.opentmn.opentmn.MainActivity;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.GameResult;
import com.opentmn.opentmn.model.GameType;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.screens.dialogs.MessageDialog;
import com.opentmn.opentmn.screens.lobby.LobbyActivity;
import com.opentmn.opentmn.screens.menu.MenuActivity;
import com.opentmn.opentmn.widget.Toolbar;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.photo.VKUploadImage;
import com.vk.sdk.dialogs.VKShareDialogBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.opentmn.opentmn.R;

/**
 * Created by kost on 03.01.17.
 */

public class GameResultActivity extends BaseActivity implements GameResultView {

    private static final String GAME_EXTRA_KEY = "game";
    private static final String GAME_RESULT_EXTRA_KEY = "game_result";

    private GameResultPresenter mPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.result_text_view)
    TextView mResultTextView;
    @BindView(R.id.points_text_view)
    TextView mPointsTextView;
    @BindView(R.id.background_image_view)
    ImageView mBackgroundImageView;
    @BindView(R.id.share_layout)
    ViewGroup mShareLayout;
    @BindView(R.id.score_view)
    View mScoreView;
    @BindView(R.id.score_win_view)
    View mScoreWinView;
    @BindView(R.id.win_points_text_view)
    TextView mPointsWinTextView;
    @BindView(R.id.win_coins_text_view)
    TextView mCoinsWinTextView;
    @BindView(R.id.enemy_name_text_view)
    TextView mEnemyNameTextView;
    @BindView(R.id.enemy_name_pref_text_view)
    TextView mEnemyNamePrefTextView;
    @BindView(R.id.stickers_view)
    View mStickersView;

    public static void start(Context context, Game game, GameResult result) {
        Intent intent = new Intent(context, GameResultActivity.class);
        intent.putExtra(GAME_EXTRA_KEY, game);
        intent.putExtra(GAME_RESULT_EXTRA_KEY, result);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        ButterKnife.bind(this);

        mToolbar.setNavigationImageVisible(false);
        Game game = (Game) getIntent().getSerializableExtra(GAME_EXTRA_KEY);
        GameResult result = (GameResult) getIntent().getSerializableExtra(GAME_RESULT_EXTRA_KEY);

        mPresenter = new GameResultPresenter(this, game, result);
        mPresenter.init();
    }

    @Override
    public void showResult(GameResult gameResult) {
        if(gameResult.getIsDraw() == 1) {
            mResultTextView.setText(getString(R.string.game_result_draw_title));
            mBackgroundImageView.setImageResource(R.mipmap.congrat_bg_3);
            mPointsTextView.setText(String.valueOf(gameResult.getRatingAdded()));
            mScoreWinView.setVisibility(View.GONE);
            mScoreView.setVisibility(View.VISIBLE);
            mEnemyNamePrefTextView.setText(getString(R.string.game_result_draw_pref));
        } else if(gameResult.isWinner()) {
            mResultTextView.setText(getString(R.string.game_result_win_title));
            mBackgroundImageView.setImageResource(R.mipmap.congrat_bg_1);
            mPointsWinTextView.setText(String.valueOf(gameResult.getRatingAdded()));
            mCoinsWinTextView.setText(String.valueOf(gameResult.getMoneyAdded()));
            mScoreView.setVisibility(View.GONE);
            mScoreWinView.setVisibility(View.VISIBLE);
            mEnemyNamePrefTextView.setText(getString(R.string.game_result_win_pref));
        } else {
            mResultTextView.setText(getString(R.string.game_result_lose_title));
            mBackgroundImageView.setImageResource(R.mipmap.congrat_bg_2);
            mPointsTextView.setText(String.valueOf(gameResult.getRatingAdded()));
            mScoreWinView.setVisibility(View.GONE);
            mScoreView.setVisibility(View.VISIBLE);
            mEnemyNamePrefTextView.setText(getString(R.string.game_result_lose_pref));
        }
        mEnemyNameTextView.setText(gameResult.getEnemyName());
        if(gameResult.getType() == GameType.TRAINING) {
            mStickersView.setVisibility(View.GONE);
            mShareLayout.setVisibility(View.GONE);
        } else {
            mStickersView.setVisibility(View.VISIBLE);
            mShareLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.menu_button)
    void menuButtonClick() {
        MainActivity.startAfterGame(this);
    }

    @OnClick(R.id.play_again_button)
    void playAgainClick() {
        mPresenter.onPlayAgainClick();
    }

    @OnClick(R.id.vk_share_button)
    void vkShareClick() {
        mPresenter.onVkShareClick(this);
    }

    @OnClick(R.id.fb_share_button)
    void fbShareClick() {
        mPresenter.onFbShareClick(this);
    }

    @OnClick({R.id.sticker_1_view, R.id.sticker_2_view, R.id.sticker_3_view})
    void onStickerClick(View view) {
        int smileId;
        if(view.getId() == R.id.sticker_1_view)
            smileId = 1;
        else if(view.getId() == R.id.sticker_2_view)
            smileId = 2;
        else
            smileId = 3;
        mPresenter.onSmileClick(smileId);
    }

    @Override
    public void showMyGames() {
        MainActivity.startAfterGame(this);
    }

    @Override
    public void startLobby(Game game) {
        LobbyActivity.startActivity(this, game);
        finish();
    }

    @Override
    public void showVkShareDialog(String text, String link) {
        VKShareDialogBuilder builder = new VKShareDialogBuilder();
        builder.setText(text);
        builder.setAttachmentLink(text, link);
        //VKPhotoArray vkPhotoArray = new VKPhotoArray();
        //vkPhotoArray.add(new VKApiPhoto("photo308416709_456239077"));
        //builder.setUploadedPhotos(vkPhotoArray);
        builder.show(getSupportFragmentManager(), null);
    }

    @Override
    public void showFbShareDialog(String text, String link) {
        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                .setContentTitle(text)
                .setContentUrl(Uri.parse(link))
                .build();
        ShareDialog.show(this, shareLinkContent);
    }

    @Override
    public void showSmileSentDialog() {
        MessageDialog messageDialog = MessageDialog.getInstance(getString(R.string.game_result_smile_sent_title), getString(R.string.game_result_smile_sent_text));
        messageDialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void showStickerErrorDialog() {
        MessageDialog messageDialog = MessageDialog.getInstance("ИНФОРМАЦИЯ", "Вы уже отправляли сопернику стикер");
        messageDialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onBackPressed() {
        menuButtonClick();
    }
}
