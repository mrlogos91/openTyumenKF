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
import android.widget.TextView;

import сom.opentmn.opentmn.R;

/**
 * Created by kost on 15.01.17.
 */

public class ErrorDialog extends DialogFragment {

    private final static String BUNDLE_TITLE_KEY = "title";
    private final static String BUNDLE_TEXT_KEY = "text";

    private ErrorDialog.OnDialogButtonClickListener mOnDialogButtonClickListener;

    public static ErrorDialog getInstance(String title, String text) {
        ErrorDialog dialog = new ErrorDialog();
        Bundle args = new Bundle();
        args.putString(BUNDLE_TITLE_KEY, title);
        args.putString(BUNDLE_TEXT_KEY, text);
        dialog.setArguments(args);
        return dialog;
    }

    public static ErrorDialog getInstance(String text) {
        ErrorDialog dialog = new ErrorDialog();
        Bundle args = new Bundle();
        args.putString(BUNDLE_TEXT_KEY, text);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_error, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        Bundle args = getArguments();
        String title = args.getString(BUNDLE_TITLE_KEY);
        String text = args.getString(BUNDLE_TEXT_KEY);
        if(title != null) {
            TextView titleTextView = (TextView) view.findViewById(R.id.title_text_view);
            titleTextView.setText(title);
        }
        TextView messageTextView = (TextView) view.findViewById(R.id.subtitle_text_view);
        messageTextView.setText(text);


        Button cancelButton = (Button) view.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        Button repeatButton = (Button) view.findViewById(R.id.button_repeat);
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(mOnDialogButtonClickListener != null)
                    mOnDialogButtonClickListener.onRepeatClick();
            }
        });

    }

    public void setOnDialogButtonClickListener(ErrorDialog.OnDialogButtonClickListener onDialogButtonClickListener) {
        mOnDialogButtonClickListener = onDialogButtonClickListener;
    }

    public interface OnDialogButtonClickListener {
        void onRepeatClick();
    }
}
