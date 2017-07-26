package com.opentmn.opentmn.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opentmn.opentmn.Config;
import com.opentmn.opentmn.MainActivity;
import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.keyvalue.KeyValueStorage;
import com.opentmn.opentmn.model.PushData;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import сom.opentmn.opentmn.R;

/**
 * Created by kost on 25.01.17.
 */

public class GcmService extends FirebaseMessagingService {

    private static final String TAG = "GcmService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived");
        Map<String, String> data = remoteMessage.getData();
        for (Iterator i = data.keySet().iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            String value = (String) data.get(key);
            Log.d(TAG, key + " = " + value);
        }
        String meta = data.get("meta");
        if(meta != null) {
            Gson gson = new Gson();
            PushData pushData = gson.fromJson(meta, PushData.class);
            if (pushData.getSmileId() != null) {
                KeyValueStorage storage = RepositoryProvider.provideKeyValueStorage();
                storage.setPushData(pushData);
                EventBus.getDefault().post(pushData);
                if(storage.getPushData() != null) {
                    sendNotification(data.get("message"), pushData);
                }
            } else
                sendNotification(data.get("message"), pushData);
        }
    }

    private void sendNotification(String message, PushData pushData) {
        Log.d(TAG, "sendNotification");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setSmallIcon(R.mipmap.app_icon)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        Bitmap bitmap = null;
        if(pushData.getUserAvatar() != null) {
            try {
                bitmap = Glide.with(this).load(Config.SERVER + pushData.getUserAvatar()).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(bitmap == null)
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ava_placeholder);
        notificationBuilder.setLargeIcon(bitmap);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int id = (int)((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(id, notificationBuilder.build());
    }
}
