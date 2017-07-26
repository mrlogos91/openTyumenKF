package com.opentmn.opentmn.screens.invite;

import android.content.Context;
import android.util.Log;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.SocialUser;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.utils.RxSchedulers;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import com.opentmn.opentmn.R;

/**
 * Created by kost on 22.01.17.
 */

public class InvitePresenter {

    private InviteView mView;
    private MyTyumenRepository mRepository;
    private User mUser;
    private List<SocialUser> mUsers;
    private List<SocialUser> mFiltered;

    public InvitePresenter(InviteView view) {
        mView = view;
        mRepository = RepositoryProvider.provideRepository();
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
        mUsers = new ArrayList<>();
        mFiltered = new ArrayList<>();
    }

    public void init() {
        mView.getTextChangedObservable()
                .skip(1)
                .debounce(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(textViewTextChangeEvent -> {
                    String text = textViewTextChangeEvent.text().toString();
                    mFiltered = new ArrayList<SocialUser>();
                    if(text.length() == 0) {
                        mFiltered = mUsers;
                    } else {
                        for (SocialUser user : mUsers) {
                            if (user.getName().toLowerCase().contains(text.toLowerCase())) {
                                mFiltered.add(user);
                            }
                        }
                    }
                    mView.showUsers(mFiltered);
                });
        loadUsers(null, null);
    }

    public void loadUsers(String captchaSid, String captchaKey) {
        User user = RepositoryProvider.provideKeyValueStorage().getUser();
        SocialUser socialUser = RepositoryProvider.provideKeyValueStorage().getSocialUser();
        Subscription subscription = mRepository.socials(user.getToken(), socialUser.getSocialId(), socialUser.getSocialToken(), captchaSid, captchaKey)
                .compose(RxSchedulers.async())
                .doOnSubscribe(mView::showProgress)
                .doAfterTerminate(mView::hideProgress)
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        mUsers = response.getData();
                        mFiltered = mUsers;
                        mView.showUsers(mFiltered);
                    } else if(response.getError() != null && (response.getError().getCode() == 14 || response.getError().getCode() == -14 || response.getError().getCode() == 12) && response.getError().getMeta() != null && response.getError().getMeta().getCaptchaSid() != null && response.getError().getMeta().getCaptchaImg() != null) {
                        mView.showCaptchaDialog(response.getError().getMeta().getCaptchaImg(), response.getError().getMeta().getCaptchaSid());
                    } else {
                        mView.showApiResponseError(response, () -> init());
                    }
                }, throwable -> {
                    mView.showNetworkError(() -> init());
                });
        mView.addSubscription(subscription);
    }

    public void onInviteClick(Context context, SocialUser socialUser) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("user_id", socialUser.getId());
        params.put("message", context.getResources().getString(R.string.social_invite_text));
        params.put("attachment", "photo308416709_456239077");
        VKRequest vkRequest = new VKRequest("messages.send", new VKParameters(params));
        mView.showProgress();
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                mView.hideProgress();
                Log.d("InvitePresenter", "onComplete");
                JSONObject jsonObject = response.json;
                if(jsonObject != null)
                    Log.d("Json", jsonObject.toString());
                mView.showMessageSentDialog();
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                mView.hideProgress();
                Log.d("InvitePresenter", "onError");
                Log.d("InvitePresenter", error.toString());
            }

            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }
        });
    }

    public void onAddFriendClick(User user) {
        Subscription subscription = mRepository.addFriend(user.getId(), mUser.getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mView::showProgress)
                .doAfterTerminate(mView::hideProgress)
                .subscribe(data -> {
                    if(data.isSuccess()){
                        user.setFriend(true);
                        mView.showUsers(mUsers);
                    } else {
                        mView.showApiResponseError(data, () -> onAddFriendClick(user));
                    }

                }, throwable -> {
                    mView.showNetworkError(() -> onAddFriendClick(user));
                });
        mView.addSubscription(subscription);
    }

    public void onDeleteFriendClick(User user) {
        Subscription subscription = mRepository.deleteFriend(user.getId(), mUser.getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mView::showProgress)
                .doAfterTerminate(mView::hideProgress)
                .subscribe(data -> {
                    if(data.isSuccess()){
                        user.setFriend(false);
                        mView.showUsers(mUsers);
                    } else {
                        mView.showApiResponseError(data, () -> onDeleteFriendClick(user));
                    }

                }, throwable -> {
                    mView.showNetworkError(() -> onDeleteFriendClick(user));
                });
        mView.addSubscription(subscription);
    }

    public void onUserClick(User user) {
        mView.startProfile(user);
    }

    public void onPlayClick(User user) {
        Subscription subscription = mRepository.createGameAgainstUser(user.getId(), mUser.getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mView::showProgress)
                .doAfterTerminate(mView::hideProgress)
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        Game game = response.getData();
                        mView.showGameCreatedDialog(game);
                        mView.showUsers(mUsers);
                    } else {
                        mView.showApiResponseError(response, () -> onPlayClick(user));
                    }
                }, throwable -> {
                    mView.showNetworkError(() -> onPlayClick(user));
                });
        mView.addSubscription(subscription);
    }
}
