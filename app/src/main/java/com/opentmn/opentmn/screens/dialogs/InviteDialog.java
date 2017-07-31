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
import com.opentmn.opentmn.R;
import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.User;

/**
 * Created by kost on 25.01.17.
 */

public class InviteDialog extends DialogFragment {


    private OnDialogButtonClickListener mOnDialogButtonClickListener;
    private EditText mEditText;

    public static InviteDialog getInstance() {
        InviteDialog dialog = new InviteDialog();
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_invite, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        mEditText = (EditText) view.findViewById(R.id.edit_text);

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
                    mOnDialogButtonClickListener.onConfirmClick(InviteDialog.this, mEditText.getText().toString());
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
