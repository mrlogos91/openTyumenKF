package com.opentmn.opentmn.screens.banner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;

import com.opentmn.opentmn.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BannerActivity extends AppCompatActivity {

    @BindView(R.id.banner_btn)
    AppCompatImageButton bannerBtn;

    @BindView(R.id.banner_view)
    View bannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        ButterKnife.bind(this);

        bannerBtn.setOnClickListener(view -> finish());
        bannerView.setOnClickListener(view -> showBannerAction());
    }

    private void showBannerAction() {
        String url = "https://myhistorypark.ru";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

}
