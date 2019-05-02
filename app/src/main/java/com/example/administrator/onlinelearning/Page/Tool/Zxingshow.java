package com.example.administrator.onlinelearning.Page.Tool;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.onlinelearning.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Zxingshow extends AppCompatActivity {
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.tv_neirong)
    TextView tvNeirong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zxingshow);
        ButterKnife.bind(this);
        tvNeirong.setText("扫描到以下内容"+"\n"+"（长按可复制）");
        //结束content传过来的内容
        String name = getIntent().getStringExtra("name");
        content.setText(name);
        initWindow();
    }

    private void initWindow() {
        //沉浸式
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //改变虚拟键盘颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#000000"));
        }
    }
}
