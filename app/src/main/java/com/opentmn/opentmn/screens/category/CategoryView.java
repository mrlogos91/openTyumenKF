package com.opentmn.opentmn.screens.category;

import com.opentmn.opentmn.model.Category;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.screens.base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kost on 24.12.16.
 */

public interface CategoryView extends BaseView {

    void showCategoryList(List<Category> categoryList);

    void startGame(Game game, int roundNumber);
}
