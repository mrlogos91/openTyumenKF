package com.opentmn.opentmn.network.retrofit;

import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import com.opentmn.opentmn.Config;

/**
 * Created by Alexey Antonchik on 11.12.16.
 */

public final class ApiFactory {

    private static OkHttpClient sClient;
    private static volatile MyTyumenService sService;

    private ApiFactory() {
    }

    private static final HttpLoggingInterceptor LOGGING_INTERCEPTOR = new HttpLoggingInterceptor();

    static {
        LOGGING_INTERCEPTOR.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @NonNull
    public static MyTyumenService getService() {
        MyTyumenService service = sService;
        if (service == null) {
            synchronized (ApiFactory.class) {
                service = sService;
                if (service == null) {
                    service = sService = buildRetrofit().create(MyTyumenService.class);
                }
            }
        }
        return service;
    }

    @NonNull
    private static Retrofit buildRetrofit() {

        return new Retrofit.Builder()
                .baseUrl(Config.API_ENDPOINT)
                .client(getClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @NonNull
    private static OkHttpClient getClient() {
        OkHttpClient client = sClient;
        if (client == null) {
            synchronized (ApiFactory.class) {
                client = sClient;
                if (client == null) {
                    client = sClient = buildClient();
                }
            }
        }
        return client;
    }

    @NonNull
    private static OkHttpClient buildClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor())
             //   .addInterceptor(new AuthInterceptor())
                .addInterceptor(LOGGING_INTERCEPTOR)
                .build();
    }


}
