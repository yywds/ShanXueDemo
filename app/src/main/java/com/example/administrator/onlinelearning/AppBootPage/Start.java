package com.example.administrator.onlinelearning.AppBootPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.onlinelearning.LoginAndRegister.Guide;
import com.example.administrator.onlinelearning.R;

public class Start extends AppCompatActivity {
    boolean isFirstIn = false;
    private Intent intent;
    private SharedPreferences.Editor edit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firstRun();//1.引导页：判断APP是否是第一次启动
    }

    /**
     * 1.引导页：判断APP是否是第一次启动
     */
    private void firstRun() {
        SharedPreferences sharedPreferences = getSharedPreferences("FirstRun",0);
        Boolean firstRun = sharedPreferences.getBoolean("First",true);
        if (firstRun) {
            sharedPreferences.edit().putBoolean("First",false).commit();
            //如果APP是第一次启动则跳转到BootPage
            startActivity(new Intent(Start.this,VideoBootPage.class));
            finish();//销毁，防止按返回键的时候返回到此页
        }
        else {
            //如果APP不是第一次启动则跳转到Register
            startActivity(new Intent(Start.this,BootPage.class));
            finish();//销毁，防止按返回键的时候返回到此页
        }

    }
}
