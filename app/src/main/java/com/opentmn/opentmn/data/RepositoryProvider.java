package com.opentmn.opentmn.data;

import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.opentmn.opentmn.data.keyvalue.HawkKeyValueStorage;
import com.opentmn.opentmn.data.keyvalue.KeyValueStorage;
import com.opentmn.opentmn.data.repository.DefaultRepository;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;

/**
 * Created by Alexey Antonchik on 12.12.16.
 */

public class RepositoryProvider {

    private static MyTyumenRepository sMyRepository;
    private static KeyValueStorage sKeyValueStorage;

    private RepositoryProvider() {
    }

    @NonNull
    public static MyTyumenRepository provideRepository() {
        if (sMyRepository == null) {
            sMyRepository = new DefaultRepository();
        }
        return sMyRepository;
    }

    public static void setMyRepository(@NonNull MyTyumenRepository myRepository) {
        sMyRepository = myRepository;
    }

    public static void setKeyValueStorage(@NonNull KeyValueStorage keyValueStorage) {
        sKeyValueStorage = keyValueStorage;
    }

    @MainThread
    public static void init(@Nullable Context context) {
        sMyRepository = new DefaultRepository();
        sKeyValueStorage = new HawkKeyValueStorage(context);
    }

    @NonNull
    public static KeyValueStorage provideKeyValueStorage() {
        return sKeyValueStorage;
    }

    public static void setKeyValueRepository(@NonNull KeyValueStorage keyValueStorage) {
        sKeyValueStorage = keyValueStorage;
    }

}
