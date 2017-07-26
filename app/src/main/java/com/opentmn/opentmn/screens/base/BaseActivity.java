package com.opentmn.opentmn.screens.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.keyvalue.KeyValueStorage;
import com.opentmn.opentmn.model.PushData;
import com.opentmn.opentmn.network.model.ApiResponseModel;
import com.opentmn.opentmn.network.model.Error;
import com.opentmn.opentmn.screens.dialogs.ErrorDialog;
import com.opentmn.opentmn.screens.dialogs.StickerDialog;
import com.opentmn.opentmn.screens.lobby.LobbyActivity;
import com.opentmn.opentmn.screens.question.QuestionsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import сom.opentmn.opentmn.R;

/**
 * Created by kost on 13.12.16.
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    private CompositeSubscription mCompositeSubscription;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Идет загрузка");
        mProgressDialog.setMessage("Пожалуйста подождите...");
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    public void showApiResponseError(@NonNull ApiResponseModel apiResponseModel, ErrorDialog.OnDialogButtonClickListener onRepeatClickListener) {
        Error error = apiResponseModel.getError();
        if(error != null && error.getMessage() != null && error.getMessage().length() > 0) {
            String message = error.getMessage();
            if(error.getDesc() != null && error.getDesc().length() > 0)
                message = message + ". " + error.getDesc();
            ErrorDialog dialog = ErrorDialog.getInstance(message);
            dialog.show(getSupportFragmentManager(), null);
            dialog.setOnDialogButtonClickListener(onRepeatClickListener);
        } else {
            String title = getResources().getString(R.string.alert_error_500_title);
            String message = getResources().getString(R.string.alert_error_500_message);
            ErrorDialog dialog = ErrorDialog.getInstance(title, message);
            dialog.show(getSupportFragmentManager(), null);
            dialog.setOnDialogButtonClickListener(onRepeatClickListener);
        }
    }

    public void showToast(@StringRes int res) {
        Toast.makeText(this.getApplication(), getResources().getString(res), Toast.LENGTH_SHORT).show();
    }

    public void showToast(@NonNull String text) {
        Toast.makeText(this.getApplication(), text, Toast.LENGTH_SHORT).show();
    }

    public void showNetworkError(ErrorDialog.OnDialogButtonClickListener onRepeatClickListener) {
        String message = getResources().getString(R.string.error_network);
        ErrorDialog dialog = ErrorDialog.getInstance(message);
        dialog.show(getSupportFragmentManager(), "error");
        dialog.setOnDialogButtonClickListener(onRepeatClickListener);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        EventBus.getDefault().register(this);
        PushData pushData = RepositoryProvider.provideKeyValueStorage().getSavedPushData();
        if(pushData != null && !(this instanceof LobbyActivity)) {
            Log.d(TAG, "push data not null");
            StickerDialog stickerDialog = StickerDialog.getInstance(pushData.getUserName(), pushData.getUserAvatar(), Integer.parseInt(pushData.getSmileId()));
            stickerDialog.show(getSupportFragmentManager(), null);
            RepositoryProvider.provideKeyValueStorage().setSavedPushData(null);
        } else if(pushData == null) {
            Log.d(TAG, "push data null");
        } else {
            Log.d(TAG, "LobbyActivity");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStickerPushed(PushData event) {
        Log.d(TAG, "onPushEvent");
        KeyValueStorage storage = RepositoryProvider.provideKeyValueStorage();
        storage.setPushData(null);
        if(this instanceof QuestionsActivity) {
            Log.d(TAG, "save event");
            storage.setSavedPushData(event);
        } else {
            Log.d(TAG, "show dialog");
            StickerDialog stickerDialog = StickerDialog.getInstance(event.getUserName(), event.getUserAvatar(), Integer.parseInt(event.getSmileId()));
            stickerDialog.show(getSupportFragmentManager(), null);
        }
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        if(mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = null;
        }
        super.onStop();
    }

    public void addSubscription(Subscription subscription) {
        if(mCompositeSubscription == null)
            mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(subscription);
    }

    public void showProgress() {
        mProgressDialog.show();
    }

    public void hideProgress() {
        mProgressDialog.dismiss();
    }

}
