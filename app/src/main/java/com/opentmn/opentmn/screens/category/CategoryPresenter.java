package com.opentmn.opentmn.screens.category;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.Category;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.utils.RxSchedulers;

import java.util.List;

import rx.Subscription;

/**
 * Created by kost on 24.12.16.
 */

public class CategoryPresenter {

    private CategoryView mCategoryView;
    private List<Category> mCategoryList;
    private Game mGame;
    private int mRoundNumber;


    public CategoryPresenter(CategoryView categoryView, Game game, int roundNumber) {
        mCategoryView = categoryView;
        mGame = game;
        mRoundNumber = roundNumber;
    }

    public void init() {
        Subscription subscription = RepositoryProvider.provideRepository().categories(null, RepositoryProvider.provideKeyValueStorage().getUser().getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mCategoryView::showProgress)
                .doOnNext(data -> mCategoryView.hideProgress())
                .doOnError(err -> mCategoryView.hideProgress())
                .subscribe(data -> {
                    if(data.isSuccess()){
                        mCategoryList = data.getData();
                        mCategoryView.showCategoryList(mCategoryList);
                    } else {
                        mCategoryView.showApiResponseError(data, () -> {init();});
                    }

                }, throwable -> {
                    mCategoryView.showNetworkError(() -> {init();});
                });
        mCategoryView.addSubscription(subscription);
    }

    public void onCategoryClick(int index) {
        Category category = mCategoryList.get(index);
        String token = RepositoryProvider.provideKeyValueStorage().getUser().getToken();
        Subscription subscription = RepositoryProvider.provideRepository().createRound(mGame.getId(), category.getId(), token)
                .compose(RxSchedulers.async())
                .doOnSubscribe(mCategoryView::showProgress)
                .doOnNext(data -> mCategoryView.hideProgress())
                .doOnError(err -> mCategoryView.hideProgress())
                .subscribe(data -> {
                    if (data.isSuccess()) {
                        Game game = data.getData();
                        mCategoryView.startGame(game, mRoundNumber);
                    } else {
                        mCategoryView.showApiResponseError(data, () -> onCategoryClick(index));
                    }

                }, throwable -> {
                    mCategoryView.showNetworkError(() -> onCategoryClick(index));
                });
        mCategoryView.addSubscription(subscription);
    }



}
