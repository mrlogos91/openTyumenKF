package com.opentmn.opentmn.screens.history;

import android.util.Log;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.network.model.ApiResponseModel;
import com.opentmn.opentmn.utils.RxSchedulers;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;

/**
 * Created by kost on 21.01.17.
 */

public class HistoryPresenter {

    private final static int PER_PAGE = 20;

    private HistoryView mView;
    private MyTyumenRepository mRepository;
    private int mCurrentPage = 0;
    private boolean mListEnded;
    private User mUser;
    private List<Game> mGames;
    private int mSelectedTab;

    public HistoryPresenter(HistoryView view) {
        mView = view;
        mRepository = RepositoryProvider.provideRepository();
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
        mSelectedTab = -1;
    }

    public void init() {
        Log.d("HistoryPresenter", "init");
        loadMore();
    }

    public void loadMore() {
        Log.d("HistoryPresenter", "loadMore");
        if(mListEnded)
            return;
        int page = mCurrentPage + 1;
        Observable<ApiResponseModel<List<Game>>> observable = mRepository.gamesHistory(page, PER_PAGE, mSelectedTab == 0 ? 1 : null, mSelectedTab == 2 ? 1 : null, mSelectedTab == 1 ? 1 : null, mUser.getToken())
                .compose(RxSchedulers.async());
        if(page == 1)
            observable = observable
                    .doOnSubscribe(mView::showProgress)
                    .doAfterTerminate(mView::hideProgress);
        Subscription subscription = observable
                .subscribe(response -> {
                    if (response.isSuccess()) {
                        List<Game> games = response.getData();
                        if (games.size() > 0) {
                            mCurrentPage = page;
                            if (page == 1)
                                mGames = games;
                            else
                                mGames.addAll(games);
                            mView.showGames(mGames);
                        } else {
                            mListEnded = true;
                        }
                    } else {
                        mView.showApiResponseError(response, () -> loadMore());
                    }
                }, throwable -> {
                    mView.showNetworkError(() -> loadMore());
                });
        mView.addSubscription(subscription);
    }

    public void tabSelected(int selectedTab) {
        mGames = new ArrayList<>();
        mView.showGames(mGames);
        mListEnded = false;
        mCurrentPage = 0;
        mSelectedTab = selectedTab;
        loadMore();
    }
}
