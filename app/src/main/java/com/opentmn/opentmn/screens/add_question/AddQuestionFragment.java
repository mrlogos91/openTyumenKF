package com.opentmn.opentmn.screens.add_question;

import android.Manifest;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;

import com.opentmn.opentmn.model.Category;
import com.opentmn.opentmn.model.Question;
import com.opentmn.opentmn.screens.base.BaseFragment;
import com.opentmn.opentmn.screens.dialogs.MessageDialog;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import сom.opentmn.opentmn.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by kost on 08.01.17.
 */

public class AddQuestionFragment extends BaseFragment implements AddQuestionView {

    private static final int ATTACH_PHOTO_REQUEST_CODE = 0;
    private final static int READ_FILES_REQUEST = 1;

    @BindView(R.id.scroll_view)
    ScrollView mScrollView;
    @BindView(R.id.category_view)
    View mCategoryView;
    @BindView(R.id.category_text_view)
    TextView mCategoryTextView;
    @BindView(R.id.add_photo_button)
    Button mAddPhotoButton;
    @BindView(R.id.question_text_view)
    EditText mQuestionEditText;
    @BindView(R.id.source_edit_text)
    EditText mSourceEditText;
    @BindView(R.id.author_edit_text)
    EditText mAuthorEditText;
    @BindViews({R.id.answer_1_edit_text, R.id.answer_2_edit_text, R.id.answer_3_edit_text, R.id.answer_4_edit_text})
    EditText[] mAnswerEditTextArr;
    @BindView(R.id.ref_edit_text)
    EditText mRefEditText;
    @BindView(R.id.check_box)
    CheckBox mCheckBox;

    @BindView(R.id.add_photo_view)
    View mAddPhotoView;
    @BindView(R.id.photo_image_view)
    ImageView mPhotoImageView;
    @BindView(R.id.rights_edit_text)
    EditText mRightsEditText;

    @BindView(R.id.quest_count_text_view)
    TextView mQuestCountTextView;
    @BindViews({R.id.answer_1_count_text_view, R.id.answer_2_count_text_view, R.id.answer_3_count_text_view, R.id.answer_4_count_text_view})
    TextView[] mAnswerCountTextViews;
    @BindView(R.id.ref_count_text_view)
    TextView mRefCountTextView;

    private AddQuestionPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setupCountTextViews();

