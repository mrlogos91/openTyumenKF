package com.opentmn.opentmn.screens.page;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.opentmn.opentmn.model.Page;
import com.opentmn.opentmn.screens.base.BaseFragment;

import com.opentmn.opentmn.R;

/**
 * Created by kost on 12.01.17.
 */

public class PageFragment extends BaseFragment implements PageView {

    public static PageFragment getInstance(String alias) {
        PageFragment pageFragment = new PageFragment();
        Bundle args = new Bundle();
        args.putString(PageActivity.EXTRA_ALIAS_KEY, alias);
        pageFragment.setArguments(args);
        return pageFragment;
    }

    private WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mWebView = (WebView) view.findViewById(R.id.web_view);
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        Bundle args = getArguments();
        String alias = args.getString(PageActivity.EXTRA_ALIAS_KEY);
        PagePresenter pagePresenter = new PagePresenter(this, alias);
        pagePresenter.init();
    }

    @Override
    public void showPage(Page page) {
        mWebView.loadDataWithBaseURL(null, page.getBody(), "text/html", "UTF-8", null);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
