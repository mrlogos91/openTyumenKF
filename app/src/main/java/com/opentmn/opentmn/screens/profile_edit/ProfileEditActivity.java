package com.opentmn.opentmn.screens.profile_edit;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.opentmn.opentmn.Config;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.screens.base.BaseActivity;
import com.opentmn.opentmn.screens.dialogs.MessageDialog;
import com.opentmn.opentmn.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import сom.opentmn.opentmn.R;

/**
 * Created by kost on 24.01.17.
 */

public class ProfileEditActivity extends BaseActivity implements ProfileEditView {

    private static final int ATTACH_PHOTO_REQUEST_CODE = 0;
    private final static int READ_FILES_REQUEST = 1;

    public static void start(int reqCode, BaseActivity context) {
        Intent intent = new Intent(context, ProfileEditActivity.class);
        context.startActivityForResult(intent, reqCode);
    }

    @BindView(R.id.ava_image_view)
    ImageView mAvaImageView;
    @BindView(R.id.name_edit_text)
    EditText mNameEditText;
    @BindView(R.id.email_edit_text)
    EditText mEmailEditText;
    @BindView(R.id.gender_text_view)
    TextView mGenderTextView;

    @BindView(R.id.pass_current_edit_text)
    EditText mOldPassEditText;
    @BindView(R.id.pass_new_edit_text)
    EditText mNewPassEditText;
    @BindView(R.id.pass_confirm_edit_text)
    EditText mConfirmPassEditText;

    private ProfileEditPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationImageRes(Toolbar.NAVIGATION_BACK_ICON);
        toolbar.setOnNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mPresenter = new ProfileEditPresenter(this);
        mPresenter.init();
    }

    @OnClick(R.id.gender_view)
    void onGenderClick() {
        mPresenter.onGenderClick();
    }

    @OnClick(R.id.save_button)
    void onSaveClick() {
        mPresenter.onSaveClick(this, mNameEditText.getText().toString(), mEmailEditText.getText().toString());
    }

    @OnClick(R.id.change_pass_button)
    void onPassChangeClick() {
        mPresenter.onPassChangeClick(mOldPassEditText.getText().toString(), mNewPassEditText.getText().toString(), mConfirmPassEditText.getText().toString());
    }

    @OnClick(R.id.edit_ava_button)
    void onEditAvaClick() {
        if(hasFilesPermission()) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Выбрать фото"), ATTACH_PHOTO_REQUEST_CODE);
        } else {
            requestFilesPermission();
        }
    }

    @Override
    public void showUser(User user) {
        if(user.getAvatar() != null)
            Glide.with(this).load(Config.SERVER + user.getAvatar()).bitmapTransform(new CropCircleTransformation(this)).placeholder(R.mipmap.ava_placeholder).into(mAvaImageView);
        else
            mAvaImageView.setImageResource(R.mipmap.ava_placeholder);
        mNameEditText.setText(user.getName());
        mEmailEditText.setText(user.getEmail());
        if(user.getGenderId() == 1)
            mGenderTextView.setText("Мужской");
        else if(user.getGenderId() == 2)
            mGenderTextView.setText("Женский");
        else
            mGenderTextView.setText("");
    }

    @Override
    public void showUserAvatar(Uri uri) {
        mAvaImageView.setImageURI(uri);
    }

    @Override
    public void showGendersDialog(int selected) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Выберите категорию");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Мужской");
        arrayAdapter.add("Женский");
        builderSingle.setNegativeButton(
                "Отмена",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setSingleChoiceItems(
                arrayAdapter, selected - 1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.onGenderSelect(which + 1);
                        dialog.dismiss();

                    }
                });
        builderSingle.show();
    }

    @Override
    public void showDialog(String title, String message) {
        MessageDialog messageDialog = MessageDialog.getInstance(title, message);
        messageDialog.show(getSupportFragmentManager(), "message");
    }

    @Override
    public void clearFields() {
        mOldPassEditText.setText("");
        mNewPassEditText.setText("");
        mConfirmPassEditText.setText("");
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
                mPresenter.onImagePick(uri);
            }
        }
    }

    public boolean hasFilesPermission() {

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 23) {

            int read = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            int write = ContextCompat.checkSelfPermission(this,
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

    @TargetApi(Build.VERSION_CODES.M)
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
                    onEditAvaClick();
                }
                return;

            }
        }
    }
}
