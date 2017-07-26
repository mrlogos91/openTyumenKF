package com.opentmn.opentmn.screens.add_question;

import android.net.Uri;

import com.opentmn.opentmn.model.Category;
import com.opentmn.opentmn.screens.base.BaseView;

import java.util.List;

/**
 * Created by kost on 22.01.17.
 */

public interface AddQuestionView extends BaseView {

    void showImage(Uri uri);

    void hideImage();

    void showAuthorName(String name);

    void showCategoriesDialog(List<Category> categoryList);

    void showCategory(Category category);

    void showErrorDialog(String error);

    void showSuccessDialog();

    void clearFields();
}
