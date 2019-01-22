package com.zt.simplebanner.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recyclerview_demo:
                startActivity(new Intent(this, RecyclerBannerDemoActivity.class));
                break;
            case R.id.scrollview_demo:
                startActivity(new Intent(this, ScrollBannerDemoActivity.class));
                break;
        }
    }
}
