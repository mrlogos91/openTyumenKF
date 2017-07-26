package com.opentmn.opentmn.screens.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.opentmn.opentmn.R;

/**
 * Created by kost on 31.12.16.
 */

public class TitleViewHolder extends BaseViewHolder {

    private TextView mTitleTextView;

    public TitleViewHolder(View itemView) {
        super(itemView);

        mTitleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
    }

    public void setTitle(String title) {
        mTitleTextView.setText(title);
    }

}
