package com.opentmn.opentmn.screens.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vk.sdk.api.VKApi;

import сom.opentmn.opentmn.R;

/**
 * Created by kost on 22.01.17.
 */

public class InputDialog extends DialogFragment {

    private static final String EXTRA_IMAGE_KEY = "image";

    private OnDialogButtonClickListener mOnDialogButtonClickListener;
    private EditText mEditText;

    public static InputDialog getInstance(String url) {
        InputDialog dialog = new InputDialog();
        Bundle args = new Bundle();
        args.putString(EXTRA_IMAGE_KEY, url);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_input, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        mEditText = (EditText) view.findViewById(R.id.edit_text);

        Bundle args = getArguments();
        String image = args.getString(EXTRA_IMAGE_KEY);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        Glide.with(getActivity()).load(image).into(imageView);
        Button cancelButton = (Button) view.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        Button confirmButton = (Button) view.findViewById(R.id.button_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnDialogButtonClickListener != null)
                    mOnDialogButtonClickListener.onConfirmClick(InputDialog.this, mEditText.getText().toString());
            }
        });

    }

    public void setOnDialogButtonClickListener(OnDialogButtonClickListener onDialogButtonClickListener) {
        mOnDialogButtonClickListener = onDialogButtonClickListener;
    }

    public interface OnDialogButtonClickListener {
        void onConfirmClick(DialogFragment dialogFragment, String text);
    }
}