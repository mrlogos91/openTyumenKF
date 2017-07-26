package com.opentmn.opentmn.screens.question;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.opentmn.opentmn.MainActivity;
import com.opentmn.opentmn.model.Answer;
import com.opentmn.opentmn.model.Category;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.GameResult;
import com.opentmn.opentmn.model.Question;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.screens.dialogs.ConfirmDialog;
import com.opentmn.opentmn.screens.dialogs.MessageDialog;
import com.opentmn.opentmn.screens.lobby.LobbyActivity;
import com.opentmn.opentmn.screens.result.GameResultActivity;
import com.opentmn.opentmn.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import сom.opentmn.opentmn.R;

/**
 * Created by kost on 24.12.16.
 */

public class QuestionsActivity extends BaseActivity implements QuestionsView, ConfirmDialog.OnDialogButtonClickListener {

    private static final String GAME_KEY = "game";
    private static final String ROUND_NUMBER_KEY = "round_number";

    private QuestionsPresenter mPresenter;
    private QuestionsPagerAdapter mAdapter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    public static void start(Context context, Game game, int roundNumber) {
        Intent intent = new Intent(context, QuestionsActivity.class);
        intent.putExtra(GAME_KEY, game);
        intent.putExtra(ROUND_NUMBER_KEY, roundNumber);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_pager);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        Game game = (Game) intent.getSerializableExtra(GAME_KEY);
        int roundNumber = intent.getIntExtra(ROUND_NUMBER_KEY, 0);

        mToolbar.setNavigationImageRes(Toolbar.NAVIGATION_BACK_ICON);
        mToolbar.setOnNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mToolbar.setTitle("Раунд " + String.valueOf(roundNumber + 1));

        mAdapter = new QuestionsPagerAdapter(getSupportFragmentManager(), game, roundNumber);
        mViewPager.setAdapter(mAdapter);
        mPresenter = new QuestionsPresenter(this, game, roundNumber);
        mPresenter.init();
        //setupNavBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void showMessageDialog(String title, String subtitle) {
        MessageDialog messageDialog = MessageDialog.getInstance(title, subtitle);
        messageDialog.show(getSupportFragmentManager(), "message_dialog");
        messageDialog.setOnDismissListener(new MessageDialog.OnDismissListener() {
            @Override
            public void onDismiss() {
                mAdapter.startTimer(0);
            }
        });

    }

    private void setupNavBar() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public void onCancelClick() {

    }

    @Override
    public void onConfirmClick() {
        mPresenter.onLeaveConfirmClick();
    }

    @Override
    public void showQuestions(List<Question> questions) {
        mAdapter.setQuestions(questions);
    }

    @Override
    public void nextRound(Game game, ArrayList<Category> categories) {
        //QuestionsActivity.start(this, game);
        //finish();
    }

    @Override
    public void showQuestion(int number) {
        mViewPager.setCurrentItem(number);
        mAdapter.startTimer(number);
    }

    @Override
    public void setGame(Game game) {
        mAdapter.setGame(game);
    }

    @Override
    public void showResults(Game game, GameResult result) {
        GameResultActivity.start(this, game, result);
        finish();
    }

    @Override
    public void startLobby(Game game) {
        finish();
    }

    @Override
    public void onBackPressed() {
        ConfirmDialog confirmDialog = ConfirmDialog.getInstance("Внимание", "Вы действительно хотите завершить раунд? Все оставшиеся вопросы будут считаться неверно отвеченными!");
        confirmDialog.show(getSupportFragmentManager(), "confirm");
        confirmDialog.setOnDialogButtonClickListener(this);
    }

    public QuestionsPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void leave() {
        finish();
    }
}
