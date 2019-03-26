package com.zt.simplebanner.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zt.simplebanner.OnBannerClickListener;
import com.zt.simplebanner.demo.scroll.ScrollBannerView;
import com.zt.simplebanner.scroll.ScrollBannerPointView;

import java.util.ArrayList;
import java.util.List;

public class ScrollBannerDemoActivity extends AppCompatActivity implements OnBannerClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_scrollbanner_demo);

        ScrollBannerView scrollBannerView = findViewById(R.id.scroll_banner_view);
        scrollBannerView.bindData(getData());
        scrollBannerView.setOnBannerClickListener(this);

        ScrollBannerPointView pointsView = findViewById(R.id.scroll_banner_point);

        //必须在scrollBannerView bindData之后设置
        pointsView.setScrollBannerView(scrollBannerView);
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i + "");
        }
        return list;
    }

    @Override
    public void onBannerClick(int position) {
        Toast.makeText(this, "clicked " + position, Toast.LENGTH_SHORT).show();
    }
}
