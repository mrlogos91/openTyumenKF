package com.opentmn.opentmn.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.AnswerAlias;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.Question;
import com.opentmn.opentmn.model.Round;
import com.opentmn.opentmn.model.User;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import сom.opentmn.opentmn.R;

/**
 * Created by kost on 14.01.17.
 */

public class LobbyRoundView extends FrameLayout {

    @BindView(R.id.number_text_view)
    TextView mNumTextView;
    @BindView(R.id.category_text_view)
    TextView mCategoryTextView;
    @BindViews({R.id.quest_1_image_view, R.id.quest_2_image_view, R.id.quest_3_image_view})
    ImageView[] mQuestImageViews;
    @BindViews({R.id.quest_enemy_1_image_view, R.id.quest_enemy_2_image_view, R.id.quest_enemy_3_image_view})
    ImageView[] mEnemyQuestImageViews;


    public LobbyRoundView(Context context) {
        super(context);
    }

    public LobbyRoundView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setupView(context);
    }

    public LobbyRoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setupView(context);
    }

    private void setupView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_lobby_round, this);

        ButterKnife.bind(this);
    }

    public void setupWithRound(Round round, int index, Game game) {
        User myUser = RepositoryProvider.provideKeyValueStorage().getUser();
        boolean isCreator = game.isCreator(myUser);
        mNumTextView.setText(String.valueOf(index + 1));
        mCategoryTextView.setText(round.getCategory().getName());

        if(isCreator) {
            for (int i = 0; i < round.getQuestions().size(); i++) {
                Question question = round.getQuestions().get(i);

                if (question.getCreatorAnswer() == null)
                    mQuestImageViews[i].setImageResource(R.mipmap.lobby_round_2);
                else if (question.getCreatorAnswer().equals(AnswerAlias.ANSWER_TRUE))
                    mQuestImageViews[i].setImageResource(R.mipmap.lobby_round_1);
                else
                    mQuestImageViews[i].setImageResource(R.mipmap.lobby_round_3);

                if (question.getFollowerAnswer() == null)
                    mEnemyQuestImageViews[i].setImageResource(R.mipmap.lobby_round_2);
                else if (question.getFollowerAnswer().equals(AnswerAlias.ANSWER_TRUE))
                    mEnemyQuestImageViews[i].setImageResource(R.mipmap.lobby_round_1);
                else
                    mEnemyQuestImageViews[i].setImageResource(R.mipmap.lobby_round_3);
            }
        } else {
            for (int i = 0; i < round.getQuestions().size(); i++) {
                Question question = round.getQuestions().get(i);

                if (question.getCreatorAnswer() == null)
                    mEnemyQuestImageViews[i].setImageResource(R.mipmap.lobby_round_2);
                else if (question.getCreatorAnswer().equals(AnswerAlias.ANSWER_TRUE))
                    mEnemyQuestImageViews[i].setImageResource(R.mipmap.lobby_round_1);
                else
                    mEnemyQuestImageViews[i].setImageResource(R.mipmap.lobby_round_3);

                if (question.getFollowerAnswer() == null)
                    mQuestImageViews[i].setImageResource(R.mipmap.lobby_round_2);
                else if (question.getFollowerAnswer().equals(AnswerAlias.ANSWER_TRUE))
                    mQuestImageViews[i].setImageResource(R.mipmap.lobby_round_1);
                else
                    mQuestImageViews[i].setImageResource(R.mipmap.lobby_round_3);
            }
        }
    }

    public void setupWithRound(int index, String title) {
        mNumTextView.setText(String.valueOf(index + 1));
        mCategoryTextView.setText(title);
        for(int i = 0; i < 3; i++) {
            mQuestImageViews[i].setImageResource(R.mipmap.lobby_round_2);
            mEnemyQuestImageViews[i].setImageResource(R.mipmap.lobby_round_2);
        }
    }
}
