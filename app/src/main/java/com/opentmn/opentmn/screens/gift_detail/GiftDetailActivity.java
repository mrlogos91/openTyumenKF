package com.opentmn.opentmn.screens.gift_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.opentmn.opentmn.Config;
import com.opentmn.opentmn.MainActivity;
import com.opentmn.opentmn.model.Alias;
import com.opentmn.opentmn.model.Gift;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.screens.category.CategoryActivity;
import com.opentmn.opentmn.screens.dialogs.GiftDialog;
import com.opentmn.opentmn.screens.dialogs.MessageDialog;
import com.opentmn.opentmn.screens.page.PageActivity;
import com.opentmn.opentmn.widget.TextView;
import com.opentmn.opentmn.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import com.opentmn.opentmn.R;

/**
 * Created by kost on 12.01.17.
 */

public class GiftDetailActivity extends BaseActivity implements GiftDetailView {

    private static final String EXTRA_GIFT_KEY = "gift";

    public static void startActivity(Context context, Gift gift) {
        Intent intent = new Intent(context, GiftDetailActivity.class);
        intent.putExtra(EXTRA_GIFT_KEY, gift);
        context.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.pic_image_view)
    ImageView mPicImageView;
    @BindView(R.id.name_text_view)
    TextView mNameTextView;
    @BindView(R.id.desc_text_view)
    TextView mDescTextView;
    @BindView(R.id.count_text_view)
    TextView mCountTextView;
    @BindView(R.id.coins_text_view)
    TextView mCoinsTextView;
    @BindView(R.id.gift_coins_text_view)
    TextView mGiftCoinsTextView;

    private GiftDetailPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_detail);
        ButterKnife.bind(this);

        mToolbar.setNavigationImageRes(Toolbar.NAVIGATION_BACK_ICON);
        mToolbar.setOnNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolbar.setRightImageRes(Toolbar.ICON_INFO, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PageActivity.startActivity(GiftDetailActivity.this, "Правила", Alias.SHOP_RULES);
            }
        });
        Gift gift = (Gift) getIntent().getSerializableExtra(EXTRA_GIFT_KEY);
        mPresenter = new GiftDetailPresenter(this, gift);
        mPresenter.init();
    }

    @OnClick(R.id.buy_button)
    void onBuyClick() {
        mPresenter.onBuyClick();
    }

    @Override
    public void showGift(Gift gift) {
        mNameTextView.setText(gift.getName());
        mDescTextView.setText(gift.getDesc());
        mCountTextView.setText(String.valueOf(gift.getCount()) + " шт.");
        mGiftCoinsTextView.setText(String.valueOf(gift.getPrice()));
        if(gift.getImage() != null)
            Glide.with(this).load(Config.SERVER + gift.getImage()).into(mPicImageView);
    }

    @Override
    public void showCoins(int coins) {
        mCoinsTextView.setText(String.valueOf(coins));
    }

    @Override
    public void showBuyDialog() {
        GiftDialog giftDialog = GiftDialog.getInstance();
        giftDialog.show(getSupportFragmentManager(), "gift");
        giftDialog.setOnDialogButtonClickListener(new GiftDialog.OnDialogButtonClickListener() {
            @Override
            public void onConfirmClick(DialogFragment dialogFragment, String text) {
                if(text.length() == 0)
                    return;
                dialogFragment.dismiss();
                mPresenter.onEmailConfirmClick(text);
            }
        });
    }

    @Override
    public void showNoMoneyDialog() {
        MessageDialog messageDialog = MessageDialog.getInstance("ОШИБКА", "Не хватает монеток!");
        messageDialog.show(getSupportFragmentManager(), "no_money");
    }

    @Override
    public void showSuccessDialog() {
        MessageDialog messageDialog = MessageDialog.getInstance(getString(R.string.alert_gift_success_title), getString(R.string.alert_gift_success_message));
        messageDialog.show(getSupportFragmentManager(), "success");
        messageDialog.setOnDismissListener(new MessageDialog.OnDismissListener() {
            @Override
            public void onDismiss() {
                finish();
            }
        });
    }
}
