package com.opentmn.opentmn.screens.blacklist;

import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by kost on 18.01.17.
 */

public interface BlackListView extends BaseView {

    void showUsers(List<User> users);

    Observable<TextViewTextChangeEvent> getTextChangedObservable();
}
