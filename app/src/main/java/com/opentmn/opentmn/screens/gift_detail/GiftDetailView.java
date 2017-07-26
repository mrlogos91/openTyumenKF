package com.opentmn.opentmn.screens.gift_detail;

import com.opentmn.opentmn.model.Gift;
import com.opentmn.opentmn.screens.base.BaseView;

/**
 * Created by kost on 25.01.17.
 */

public interface GiftDetailView extends BaseView {

    void showGift(Gift gift);

    void showCoins(int coins);

    void showBuyDialog();

    void showNoMoneyDialog();

    void showSuccessDialog();
}
