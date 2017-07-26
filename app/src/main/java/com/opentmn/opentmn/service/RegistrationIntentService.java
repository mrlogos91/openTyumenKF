package com.opentmn.opentmn.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.User;

import rx.schedulers.Schedulers;

/**
 * Created by kost on 25.01.17.
 */

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    public RegistrationIntentService(String name) {
        super(name);
    }

    @Override
    public void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "GCM Registration Token: " + token);
        sendTokenToServer(token);
    }

    private void sendTokenToServer(String deviceToken) {
        User user = RepositoryProvider.provideKeyValueStorage().getUser();
        if(user == null)
            return;
        RepositoryProvider.provideRepository().addToken(user.getToken(), deviceToken)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(response -> {

                }, throwable -> {

                });
    }
}
