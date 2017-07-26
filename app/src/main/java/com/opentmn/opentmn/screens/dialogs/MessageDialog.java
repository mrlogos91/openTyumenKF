package com.opentmn.opentmn.screens.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import com.opentmn.opentmn.R;

/**
 * Created by kost on 12.01.17.
 */

public class MessageDialog extends DialogFragment {

    private final static String BUNDLE_TITLE_KEY = "title";
    private final static String BUNDLE_SUBTITLE_KEY = "subtitle";

    private OnDismissListener mOnDismissListener;

    public static MessageDialog getInstance(String title, String subtitle) {
        MessageDialog dialog = new MessageDialog();
        Bundle args = new Bundle();
        args.putString(BUNDLE_TITLE_KEY, title);
        args.putString(BUNDLE_SUBTITLE_KEY, subtitle);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_message, container);
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

        if(subtitle == null || subtitle.length() == 0) {
            subtitleTextView.setVisibility(View.GONE);
        }
        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(mOnDismissListener != null)
            mOnDismissListener.onDismiss();
    }

    public interface OnDismissListener {
        void onDismiss();
    }
}
