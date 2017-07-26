package com.opentmn.opentmn.screens.profile.viewholder;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseViewHolder;
import com.opentmn.opentmn.screens.profile.ProfileAdapter;
import com.opentmn.opentmn.utils.FontHelper;
import com.opentmn.opentmn.widget.Button;

import com.opentmn.opentmn.R;

/**
 * Created by kost on 07.01.17.
 */

public class ButtonViewHolder extends BaseViewHolder {

    private Button mButton;

    public ButtonViewHolder(View itemView) {
        super(itemView);

        mButton = (Button) itemView.findViewById(R.id.button);
    }

    public void setupNewGameButton() {
        Resources resources = itemView.getContext().getResources();
        FontHelper.setFont(mButton, resources.getString(R.string.font_heavy_path));
        mButton.setText("Новая игра");
        mButton.setBackgroundResource(R.drawable.button_blue_selector);
        mButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.my_games_new_button_text_size));
        mButton.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mButton.setTextColor(Color.WHITE);
        mButton.setPadding(0, 0, 0, resources.getDimensionPixelSize(R.dimen.button_bottom_padding));
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mButton.getLayoutParams();
        layoutParams.topMargin = 0;
        layoutParams.bottomMargin = 0;
    }

    public void setupFinishedGamesButton() {
        Resources resources = itemView.getContext().getResources();
        FontHelper.setFont(mButton, resources.getString(R.string.font_heavy_path));
        mButton.setText("Завершенные игры");
        mButton.setBackgroundResource(R.drawable.button_red_selector);
        mButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.my_games_new_button_text_size));
        mButton.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mButton.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mButton.setTextColor(Color.WHITE);
        mButton.setPadding(0, 0, 0, resources.getDimensionPixelSize(R.dimen.button_bottom_padding));
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mButton.getLayoutParams();
        layoutParams.topMargin = resources.getDimensionPixelSize(R.dimen.my_games_finished_button_margin);
        layoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.my_games_finished_button_margin);
    }

    public void setupBlackListButton(boolean isMyProfile, User user, ProfileAdapter.OnItemClickListener onItemClickListener) {
        Resources resources = itemView.getContext().getResources();
        FontHelper.setFont(mButton, resources.getString(R.string.font_medium_path));
        if(isMyProfile) {
            mButton.setText("Черный список");
        } else if(user.isBlock()) {
            mButton.setText("Удалить из черного списка");
        } else {
            mButton.setText("Добавить в черный список");
        }
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMyProfile) {
                    onItemClickListener.onBlackListClick();
                } else {
                    onItemClickListener.onBlockClick();
                }
            }
        });
        mButton.setBackgroundDrawable(null);
        mButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.profile_black_list_button_text_size));
        mButton.getLayoutParams().width = resources.getDimensionPixelSize(R.dimen.profile_black_list_button_width);
        mButton.getLayoutParams().height = resources.getDimensionPixelSize(R.dimen.profile_black_list_button_height);
        mButton.setTextColor(resources.getColor(R.color.text_blue_tr));
        mButton.setTransformationMethod(null);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mButton.getLayoutParams();
        layoutParams.bottomMargin = 0;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mButton.setOnClickListener(onClickListener);
    }
}
