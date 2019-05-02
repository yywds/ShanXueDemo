package com.example.administrator.onlinelearning.CommonAndPlugin;


import com.kongzue.dialog.v2.DialogSettings;

import java.util.Random;

import cn.smssdk.SMSSDK;

import static com.kongzue.dialog.v2.DialogSettings.STYLE_IOS;
import static com.kongzue.dialog.v2.DialogSettings.THEME_LIGHT;

public class Common {

    //对话框设置
    public static void diaLog(){
        DialogSettings.style = STYLE_IOS;  //对话框为IOS风格
        DialogSettings.tip_theme = THEME_LIGHT;  //设置提示框主题为亮色主题
        DialogSettings.use_blur = false; //不开启模糊
    }

    //对话框设置
    public static void diaLogmohu(){
        DialogSettings.style = STYLE_IOS;  //对话框为IOS风格
        DialogSettings.tip_theme = THEME_LIGHT;  //设置提示框主题为亮色主题
        DialogSettings.use_blur = true; //不开启模糊
    }

    //提交用户信息
    public static void SubmitUserInfo(String country, String phone) {
        Random random = new Random(); //产生随机数
        String uid = Math.abs(random.nextInt()) + "";
        String name = "杨勇文";
        SMSSDK.submitUserInfo(uid, name, null, country, phone);
    }




}
