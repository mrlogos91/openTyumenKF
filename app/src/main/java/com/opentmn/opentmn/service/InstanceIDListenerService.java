package com.opentmn.opentmn.service;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by kost on 25.01.17.
 */

public class InstanceIDListenerService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        Intent mIntent = new Intent(this, RegistrationIntentService.class);
        startService(mIntent);
    }
}
