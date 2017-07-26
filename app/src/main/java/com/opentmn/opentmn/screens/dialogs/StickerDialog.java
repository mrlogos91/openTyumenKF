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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.opentmn.opentmn.Config;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import com.opentmn.opentmn.R;

/**
 * Created by kost on 25.01.17.
 */

public class StickerDialog extends DialogFragment {

    private final static String BUNDLE_NAME_KEY = "name";
    private final static String BUNDLE_AVA_KEY = "ava";
    private final static String BUNDLE_STICKER_KEY = "sticker";

    public static StickerDialog getInstance(String name, String ava, int stickerId) {
        StickerDialog dialog = new StickerDialog();
        Bundle args = new Bundle();
        args.putString(BUNDLE_NAME_KEY, name);
        args.putString(BUNDLE_AVA_KEY, ava);
        args.putInt(BUNDLE_STICKER_KEY, stickerId);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_smile, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        Bundle args = getArguments();
        String name = args.getString(BUNDLE_NAME_KEY);
        String ava = args.getString(BUNDLE_AVA_KEY);
        int stickerId = args.getInt(BUNDLE_STICKER_KEY);

        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        setup(name, ava, stickerId);
    }

    private void setup(String name, String ava, int stickerId) {
        TextView nameTextView = (TextView) getView().findViewById(R.id.name_text_view);
        ImageView avaImageView = (ImageView) getView().findViewById(R.id.ava_image_view);
        ImageView stickerImageView = (ImageView) getView().findViewById(R.id.sticker_image_view);

        nameTextView.setText(name);
        if(ava != null)
            Glide.with(getActivity()).load(Config.SERVER + ava).bitmapTransform(new CropCircleTransformation(getActivity())).placeholder(R.mipmap.ava_placeholder_b).into(avaImageView);
        else
            avaImageView.setImageResource(R.mipmap.ava_placeholder_b);
        if(stickerId == 1)
            stickerImageView.setImageResource(R.mipmap.sticker_1);
        else if(stickerId == 2)
            stickerImageView.setImageResource(R.mipmap.sticker_2);
        else
            stickerImageView.setImageResource(R.mipmap.sticker_3);
    }
}
