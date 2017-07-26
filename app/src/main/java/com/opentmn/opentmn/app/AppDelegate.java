package com.opentmn.opentmn.app;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.vk.sdk.VKSdk;

import io.fabric.sdk.android.Fabric;
import сom.opentmn.opentmn.R;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.yandex.metrica.YandexMetrica;

/**
 * Created by Alexey Antonchik on 12.12.16.
 */

public class AppDelegate extends Application {

    public void onCreate() {
        super.onCreate();
        RepositoryProvider.init(this);
        VKSdk.initialize(this);
        Fabric.with(this, new Crashlytics());
        // Initialize the SDK before executing any other operations,
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        YandexMetrica.activate(getApplicationContext(), getString(R.string.yandex_api_key));
        YandexMetrica.enableActivityAutoTracking(this);
    }

}
