package com.opentmn.opentmn.screens.shop;

import com.opentmn.opentmn.model.Gift;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseView;

import java.util.List;

/**
 * Created by kost on 04.01.17.
 */

public interface GiftsView extends BaseView {

    void showGifts(List<Gift> gifts);

    void showGiftDetail(Gift gift);

    void showCoins(int coins);

    void showUser(User user);
}
