package com.opentmn.opentmn.screens.splash;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.github.javiersantos.appupdater.AppUpdater;
import com.opentmn.opentmn.Config;
import com.opentmn.opentmn.MainActivity;
import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.screens.banner.BannerActivity;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.screens.dialogs.ConfirmDialog;
import com.opentmn.opentmn.screens.login.LoginActivity;
import com.opentmn.opentmn.screens.menu.MenuActivity;

import com.opentmn.opentmn.R;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Alexey Antonchik on 21.12.16.
 */

public class SplashActivity extends BaseActivity {


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    try {
      checkUpdate();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    finish();
  }
  String latestVersion = "";
  String currentVersion ="";
  private void checkUpdate() throws ExecutionException, InterruptedException {
    currentVersion = getCurrentVersion();
    latestVersion = new GetLatestVersion().execute().get();
    if (latestVersion != null) {
      String[] latestVersionCode = latestVersion.split("\\.");
      String[] currentVersionCode = currentVersion.split("\\.");
      if ((Integer.parseInt(currentVersionCode[0]) < Integer.parseInt(latestVersionCode[0]))||
          (Integer.parseInt(currentVersionCode[1]) < Integer.parseInt(latestVersionCode[1]) && (Integer.parseInt(currentVersionCode[0]) == Integer.parseInt(latestVersionCode[0])))||
          (Integer.parseInt(currentVersionCode[2]) < Integer.parseInt(latestVersionCode[2]) && (Integer.parseInt(currentVersionCode[1]) == Integer.parseInt(latestVersionCode[1]))))
      {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.opentmn.opentmn"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("Вышло новое обновление, с новыми возможностями. Скорее обнови!")
            .setSmallIcon(R.drawable.app_status_icon)
            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int id = (int)((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(id, notificationBuilder.build());
      }
    }
    if (RepositoryProvider.provideKeyValueStorage().getUser() != null) {
      MenuActivity.startActivity(this);
    } else {
      LoginActivity.start(this);
    }

    startActivity(new Intent(this, BannerActivity.class));
  }

  public String getCurrentVersion() {
    PackageManager pm = this.getPackageManager();
    PackageInfo pInfo = null;

    try {
      pInfo = pm.getPackageInfo(this.getPackageName(), 0);

    } catch (PackageManager.NameNotFoundException e1) {
      e1.printStackTrace();
    }
    String currentVersion = pInfo.versionName;

    return currentVersion;
  }

  private class GetLatestVersion extends AsyncTask<String, String, String> {
    String latestVersion;

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
      super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... params) {
      try {
        //It retrieves the latest version by scraping the content of current version from play store at runtime
        String urlOfAppFromPlayStore = "https://play.google.com/store/apps/details?id=com.opentmn.opentmn";
        Document doc = Jsoup.connect(urlOfAppFromPlayStore).get();
        latestVersion = doc.getElementsByAttributeValue("itemprop","softwareVersion").first().text();

      }catch (Exception e){
        e.printStackTrace();

      }

      return latestVersion;
    }
  }
}
