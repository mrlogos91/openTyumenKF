package com.opentmn.opentmn.screens.my_games;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.opentmn.opentmn.MainActivity;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.GameResult;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseFragment;
import com.opentmn.opentmn.screens.dialogs.ConfirmDialog;
import com.opentmn.opentmn.screens.dialogs.MessageDialog;
import com.opentmn.opentmn.screens.history.HistoryActivity;
import com.opentmn.opentmn.screens.lobby.LobbyActivity;
import com.opentmn.opentmn.screens.opponent.OpponentActivity;
import com.opentmn.opentmn.screens.result.GameResultActivity;

import java.util.List;

import сom.opentmn.opentmn.R;

/**
 * Created by kost on 14.01.17.
 */

public class MyGamesFragment extends BaseFragment implements MyGamesView, MyGamesAdapter.OnItemClickListener {

    private Toolbar mHeaderLayout;
    private TextView mRatingTextView;

    private MyGamesPresenter mPresenter;
    private MyGamesAdapter mAdapter;
    private int mHeaderHeight;
    private boolean mAnimating;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_games, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHeaderLayout = (Toolbar) view.findViewById(R.id.header_layout);
        mHeaderHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.shop_header_height);

        mRatingTextView = (TextView) view.findViewById(R.id.rating_text_view);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MyGamesAdapter(getActivity(), this);
        recyclerView.setAdapter(mAdapter);
        setupHeaderAnimation(recyclerView);

        mPresenter = new MyGamesPresenter(this);
    }

    @Override
    public void showUser(User user) {
        mRatingTextView.setText(String.valueOf(user.getRating()));
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.init();
    }

    private void setupHeaderAnimation(RecyclerView recyclerView) {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(mAnimating)
                    return;
                if(dy > 0 && mHeaderLayout.getY() > -mHeaderHeight) {
                    mHeaderLayout.setY((int)mHeaderLayout.getY() - dy);
                } else if(dy < 0 && mHeaderLayout.getY() != 0) {
                    mAnimating = true;
                    mHeaderLayout.animate().setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            mAnimating = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    }).y(0);
                }
            }
        });
    }

    @Override
    public void onGameClick(Game game) {
        mPresenter.onGameClick(game);
    }

    @Override
    public void onAcceptClick(Game game) {
        mPresenter.onAcceptClick(game);
    }

    @Override
    public void onDenyClick(Game game) {
        mPresenter.onDenyClick(game);
    }

    @Override
    public void onNewGameClick() {
        mPresenter.onNewGameClick();;
    }

    @Override
    public void onFinishedGamesClick() {
        mPresenter.onFinishedGamesClick();
    }

    @Override
    public void showGames(List<Game> games) {
        mAdapter.setGames(games);
    }

    @Override
    public void startNewGame() {
        OpponentActivity.startActivity(getActivity());
    }

    @Override
    public void startHistory() {
        HistoryActivity.start(getActivity());
    }

    @Override
    public void startLobby(Game game) {
        LobbyActivity.startActivity(getActivity(), game);
    }

    @Override
    public void showWaitDialog(Game game) {
        MessageDialog messageDialog = MessageDialog.getInstance("ИНФОРМАЦИЯ", "Пользователь " + game.getFollower().getName() + " пока не принял запрос");
        messageDialog.show(getActivity().getSupportFragmentManager(), "info");
    }

    @Override
    public void showSoonDialog() {
        MessageDialog messageDialog = MessageDialog.getInstance("Уже скоро!", "");
        messageDialog.show(getActivity().getSupportFragmentManager(), "soon");
    }

    @Override
    public void showAcceptGameDialog(Game game) {
        ConfirmDialog confirmDialog = ConfirmDialog.getInstance("ПОДТВЕРЖДЕНИЕ", "Начать игру против " + game.getCreator().getName() + "?");
        confirmDialog.show(getActivity().getSupportFragmentManager(), "confirm");
        confirmDialog.setOnDialogButtonClickListener(new ConfirmDialog.OnDialogButtonClickListener() {
            @Override
            public void onCancelClick() {

            }

            @Override
            public void onConfirmClick() {
                mPresenter.onGameConfirmClick(game);
            }
        });
    }

    @Override
    public void showNoFollowerDialog() {
        MessageDialog messageDialog = MessageDialog.getInstance("ИНФОРМАЦИЯ", "К этой игре пока никто не подключился");
        messageDialog.show(getActivity().getSupportFragmentManager(), "info");
    }

    @Override
    public void startGameResult(GameResult gameResult) {
        GameResultActivity.start(getActivity(), null, gameResult);
    }
}
