/*
 * Copyright (c) 2019.
 */

package com.zt.simplebanner.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.zt.simplebanner.demo.recycler.RecyclerBannerView;

public class RecyclerBannerDemoActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {

    private RecyclerBannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_recyclerbanner_demo);
        bannerView = findViewById(R.id.recycler_banner_view);
        bannerView.bindData();

        RadioGroup radioGroup = findViewById(R.id.style_radio_group);
        radioGroup.setOnCheckedChangeListener(this);

        CheckBox checkBox = findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (bannerView != null) {
            bannerView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bannerView != null) {
            bannerView.onPause();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.button1:
                bannerView.setStyle(1);
                break;
            case R.id.button2:
                bannerView.setStyle(2);
                break;
            case R.id.button3:
                bannerView.setStyle(3);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            bannerView.startAutoLoop(1000);
        } else {
            bannerView.stopAutoLoop();
        }
    }
}
