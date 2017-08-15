package com.opentmn.opentmn.screens.lobby;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.GameResult;
import com.opentmn.opentmn.model.GameStatus;
import com.opentmn.opentmn.model.PushData;
import com.opentmn.opentmn.model.Round;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.screens.category.CategoryActivity;
import com.opentmn.opentmn.screens.dialogs.ConfirmDialog;
import com.opentmn.opentmn.screens.dialogs.MessageDialog;
import com.opentmn.opentmn.screens.dialogs.StickerDialog;
import com.opentmn.opentmn.screens.question.QuestionsActivity;
import com.opentmn.opentmn.screens.question.QuestionsPagerAdapter;
import com.opentmn.opentmn.screens.result.GameResultActivity;
import com.opentmn.opentmn.widget.Button;
import com.opentmn.opentmn.widget.LobbyHeaderView;
import com.opentmn.opentmn.widget.LobbyRoundView;
import com.opentmn.opentmn.widget.TextView;
import com.opentmn.opentmn.widget.Toolbar;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.opentmn.opentmn.R;

/**
 * Created by kost on 12.01.17.
 */

public class LobbyActivity extends BaseActivity implements LobbyView, View.OnClickListener {

    private final static String EXTRA_GAME_KEY = "game";
    private final static String EXTRA_IS_NEW_GAME_KEY = "is_new";

    public static void startActivity(Context context, Game game) {
        Intent intent = new Intent(context, LobbyActivity.class);
        intent.putExtra(EXTRA_GAME_KEY, game);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Game game, boolean isNewGame) {
        Intent intent = new Intent(context, LobbyActivity.class);
        intent.putExtra(EXTRA_GAME_KEY, game);
        intent.putExtra(EXTRA_IS_NEW_GAME_KEY, isNewGame);
        context.startActivity(intent);
    }

    private LobbyPresenter mPresenter;

    @BindViews({R.id.round_1_view, R.id.round_2_view, R.id.round_3_view})
    LobbyRoundView[] mLobbyRoundViews;
    @BindView(R.id.header_view)
    LobbyHeaderView mHeaderView;
    @BindView(R.id.play_button)
    Button mPlayButton;
    @BindView(R.id.surrender_button)
    Button mSurrenderButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        ButterKnife.bind(this);

        Game game = (Game) getIntent().getSerializableExtra(EXTRA_GAME_KEY);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationImageRes(Toolbar.NAVIGATION_BACK_ICON);
        toolbar.setOnNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        boolean isNewGame = getIntent().getBooleanExtra(EXTRA_IS_NEW_GAME_KEY, false);
        mPresenter = new LobbyPresenter(this, game, isNewGame);
        mPresenter.onCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.init();
    }

    @Override
    public void onClick(View view) {
        mPresenter.onAddFriendClick();
    }

    @OnClick(R.id.play_button)
    void onPlayClick() {
        mPresenter.onPlayClick();
    }

    @OnClick(R.id.surrender_button)
    void onSurrenderClick() {
        mPresenter.onSurrenderClick();
    }

    @Override
    public void showGame(Game game, int status) {
        mHeaderView.setupWithGame(game, this);
        for(int i = 0; i < mLobbyRoundViews.length; i++) {
            if(game.getRounds().size() > i) {
                Round round = game.getRounds().get(i);
                mLobbyRoundViews[i].setupWithRound(round, i, game);
                mLobbyRoundViews[i].setVisibility(View.VISIBLE);
            } else {
                mLobbyRoundViews[i].setVisibility(View.GONE);
            }
        }
        if(game.getRounds().size() == 1) {
            if(status == GameStatus.CHOOSE_CATEGORY) {
                mLobbyRoundViews[1].setupWithRound(1, getString(R.string.lobby_round_widget_choose_category));
                mLobbyRoundViews[1].setVisibility(View.VISIBLE);
            } else if(status == GameStatus.ENEMY_CHOOSE_CATEGORY) {
                mLobbyRoundViews[1].setupWithRound(1, getString(R.string.lobby_round_widget_enemy_choose_category));
                mLobbyRoundViews[1].setVisibility(View.VISIBLE);
            }
        }
        if(game.getRounds().size() == 2) {
            if(status == GameStatus.CHOOSE_CATEGORY) {
                mLobbyRoundViews[2].setupWithRound(2, getString(R.string.lobby_round_widget_choose_category));
                mLobbyRoundViews[2].setVisibility(View.VISIBLE);
            } else if(status == GameStatus.ENEMY_CHOOSE_CATEGORY) {
                mLobbyRoundViews[2].setupWithRound(2, getString(R.string.lobby_round_widget_enemy_choose_category));
                mLobbyRoundViews[2].setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void startGame(Game game, int roundNumber) {
        QuestionsActivity.start(this, game, roundNumber);
    }

    @Override
    public void startCategories(Game game, int roundNumber) {
        CategoryActivity.startActivity(this, game, roundNumber);
    }

    @Override
    public void showCategoryNotSelectedDialog() {
        MessageDialog messageDialog = MessageDialog.getInstance(getString(R.string.wait_enemy_choose_category), null);
        messageDialog.show(getSupportFragmentManager(), "info");
    }

    @Override
    public void showEnemyRoundNotFinishedDialog() {
        MessageDialog messageDialog = MessageDialog.getInstance(getString(R.string.wait_enemy_end_round), null);
        messageDialog.show(getSupportFragmentManager(), "info");
    }

    @Override
    public void startResult(Game game, GameResult gameResult) {
        GameResultActivity.start(this, game, gameResult);
        finish();
    }

    @Override
    public void showSurrenderConfirmDialog() {
        ConfirmDialog confirmDialog = ConfirmDialog.getInstance("ПОДТВЕРЖДЕНИЕ", "Вы действительно хотите сдаться?");
        confirmDialog.show(getSupportFragmentManager(), "surrender");
        confirmDialog.setOnDialogButtonClickListener(new ConfirmDialog.OnDialogButtonClickListener() {
            @Override
            public void onCancelClick() {

            }

            @Override
            public void onConfirmClick() {
                mPresenter.onSurrenderConfirmClick();
            }
        });
    }

    @Override
    public void showUserAddedDialog(User user) {
        MessageDialog messageDialog = MessageDialog.getInstance("ИНФОРМАЦИЯ", "Пользователь " + user.getName() + " добавлен в список друзей");
        messageDialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void showEnabledButton() {
        mPlayButton.setEnabled(true);
        mPlayButton.setBackgroundResource(R.drawable.button_blue_selector);
    }

    @Override
    public void showDisabledButton() {
        mPlayButton.setEnabled(false);
        mPlayButton.setBackgroundResource(R.mipmap.btn_play_unactive);
    }

    @Override
    public void showStickerDialog(PushData pushData) {
        StickerDialog stickerDialog = StickerDialog.getInstance(pushData.getUserName(), pushData.getUserAvatar(), Integer.parseInt(pushData.getSmileId()));
        stickerDialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void showTrainingDialog() {
        MessageDialog messageDialog = MessageDialog.getInstance(getString(R.string.alert_info_title), getString(R.string.lobby_training_dialog_message));
        messageDialog.show(getSupportFragmentManager(), null);
    }
}
