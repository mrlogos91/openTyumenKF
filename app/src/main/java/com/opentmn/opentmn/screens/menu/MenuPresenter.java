package com.opentmn.opentmn.screens.menu;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.keyvalue.KeyValueStorage;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.utils.RxSchedulers;

import rx.Subscription;

/**
 * Created by kost on 06.03.17.
 */

public class MenuPresenter {

    private MenuView mView;
    private MyTyumenRepository mRepository;
    private User mUser;

    public MenuPresenter(MenuView view) {
        mView = view;
        mRepository = RepositoryProvider.provideRepository();
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
    }

    public void init() {
        mView.showUser(mUser);
    }

    public void onStart() {
        loadUser();
    }

    private void loadUser() {
        String token = mUser.getToken();
        Subscription subscription = mRepository.profile(token)
                .compose(RxSchedulers.async())
                .subscribe(data -> {
                    if (data.isSuccess()) {
                        User user = data.getData();
                        KeyValueStorage keyValueStorage = RepositoryProvider.provideKeyValueStorage();
                        user.setToken(mUser.getToken());
                        keyValueStorage.setUser(user);
                        mUser = user;
                        mView.showUser(mUser);
                    }

                }, throwable -> {
                });
        mView.addSubscription(subscription);
    }
}
