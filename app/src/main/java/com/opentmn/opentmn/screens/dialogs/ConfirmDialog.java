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

import com.opentmn.opentmn.R;

/**
 * Created by kost on 13.01.17.
 */

public class ConfirmDialog extends DialogFragment {

    private final static String BUNDLE_TITLE_KEY = "title";
    private final static String BUNDLE_SUBTITLE_KEY = "subtitle";

    private OnDialogButtonClickListener mOnDialogButtonClickListener;

    public static ConfirmDialog getInstance(String title, String message) {
        ConfirmDialog dialog = new ConfirmDialog();
        Bundle args = new Bundle();
        args.putString(BUNDLE_TITLE_KEY, title);
        args.putString(BUNDLE_SUBTITLE_KEY, message);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_confirm, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        Bundle args = getArguments();
        String title = args.getString(BUNDLE_TITLE_KEY);
        String subtitle = args.getString(BUNDLE_SUBTITLE_KEY);

        TextView titleTextView = (TextView) view.findViewById(R.id.title_text_view);
        titleTextView.setText(title);
        TextView subtitleTextView = (TextView) view.findViewById(R.id.subtitle_text_view);
        subtitleTextView.setText(subtitle);

        Button cancelButton = (Button) view.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(mOnDialogButtonClickListener != null)
                    mOnDialogButtonClickListener.onCancelClick();
            }
        });
        Button confirmButton = (Button) view.findViewById(R.id.button_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(mOnDialogButtonClickListener != null)
                    mOnDialogButtonClickListener.onConfirmClick();
            }
        });

    }

    public void setOnDialogButtonClickListener(OnDialogButtonClickListener onDialogButtonClickListener) {
        mOnDialogButtonClickListener = onDialogButtonClickListener;
    }

    public interface OnDialogButtonClickListener {
        void onCancelClick();
        void onConfirmClick();
    }
}
