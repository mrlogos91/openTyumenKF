package com.opentmn.opentmn.screens.question;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.Question;
import com.opentmn.opentmn.screens.question.fragment.QuestionFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kost on 11.01.17.
 */

public class QuestionsPagerAdapter extends FragmentPagerAdapter {

    private List<Question> mQuestions;
    private Game mGame;
    private int mRoundNumber;
    private QuestionFragment[] mFragments;

    public QuestionsPagerAdapter(FragmentManager fm, Game game, int roundNumber) {
        super(fm);

        mGame = game;
        mRoundNumber = roundNumber;
        mQuestions = new ArrayList<>();
        mFragments = new QuestionFragment[3];
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("QuestionsPagerAdapter", "getItem " + String.valueOf(position));
        QuestionFragment questionFragment = QuestionFragment.newInstance(mGame, mRoundNumber, position);
        mFragments[position] = questionFragment;
        return questionFragment;
    }

    @Override
    public int getCount() {
        return mQuestions.size();
    }

    public void setQuestions(List<Question> questions) {
        mQuestions = questions;
        Log.d("QuestionsPagerAdapter", "notifyDataSetChanged");
        notifyDataSetChanged();
    }

    public void setGame(Game game) {
        mGame = game;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void startTimer(int position) {
        if(mFragments[position] != null)
            mFragments[position].getPresenter().startTimer();
    }
}
