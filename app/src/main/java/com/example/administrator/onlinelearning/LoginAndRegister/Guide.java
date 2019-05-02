package com.example.administrator.onlinelearning.LoginAndRegister;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.administrator.onlinelearning.CommonAndPlugin.Common;
import com.example.administrator.onlinelearning.CommonAndPlugin.DaoMaster;
import com.example.administrator.onlinelearning.CommonAndPlugin.DaoSession;
import com.example.administrator.onlinelearning.CommonAndPlugin.StudentDao;
import com.example.administrator.onlinelearning.Model.Student;
import com.example.administrator.onlinelearning.R;
import com.kongzue.dialog.v2.CustomDialog;
import com.kongzue.dialog.v2.MessageDialog;
import com.kongzue.dialog.v2.TipDialog;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import static com.kongzue.dialog.v2.Notification.TYPE_NORMAL;

public class Guide extends AppCompatActivity {
    private ImageView imageView;
    private Button btnRegister;
    private Button btnLogin;
    private CustomDialog customDialog;
    private int notifactionType = TYPE_NORMAL;
    StudentDao studentDao;
    DaoMaster daoMaster;
    DaoSession daoSession;
    Common common;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);

        imageView = findViewById(R.id.bootView);
        btnLogin = findViewById(R.id.login);
        btnRegister = findViewById(R.id.register);
        //沉浸式
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        common.diaLog();
        perMission();//权限
        initView();//初始化
        toActivity();//跳转
        initGreenDao();//初始化GreenDao

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perMissionSMS();
            }
        });

    }

//    private void diaLog() {
////        DialogSettings.use_blur = false; //不开启模糊
////        DialogSettings.blur_alpha = 255;
////        DialogSettings.tip_theme = THEME_LIGHT;         //设置提示框主题为亮色主题
////        DialogSettings.dialog_theme = THEME_DARK;       //设置对话框主题为暗色主题
////          DialogSettings.style = STYLE_IOS;  //IOS风格
//        Notification.show(Guide.this, 0, getString(R.string.app_name), "这是一条消息", Notification.SHOW_TIME_LONG, notifactionType);
//
//
//        //自定义对话框
////        btnRegister.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                customDialog = CustomDialog.show(Guide.this, R.layout.layout_custom_dialog, new CustomDialog.BindView() {
////                    @Override
////                    public void onBind(View rootView) {
////                        ImageView btnOk = rootView.findViewById(R.id.btn_ok);
////
////                        btnOk.setOnClickListener(new View.OnClickListener() {
////                            @Override
////                            public void onClick(View v) {
////                                customDialog.doDismiss();
////                            }
////                        });
////                    }
////                });
////            }
////        });
//
//    }

    //点击按钮进行跳转
    private void toActivity() {
        //跳转到登录页面
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Guide.this, LoginPage.class));
            }
        });

        //跳转到注册页面
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Guide.this, RegisterPage.class));
            }
        });
    }

    //初始化
    private void initView() {
//        //获取手机屏幕宽高
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        int width = displayMetrics.widthPixels;
//        int height = displayMetrics.heightPixels;
//
//        //加载网络图片
//        String url = "https://img-blog.csdnimg.cn/20190126234536462.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzgwMDgx,size_16,color_FFFFFF,t_70";
//        RequestOptions options = new RequestOptions().centerCrop().override(width, height);
//        Glide.with(Guide.this).load(url).apply(options).into(imageView);

        //设置向左移入动画效果
        ObjectAnimator translationleftX = new ObjectAnimator().ofFloat(btnLogin, "translationX", -600f, 0);//向左移入
        ObjectAnimator translationleftY = new ObjectAnimator().ofFloat(btnLogin, "translationY", 0, 0);//这里y轴方向的可不用写
        AnimatorSet animatorSetleft = new AnimatorSet();  //组合动画
        animatorSetleft.playTogether(translationleftX, translationleftY); //设置动画
        animatorSetleft.setDuration(1000);  //设置动画时间
        animatorSetleft.start(); //启动

        //设置向右移入动画效果
        ObjectAnimator translationrightX = new ObjectAnimator().ofFloat(btnRegister, "translationX", 600f, 0);//向右移入
        ObjectAnimator translationrightY = new ObjectAnimator().ofFloat(btnRegister, "translationY", 0, 0);//这里y轴方向的可不用写
        AnimatorSet animatorSetright = new AnimatorSet();  //组合动画
        animatorSetright.playTogether(translationrightX, translationrightY); //设置动画
        animatorSetright.setDuration(1000);  //设置动画时间
        animatorSetright.start(); //启动

    }

    //开启权限
    private void perMission(){
        AndPermission.with(this)
                .permission(Permission.WRITE_EXTERNAL_STORAGE,
                            Permission.RECORD_AUDIO,
                            Permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //申请成功后的动作
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        // 权限申请失败回调。可提示
                        MessageDialog.show(Guide.this, "温馨提示", "请允许权限后再进行操作哦！", "好的，臣妾知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    }
                }).start();
    }

    //开启权限
    public void perMissionSMS() {
        AndPermission.with(this)
                .permission(Permission.SEND_SMS)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        sendMessage();//向手机发送验证码
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        // 权限申请失败回调。可提示
                        MessageDialog.show(Guide.this, "温馨提示", "请允许权限后再进行操作哦！", "好的，臣妾知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    }
                })
                .start();
    }

    //向手机发送验证码
    public void sendMessage() {
        cn.smssdk.gui.RegisterPage registerPage = new cn.smssdk.gui.RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 处理成功的结果
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country"); // 国家代码，如“86”
                    String phone = (String) phoneMap.get("phone"); // 手机号码，如“13800138000”
                    common.SubmitUserInfo(country, phone);
                    //查询
                    ArrayList<Student> listphone = (ArrayList<Student>) studentDao.queryBuilder().where(StudentDao.Properties.Sphone.eq(phone)).list();

                    if (listphone.size() > 0) {
                        TipDialog.show(Guide.this, "该手机号已注册", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
                    } else {
                        SharedPreferences sharedPreferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("myphone", phone).apply();
                        startActivity(new Intent(Guide.this, RegisterPage.class));
                    }
                    // TODO 利用国家代码和手机号码进行后续的操作
                } else {
                    // TODO 处理错误的结果
                }
            }
        });
        //将结果显示在界面
        registerPage.show(Guide.this);

    }

    //初始化GreenDao
    public void initGreenDao() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "onlinelearning.db", null);
        daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
        studentDao = daoSession.getStudentDao();
    }

}

