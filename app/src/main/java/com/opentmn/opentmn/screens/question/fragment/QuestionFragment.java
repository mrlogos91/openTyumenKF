package com.opentmn.opentmn.screens.question.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.opentmn.opentmn.Config;
import com.opentmn.opentmn.model.Answer;
import com.opentmn.opentmn.model.AnswerAlias;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.Question;
import com.opentmn.opentmn.screens.base.BaseFragment;
import com.opentmn.opentmn.screens.question.QuestionsActivity;
import com.opentmn.opentmn.screens.question.QuestionsPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import сom.opentmn.opentmn.R;

/**
 * Created by kost on 11.01.17.
 */

public class QuestionFragment extends BaseFragment implements QuestionView {

    private final static String BUNDLE_GAME_KEY = "game";
    private final static String BUNDLE_QUEST_NUM_KEY = "quest_num";
    private final static String BUNDLE_ROUND_NUM_KEY = "round_num";

    public static QuestionFragment newInstance(Game game, int roundNumber, int questionNumber) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_GAME_KEY, game);
        args.putInt(BUNDLE_ROUND_NUM_KEY, roundNumber);
        args.putInt(BUNDLE_QUEST_NUM_KEY, questionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private QuestionPresenter mPresenter;

    @BindView(R.id.question_click_view)
    View mQuestionClickView;
    @BindViews({R.id.answer_1_button, R.id.answer_2_button, R.id.answer_3_button, R.id.answer_4_button})
    Button[] mAnswerButtons;
    @BindViews({R.id.quest_1_image_view, R.id.quest_2_image_view, R.id.quest_3_image_view, R.id.quest_4_image_view, R.id.quest_5_image_view})
    ImageView[] mQuestImageViews;
    @BindViews({R.id.quest_1_text_view, R.id.quest_2_text_view, R.id.quest_3_text_view, R.id.quest_4_text_view, R.id.quest_5_text_view})
    TextView[] mQuestTextViews;
    @BindViews({R.id.quest_1_layout, R.id.quest_2_layout, R.id.quest_3_layout, R.id.quest_4_layout, R.id.quest_5_layout})
    ViewGroup[] mQuestLayouts;
    @BindView(R.id.question_text_view)
    TextView mQuestionTextView;
    @BindView(R.id.category_text_view)
    TextView mCategoryTextView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.hint_view)
    View mHintView;
    @BindView(R.id.author_text_view)
    TextView mAuthorTextView;
    @BindView(R.id.pic_image_view)
    ImageView mPicImageView;
    @BindView(R.id.question_with_image_layout)
    ViewGroup mQuestWithPicLayout;
    @BindView(R.id.question_with_img_text_view)
    TextView mQuestwithPicTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        Bundle args = getArguments();
        //Game game = (Game) args.getSerializable(BUNDLE_GAME_KEY);
        QuestionsActivity questionsActivity = (QuestionsActivity) getActivity();
        Game game = questionsActivity.getPresenter().getGame();
        int questNumber = args.getInt(BUNDLE_QUEST_NUM_KEY);
        int roundNumber = args.getInt(BUNDLE_ROUND_NUM_KEY);
        mPresenter = new QuestionPresenter(this, game, roundNumber, questNumber);
        mPresenter.init();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick({R.id.answer_1_button, R.id.answer_2_button, R.id.answer_3_button, R.id.answer_4_button})
    void answerButtonClick(View view) {
        int index = Integer.parseInt(view.getTag().toString());
        mPresenter.onAnswerClicked(index);
    }

    @OnClick(R.id.question_click_view)
    void questionViewClick() {
        mPresenter.onQuestionViewClick();
    }

    @OnClick(R.id.hint_view)
    void hintViewClick() {
        mHintView.setVisibility(View.GONE);
    }

    @Override
    public void showHintView() {
        mHintView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showQuestion(Question question) {
        mQuestionClickView.setEnabled(false);
        List<Answer> answers = question.getAnswers();
        for(int i = 0; i < answers.size(); i++) {
            mAnswerButtons[i].setBackgroundResource(R.drawable.button_answer_selector);
            mAnswerButtons[i].setText(answers.get(i).getName());
            mAnswerButtons[i].setEnabled(true);
        }
        String author = question.getAuthor();
        if(author == null || author.length() == 0) {
            mAuthorTextView.setVisibility(View.GONE);
        } else {
            mAuthorTextView.setText("Автор вопроса: " + author);
            mAuthorTextView.setVisibility(View.VISIBLE);
        }
        if(question.getPicture() == null) {
            mQuestWithPicLayout.setVisibility(View.GONE);
            mQuestionTextView.setText(question.getName());
            mQuestionTextView.setVisibility(View.VISIBLE);
        } else {
            mQuestionTextView.setVisibility(View.GONE);
            mQuestwithPicTextView.setText(question.getName());
            Glide.with(getActivity()).load(Config.SERVER + question.getPicture()).into(mPicImageView);
            mQuestWithPicLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void disableButtons() {
        for(Button button : mAnswerButtons) {
            button.setEnabled(false);
        }
        mQuestionClickView.setEnabled(true);
    }

    @Override
    public void showRightAnswer(int index) {
        mAnswerButtons[index].setBackgroundResource(R.mipmap.game_answer_btn_right);
    }

    @Override
    public void showWrongAnswer(int index) {
        mAnswerButtons[index].setBackgroundResource(R.mipmap.game_answer_btn_wrong);
    }

    @Override
    public void showQuestionResult(int index, boolean success) {
        mQuestImageViews[2].setImageResource(success ? R.mipmap.num_of_question_3 : R.mipmap.num_of_question_2);
    }

    @Override
    public void questionAnswered(int questionNumber, Game game) {
        QuestionsActivity questionsActivity = (QuestionsActivity) getActivity();
        QuestionsPresenter questionsPresenter = questionsActivity.getPresenter();
        questionsPresenter.setGame(game);
        questionsPresenter.questionAnswered(questionNumber);
    }

    @Override
    public void showCategoryName(String categoryName) {
        mCategoryTextView.setText(categoryName);
    }

    @Override
    public void showAnswersResults(String[] answers, int questionNumber) {
        int offset = 2 - questionNumber;
        for(int i = 0; i < 5; i++) {
            if(i < offset || i > offset + 2) {
                mQuestLayouts[i].setVisibility(View.INVISIBLE);
            } else {
                int index = i - offset;
                mQuestTextViews[i].setText(String.valueOf(index + 1));
                String answer = answers[index];
                ImageView imageView = mQuestImageViews[i];
                if(answer == null)
                    imageView.setImageResource(R.mipmap.num_of_question_1);
                else if(answer.equals(AnswerAlias.ANSWER_TRUE))
                    imageView.setImageResource(R.mipmap.num_of_question_3);
                else
                    imageView.setImageResource(R.mipmap.num_of_question_2);
                mQuestLayouts[i].setVisibility(View.VISIBLE);
            }
        }
        /*for(int i = 0; i < answers.length; i++) {
            String answer = answers[i];
            ImageView imageView = mQuestImageViews[i];
            if(answer == null)
                imageView.setImageResource(R.mipmap.num_of_question_1);
            else if(answer.equals(AnswerAlias.ANSWER_TRUE))
                imageView.setImageResource(R.mipmap.num_of_question_3);
            else
                imageView.setImageResource(R.mipmap.num_of_question_2);
        }*/
    }

    @Override
    public void showTimerProgress(int progress) {
        mProgressBar.setProgress(progress);
    }

    public QuestionPresenter getPresenter() {
        return mPresenter;
    }

    public void setGame(Game game) {
        mPresenter.setGame(game);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.stopTimer();
    }
}
