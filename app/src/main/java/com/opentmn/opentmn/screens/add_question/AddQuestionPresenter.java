package com.opentmn.opentmn.screens.add_question;

import android.content.Context;
import android.net.Uri;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.data.repository.MyTyumenRepository;
import com.opentmn.opentmn.model.Category;
import com.opentmn.opentmn.model.Question;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.utils.FileUtils;
import com.opentmn.opentmn.utils.RxSchedulers;

import java.util.List;

import rx.Subscription;
import com.opentmn.opentmn.R;

/**
 * Created by kost on 22.01.17.
 */

public class AddQuestionPresenter {

    private AddQuestionView mView;
    private Context mContext;
    private MyTyumenRepository mRepository;
    private Uri mImageUri;
    private User mUser;
    private List<Category> mCategoryList;
    private Category mCategory;

    public AddQuestionPresenter(AddQuestionView view, Context context) {
        mView = view;
        mContext = context;
        mRepository = RepositoryProvider.provideRepository();
        mUser = RepositoryProvider.provideKeyValueStorage().getUser();
    }

    public void init() {
        mView.showAuthorName(mUser.getName());
    }

    public void onImagePick(Uri uri) {
        mImageUri = uri;
        mView.showImage(mImageUri);
    }

    public void onImageRemove() {
        mImageUri = null;
        mView.hideImage();
    }

    public void onCategoryClick() {
        if(mCategoryList != null) {
            mView.showCategoriesDialog(mCategoryList);
            return;
        }
        Subscription subscription = mRepository.categories(1, mUser.getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mView::showProgress)
                .doAfterTerminate(mView::hideProgress)
                .subscribe(data -> {
                    if(data.isSuccess()){
                        mCategoryList = data.getData();
                        mView.showCategoriesDialog(mCategoryList);
                    } else {
                        mView.showApiResponseError(data, () -> {onCategoryClick();});
                    }

                }, throwable -> {
                    mView.showNetworkError(() -> {onCategoryClick();});
                });
        mView.addSubscription(subscription);
    }

    public void onCategorySelect(int index) {
        mCategory = mCategoryList.get(index);
        mView.showCategory(mCategory);
    }

    public void onSendClick(Question question, boolean checked) {
        if(mCategory == null) {
            mView.showErrorDialog(mContext.getString(R.string.required_field_empty));
            return;
        }
        if(question.getName().isEmpty()) {
            mView.showErrorDialog(mContext.getString(R.string.required_field_empty));
            return;
        }
        if(question.getSource().isEmpty()) {
            mView.showErrorDialog(mContext.getString(R.string.required_field_empty));
            return;
        }
        if(question.getAuthor().isEmpty()) {
            mView.showErrorDialog(mContext.getString(R.string.required_field_empty));
            return;
        }
        if(question.getRef().isEmpty()) {
            mView.showErrorDialog(mContext.getString(R.string.required_field_empty));
            return;
        }
        for(String answer: question.getAnswerArr()) {
            if(answer.isEmpty()) {
                mView.showErrorDialog(mContext.getString(R.string.required_field_empty));
                return;
            }
        }
        if(!checked) {
            mView.showErrorDialog(mContext.getString(R.string.rules_accept_needed));
            return;
        }
        Subscription subscription = mRepository.sendQuestion(question.getName(),
                mCategory.getId(),
                question.getAnswerArr()[0],
                question.getAnswerArr()[1],
                question.getAnswerArr()[2],
                question.getAnswerArr()[3],
                question.getSource(),
                question.getAuthor(),
                question.getRef(),
                question.getRights().length() > 0 ? question.getRights() : null,
                FileUtils.getPath(mContext, mImageUri),
                mUser.getToken())
                .compose(RxSchedulers.async())
                .doOnSubscribe(mView::showProgress)
                .doAfterTerminate(mView::hideProgress)
                .subscribe(response -> {
                    if(response.isSuccess()) {
                        mView.clearFields();
                        mCategory = null;
                        mImageUri = null;
                        mView.showAuthorName(mUser.getName());
                        mView.showSuccessDialog();
                    } else {
                        mView.showApiResponseError(response, () -> onSendClick(question, checked));
                    }
                }, throwable -> {
                    mView.showNetworkError(() -> onSendClick(question, checked));
                });
        mView.addSubscription(subscription);


    }
}
