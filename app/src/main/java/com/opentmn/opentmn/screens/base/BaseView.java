package com.opentmn.opentmn.screens.base;

import android.support.annotation.StringRes;
import android.view.View;

import com.opentmn.opentmn.network.model.ApiResponseModel;
import com.opentmn.opentmn.screens.dialogs.ConfirmDialog;
import com.opentmn.opentmn.screens.dialogs.ErrorDialog;

import rx.Subscription;

/**
 * Created by Alexey Antonchik on 15.12.16.
 */

public interface BaseView {

    void showProgress();

    void hideProgress();

    void showNetworkError(ErrorDialog.OnDialogButtonClickListener onRepeatClickListener);

    void showToast(@StringRes int text);

    void showApiResponseError(ApiResponseModel apiResponseModel, ErrorDialog.OnDialogButtonClickListener onRepeatClickListener);

    void addSubscription(Subscription subscription);

}
