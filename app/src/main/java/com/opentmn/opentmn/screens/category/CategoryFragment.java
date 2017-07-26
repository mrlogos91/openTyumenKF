package com.opentmn.opentmn.screens.category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.opentmn.opentmn.model.Category;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.screens.base.BaseFragment;
import com.opentmn.opentmn.screens.question.QuestionsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.opentmn.opentmn.R;

/**
 * Created by kost on 24.12.16.
 */

public class CategoryFragment extends BaseFragment implements CategoryView {

    public static CategoryFragment getInstance(Game game, int roundNumber) {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(CategoryActivity.EXTRA_GAME_KEY, game);
        args.putSerializable(CategoryActivity.EXTRA_ROUND_NUMBER_KEY, roundNumber);
        categoryFragment.setArguments(args);
        return categoryFragment;
    }

    private CategoryPresenter mCategoryPresenter;

    Animation slideLeft1, slideRight, slideLeft2;

    @BindView(R.id.data_view)
    View mDataView;
    @BindView(R.id.category_1_button)
    Button mCategory1Button;
    @BindView(R.id.category_2_button)
    Button mCategory2Button;
    @BindView(R.id.category_3_button)
    Button mCategory3Button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        Game game = (Game) args.getSerializable(CategoryActivity.EXTRA_GAME_KEY);
        int roundNumber = args.getInt(CategoryActivity.EXTRA_ROUND_NUMBER_KEY);

        slideLeft1 = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_in_left);
        slideRight = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_in_right);
        slideLeft2 = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_in_left);

        /*mCategory1Button.setVisibility(View.GONE);
        mCategory2Button.setVisibility(View.GONE);
        mCategory3Button.setVisibility(View.GONE);*/
        mCategoryPresenter = new CategoryPresenter(this, game, roundNumber);
        mCategoryPresenter.init();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @OnClick(R.id.category_1_button)
    void category1Click() {
        mCategoryPresenter.onCategoryClick(0);
    }

    @OnClick(R.id.category_2_button)
    void category2Click() {
        mCategoryPresenter.onCategoryClick(1);
    }

    @OnClick(R.id.category_3_button)
    void category3Click() {
        mCategoryPresenter.onCategoryClick(2);
    }

    @Override
    public void showCategoryList(List<Category> categoryList) {
        if(categoryList.size() > 0) {
            mCategory1Button.setText(categoryList.get(0).getName());
        }
        if(categoryList.size() > 1) {
            mCategory2Button.setText(categoryList.get(1).getName());
        }
        if(categoryList.size() > 2) {
            mCategory3Button.setText(categoryList.get(2).getName());
        }
        mDataView.setVisibility(View.VISIBLE);
        /*mCategory1Button.setVisibility(View.VISIBLE);
        slideLeft1.setStartOffset(0);
        mCategory1Button.startAnimation(slideLeft1);
        mCategory2Button.setVisibility(View.VISIBLE);
        slideRight.setStartOffset(300);
        mCategory2Button.startAnimation(slideRight);
        mCategory3Button.setVisibility(View.VISIBLE);
        slideLeft2.setStartOffset(600);
        mCategory3Button.startAnimation(slideLeft2);*/
    }

    @Override
    public void startGame(Game game, int roundNumber) {
        QuestionsActivity.start(getActivity(), game, roundNumber);
        getActivity().finish();
    }
}
