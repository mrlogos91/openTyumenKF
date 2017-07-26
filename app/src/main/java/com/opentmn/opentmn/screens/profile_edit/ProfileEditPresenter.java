package com.opentmn.opentmn.screens.profile_edit;

import android.content.Context;
import android.net.Uri;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.utils.FileUtils;
import com.opentmn.opentmn.utils.RxSchedulers;

import rx.Subscription;

/**
 * Created by kost on 24.01.17.
 */

public class ProfileEditPresenter {

    private ProfileEditView mView;
    private MyTyumenRepository mRepository;
    private User mUser;
    private Uri mImageUri;

    public ProfileEditPresenter(ProfileEditView profileEditView) {
        mView = profileEditView;
        mRepository = RepositoryProvider.provideRepository();
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
    }

    public void init() {
        mView.showUser(mUser);
    }

    public void onGenderClick() {
        mView.showGendersDialog(mUser.getGenderId());
    }

    public void onGenderSelect(int index) {
        mUser.setGenderId(index);
        mView.showUser(mUser);
    }

    public void onSaveClick(Context context, String name, String email) {
        Subscription subscription = mRepository.updateProfile(mUser.getToken(), null, null, mUser.getGenderId() > 0 ? String.valueOf(mUser.getGenderId()) : null, name, email, FileUtils.getPath(context, mImageUri))
                .compose(RxSchedulers.async())
                .doOnSubscribe(mView::showProgress)
                .doAfterTerminate(mView::hideProgress)
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        mUser.setName(name);
                        mUser.setEmail(email);
                        mView.showDialog("ИНФОРМАЦИЯ", "Сохранено!");
                    } else {
                        mView.showApiResponseError(response, () -> onSaveClick(context, name, email));
                    }
                }, throwable -> {
                    mView.showNetworkError(() -> onSaveClick(context, name, email));
                });
        mView.addSubscription(subscription);

    }

    public void onPassChangeClick(String oldPass, String newPass, String confirmPass) {
        if(oldPass.length() == 0) {
            mView.showDialog("ОШИБКА", "Введите Текущий пароль!");
            return;
        }
        if(newPass.length() < 8) {
            mView.showDialog("ОШИБКА", "Пароль недостаточной длины\n(не может быть меньше 8 символов)");
            return;
        }
        if(!newPass.equals(confirmPass)) {
            mView.showDialog("ОШИБКА", "Новый пароль и Подтверждение не совпадают!");
            return;
        }
        Subscription subscription = mRepository.updateProfile(mUser.getToken(), oldPass, newPass, null, null, null, null)
                .compose(RxSchedulers.async())
                .doOnSubscribe(mView::showProgress)
                .doAfterTerminate(mView::hideProgress)
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        mView.clearFields();
                        mView.showDialog("ИНФОРМАЦИЯ", "Сохранено!");
                    } else {
                        mView.showApiResponseError(response, () -> onPassChangeClick(oldPass, newPass, confirmPass));
                    }
                }, throwable -> {
                    mView.showNetworkError(() -> onPassChangeClick(oldPass, newPass, confirmPass));
                });
        mView.addSubscription(subscription);

    }

    public void onImagePick(Uri uri) {
        mImageUri = uri;
        mView.showUserAvatar(uri);
    }

}
