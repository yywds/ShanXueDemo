package com.example.administrator.onlinelearning.LoginAndRegister;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.administrator.onlinelearning.R;


public class ToLogin extends AppCompatActivity {

    private ImageView mLv;
    private TextView mTvSid;
    private Button mBtnLogin;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏，这句话要写在onCreate前面，否则报错
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tologin);
        mLv = (ImageView) findViewById(R.id.lv);
        mTvSid = (TextView) findViewById(R.id.tv_sid);
        mBtnLogin = (Button) findViewById(R.id.btn_login);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
        initImageView();//初始化图片
        toLogin();//去登录

        //沉浸式
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

    }

    private void  initImageView(){
        //获取手机屏幕宽高
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        int width = displayMetrics.widthPixels;
//        int height = displayMetrics.heightPixels;
//
//        //加载网络图片
//        String url = "https://img-blog.csdnimg.cn/20190221200709254.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzgwMDgx,size_16,color_FFFFFF,t_70";
//        RequestOptions options = new RequestOptions().centerCrop().override(width, height);
//        Glide.with(ToLogin.this).load(url).apply(options).into(mLv);
    }

    private void toLogin(){
        //从注册页中获取存储的sid
        SharedPreferences sharedPreferences = getSharedPreferences("register",Context.MODE_PRIVATE);
        mTvSid.setText("您的账号为："+ sharedPreferences.getString("sid",null));

        //点击现在去登录跳转到登录页
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ToLogin.this,LoginPage.class));
            }
        });

    }
}