        mPresenter = new AddQuestionPresenter(this, getActivity());
        mPresenter.init();
    }

    private void setupCountTextViews() {
        int questMax = getResources().getInteger(R.integer.quest_max_length);
        int answerMax = getResources().getInteger(R.integer.answer_max_length);
        int refMax = getResources().getInteger(R.integer.ref_max_length);
        mQuestCountTextView.setText(String.valueOf(mQuestionEditText.length()) + "/" + String.valueOf(questMax));
        setupCountTextView(mQuestCountTextView, mQuestionEditText, questMax);
        for(int i = 0; i < 4; i++) {
            mAnswerCountTextViews[i].setText(mAnswerEditTextArr[i].length() + "/" + String.valueOf(answerMax));
            setupCountTextView(mAnswerCountTextViews[i], mAnswerEditTextArr[i], answerMax);
        }
        mRefCountTextView.setText(mRefEditText.length() + "/" + String.valueOf(refMax));
        setupCountTextView(mRefCountTextView, mRefEditText, refMax);
    }

    @OnClick(R.id.send_button)
    void onSendClick() {
        String name = mQuestionEditText.getText().toString();
        String source = mSourceEditText.getText().toString();
        String author = mAuthorEditText.getText().toString();
        String[] answers = new String[]{mAnswerEditTextArr[0].getText().toString(), mAnswerEditTextArr[1].getText().toString(), mAnswerEditTextArr[2].getText().toString(), mAnswerEditTextArr[3].getText().toString()};
        String ref = mRefEditText.getText().toString();
        String rights = mRightsEditText.getText().toString();
        Question question = new Question(name, source, author, answers, ref, rights);
        mPresenter.onSendClick(question, mCheckBox.isChecked());
    }

    @OnClick({R.id.add_photo_button, R.id.edit_photo_button})
    void onAddPhotoClick() {
        if(hasFilesPermission()) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Выбрать фото"), ATTACH_PHOTO_REQUEST_CODE);
        } else {
            requestFilesPermission();
        }
    }

    @OnClick(R.id.remove_photo_button)
    void onRemoveButtonClick() {
        mPresenter.onImageRemove();
    }

    @OnClick(R.id.category_view)
    void onCategoryClick() {
        mPresenter.onCategoryClick();
    }

    @Override
    public void showImage(Uri uri) {
        mAddPhotoButton.setVisibility(View.GONE);
        mPhotoImageView.setImageURI(uri);
        mAddPhotoView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideImage() {
        mAddPhotoView.setVisibility(View.GONE);
        mPhotoImageView.setImageDrawable(null);
        mRightsEditText.setText("");
        mAddPhotoButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAuthorName(String name) {
        mAuthorEditText.setText(name);
    }

    @Override
    public void showCategory(Category category) {
        mCategoryTextView.setText(category.getName());
    }

    @Override
    public void showSuccessDialog() {
        MessageDialog messageDialog = MessageDialog.getInstance(getString(R.string.alert_success_title), getString(R.string.add_question_success));
        messageDialog.show(getActivity().getSupportFragmentManager(), null);
    }

    @Override
    public void showErrorDialog(String error) {
        MessageDialog messageDialog = MessageDialog.getInstance(getString(R.string.alert_info_title), error);
        messageDialog.show(getActivity().getSupportFragmentManager(), null);
    }

    @Override
    public void clearFields() {
        mCategoryTextView.setText("");
        mAddPhotoView.setVisibility(View.GONE);
        mPhotoImageView.setImageDrawable(null);
        mRightsEditText.setText("");
        mAddPhotoButton.setVisibility(View.VISIBLE);
        mQuestionEditText.setText("");
        mSourceEditText.setText("");
        mAuthorEditText.setText("");
        for(EditText editText : mAnswerEditTextArr) {
            editText.setText("");
        }
        mRefEditText.setText("");
        mCheckBox.setChecked(false);
        mScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    public void showCategoriesDialog(List<Category> categoryList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Выберите категорию");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.select_dialog_singlechoice);

        String selected = mCategoryTextView.getText().toString();
        int checked = -1;
        for(int i = 0; i < categoryList.size(); i++) {
            String categoryName = categoryList.get(i).getName();
            if(categoryName.equals(selected))
                checked = i;
            arrayAdapter.add(categoryName);
        }
        builderSingle.setNegativeButton(
                "Отмена",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        if (checked >= 0) {
            builderSingle.setSingleChoiceItems(
                    arrayAdapter, checked,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPresenter.onCategorySelect(which);
                            dialog.dismiss();

                        }
                    });
        } else {
            builderSingle.setAdapter(
                    arrayAdapter,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPresenter.onCategorySelect(which);
                            dialog.dismiss();
                        }
                    });
        }
        builderSingle.show();
    }

    private void setupCountTextView(TextView textView, EditText editText, int max) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textView.setText(String.valueOf(charSequence.length()) + "/" + String.valueOf(max));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ATTACH_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri;
            ClipData clipData;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                clipData = data.getClipData();
                if (clipData != null && clipData.getItemCount() > 1) {
                    uri = clipData.getItemAt(0).getUri();
                } else
                    uri = data.getData();
            } else {
                uri = data.getData();
            }
            if(uri != null) {
                Log.d("AddQuestionFragment", uri.toString());
                mPresenter.onImagePick(uri);
            }
        }
    }

    public boolean hasFilesPermission() {

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 23) {

            int read = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            int write = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }

        } else {
            return true;
        }
    }

    public void requestFilesPermission() {
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= 23) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_FILES_REQUEST);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_FILES_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onAddPhotoClick();
                }
                return;

            }
        }
    }
}
