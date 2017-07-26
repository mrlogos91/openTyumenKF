package com.opentmn.opentmn.screens.page;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.Page;
import com.opentmn.opentmn.utils.RxSchedulers;

import rx.Subscription;

/**
 * Created by kost on 12.01.17.
 */

public class PagePresenter {

    private PageView mView;
    private String mAlias;
    private MyTyumenRepository mRepository;

    public PagePresenter(PageView pageView, String alias) {
        mView = pageView;
        mAlias = alias;
        mRepository = RepositoryProvider.provideRepository();
    }

    public void init() {
        Subscription subscription = mRepository.page(mAlias)
                .compose(RxSchedulers.async())
                .doOnSubscribe(mView::showProgress)
                .doAfterTerminate(mView::hideProgress)
                .subscribe(data -> {
                    if(data.isSuccess()){
                        Page page = data.getData();
                        mView.showPage(page);
                    } else {
                        mView.showApiResponseError(data, null);
                    }

                }, throwable -> {
                    mView.showNetworkError(null);
                });
        mView.addSubscription(subscription);
    }
}
