package com.opentmn.opentmn.screens.base;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import com.opentmn.opentmn.network.model.ApiResponseModel;
import com.opentmn.opentmn.screens.dialogs.ErrorDialog;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kost on 24.12.16.
 */

public class BaseFragment extends Fragment {

    private CompositeSubscription mCompositeSubscription;

    public void addSubscription(Subscription subscription) {
        if(mCompositeSubscription == null)
            mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(subscription);
    }

    public void showApiResponseError(@NonNull ApiResponseModel apiResponseModel, ErrorDialog.OnDialogButtonClickListener onRepeatClickListener) {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.showApiResponseError(apiResponseModel, onRepeatClickListener);
    }

    public void showToast(@StringRes int res) {

    }

    public void showNetworkError(ErrorDialog.OnDialogButtonClickListener onRepeatClickListener){
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.showNetworkError(onRepeatClickListener);
    }

    public void showProgress() {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.showProgress();
    }

    public void hideProgress() {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.hideProgress();
    }

    @Override
    public void onStop() {
        if(mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = null;
        }
        super.onStop();
    }
}
