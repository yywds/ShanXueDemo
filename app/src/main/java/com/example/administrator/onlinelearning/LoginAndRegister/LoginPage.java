package com.example.administrator.onlinelearning.LoginAndRegister;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.example.administrator.onlinelearning.CommonAndPlugin.Common;
import com.example.administrator.onlinelearning.CommonAndPlugin.CustomVideoView;
import com.example.administrator.onlinelearning.CommonAndPlugin.DaoMaster;
import com.example.administrator.onlinelearning.CommonAndPlugin.DaoSession;
import com.example.administrator.onlinelearning.CommonAndPlugin.StudentDao;
import com.example.administrator.onlinelearning.CommonAndPlugin.VideoCache;
import com.example.administrator.onlinelearning.Model.Student;
import com.example.administrator.onlinelearning.Page.Mainpage;
import com.example.administrator.onlinelearning.R;
import com.kongzue.dialog.v2.MessageDialog;
import com.kongzue.dialog.v2.TipDialog;
import com.kongzue.dialog.v2.WaitDialog;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {
    private CustomVideoView videoview;
    private EditText mEtPhone;
    private EditText mEtPwd;
    private TextView mTvRegister;
    private TextView mTvFindPwd;
    private Button mBtnLogin;
    private TextView mTvToregister;
    DaoMaster daoMaster;
    DaoSession daoSession;
    StudentDao studentDao;
    Common common;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mTvRegister = (TextView) findViewById(R.id.tv_register);
        mTvRegister.setOnClickListener(this);
        mTvFindPwd = (TextView) findViewById(R.id.tv_find_pwd);
        mTvFindPwd.setOnClickListener(this);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        mTvToregister = (TextView) findViewById(R.id.tv_toregister);
        mTvToregister.setOnClickListener(this);


        //沉浸式
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        common.diaLog(); //对话框
        initView();  //初始化布局
        initGreenDao(); //GreenDao初始化
        remember();  //记住账号和密码
    }

    //初始化GreenDao
    public void initGreenDao() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "onlinelearning.db", null);
        daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
        studentDao = daoSession.getStudentDao();
    }

    //登录
    private void login() {
        String sid = mEtPhone.getText().toString().trim();
        String spwd = mEtPwd.getText().toString().trim();

        //账号/手机号/密码查询
        ArrayList<Student> listsid = (ArrayList<Student>) studentDao.queryBuilder().where(StudentDao.Properties.Sid.eq(sid)).list();
        ArrayList<Student> listphone = (ArrayList<Student>) studentDao.queryBuilder().where(StudentDao.Properties.Sphone.eq(sid)).list();

        //进行判断
        if (sid.isEmpty() && spwd.isEmpty()) {
            TipDialog.show(this, "信息填写不能为空哦", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
        } else if (sid.isEmpty()) {
            TipDialog.show(this, "账号不能为空", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
        } else if (spwd.isEmpty()) {
            TipDialog.show(this, "密码不能为空", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
        } else if (listsid.size() <= 0 && listphone.size() <= 0) {
            TipDialog.show(this, "账号或密码不正确", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
        } else {
            if (listsid.size() > 0) {
                for (int i = 0; i < listsid.size(); i++) {
                    if (spwd.equals(listsid.get(i).getSpwd())) {
                        //存储登录用户状态
                        SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        sp.edit().putString("sid", sid).apply();

                        WaitDialog.show(LoginPage.this, "登录中...");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(LoginPage.this, Mainpage.class));//跳转到首页
                            }
                        }, 2000);//2秒后跳转
                    } else {
                        TipDialog.show(this, "账号或密码不正确", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
                    }
                }
            } else if (listphone.size() > 0) {
                for (int i = 0; i < listphone.size(); i++) {
                    if (spwd.equals(listphone.get(i).getSpwd())) {
                        //存储登录用户状态
                        SharedPreferences sp = getSharedPreferences("LoginPhone", Context.MODE_PRIVATE);
                        sp.edit().putString("phone", sid).apply();

                        WaitDialog.show(LoginPage.this, "登录中...");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(LoginPage.this, Mainpage.class));//跳转到首页
                            }
                        }, 2000);//2秒后跳转
                    } else {
                        TipDialog.show(this, "账号或密码不正确", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
                    }
                }
            }
        }
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                findPwd();//找回密码
                break;
            case R.id.tv_toregister:
                toRegister();//注册
                break;
            case R.id.tv_find_pwd:
                changLogin();//换个方式登录
                break;
            case R.id.btn_login:
                login();  //登录
                break;
        }
    }

    //新用户注册
    private void toRegister() {
        perMissionSMSRegister();
    }

    //  开启权限(找回密码)
    public void perMissionSMSFindPwd() {
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
                        MessageDialog.show(LoginPage.this, "温馨提示", "请允许权限后再进行操作哦！", "好的，臣妾知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    }
                })
                .start();
    }

    //开启权限(新用户)
    public void perMissionSMSRegister() {
        AndPermission.with(this)
                .permission(Permission.SEND_SMS)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        sendMessageToRegister();//向手机发送验证码
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        // 权限申请失败回调。可提示
                        MessageDialog.show(LoginPage.this, "温馨提示", "请允许权限后再进行操作哦！", "好的，臣妾知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    }
                })
                .start();
    }

    //开启权限(新用户)
    public void perMissionSMSyzmLogin() {
        AndPermission.with(this)
                .permission(Permission.SEND_SMS)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        changeSendMessage();  //短信验证码登录
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        // 权限申请失败回调。可提示
                        MessageDialog.show(LoginPage.this, "温馨提示", "请允许权限后再进行操作哦！", "好的，臣妾知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    }
                })
                .start();
    }

    //向手机发送验证码
    public void sendMessageToRegister() {
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
                        TipDialog.show(LoginPage.this, "该手机号已注册", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
                    } else {
                        SharedPreferences sharedPreferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("myphone", phone).apply();
                        startActivity(new Intent(LoginPage.this, RegisterPage.class));
                    }
                    // TODO 利用国家代码和手机号码进行后续的操作
                } else {
                    // TODO 处理错误的结果
                }
            }
        });
        //将结果显示在界面
        registerPage.show(LoginPage.this);

    }

    //短信验证码登录
    private void changLogin() {
        perMissionSMSyzmLogin();
    }

    //找回密码
    private void findPwd() {
        perMissionSMSFindPwd();
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

                    SharedPreferences sharedPreferences = getSharedPreferences("findphone", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("findpwd", phone).apply();

                    ArrayList<Student> listphone = (ArrayList<Student>) studentDao.queryBuilder().where(StudentDao.Properties.Sphone.eq(phone)).list();

                    if (listphone.size() > 0) {
                        startActivity(new Intent(LoginPage.this, FindPwd.class));
                    } else {
                        TipDialog.show(LoginPage.this, "手机号不存在", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
                    }
                    // TODO 利用国家代码和手机号码进行后续的操作
                } else {
                    // TODO 处理错误的结果
                }
            }
        });
        //将结果显示在界面
        registerPage.show(LoginPage.this);

    }

    //向手机发送验证码/换个方式登陆
    public void changeSendMessage() {
        cn.smssdk.gui.RegisterPage registerPage = new cn.smssdk.gui.RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 处理成功的结果
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country"); // 国家代码，如“86”
                    String phone = (String) phoneMap.get("phone"); // 手机号码，如“13800138000”
                    common.SubmitUserInfo(country, phone);

                    ArrayList<Student> listchangephone = (ArrayList<Student>) studentDao.queryBuilder().where(StudentDao.Properties.Sphone.eq(phone)).list();
                    SharedPreferences sharedPreferences = getSharedPreferences("changephone", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("cgphone", phone).apply();

                    if (listchangephone.size() > 0) {
                        WaitDialog.show(LoginPage.this, "登录中...");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(LoginPage.this, Mainpage.class));
                            }
                        }, 2000);
                    } else {
                        TipDialog.show(LoginPage.this, "用户不存在", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
                    }
                    // TODO 利用国家代码和手机号码进行后续的操作
                } else {
                    // TODO 处理错误的结果
                }
            }
        });
        //将结果显示在界面
        registerPage.show(LoginPage.this);

    }

    //记住账号和密码
    private void remember() {
        //获取存储的注册账号和密码
        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String sidsp = sharedPreferences.getString("sid", null);

        SharedPreferences sp = getSharedPreferences("LoginPhone", Context.MODE_PRIVATE);
        String spphone = sp.getString("phone", null);

        SharedPreferences cg = getSharedPreferences("changephone", Context.MODE_PRIVATE);
        String cgph = cg.getString("cgphone",null);

        //查询密码
        if (sidsp != null) {
            ArrayList<Student> listpwd = (ArrayList<Student>) studentDao.queryBuilder().where(StudentDao.Properties.Sid.eq(sidsp)).list();
            mEtPhone.setText(sidsp);
            if (listpwd.size() > 0) {
                for (int i = 0; i < listpwd.size(); i++) {
                    mEtPwd.setText(listpwd.get(i).getSpwd());
                }
            }
            //如果登录过直接跳转到首页
            startActivity(new Intent(this, Mainpage.class));
        }
        if (spphone != null) {
            ArrayList<Student> listpwdphone = (ArrayList<Student>) studentDao.queryBuilder().where(StudentDao.Properties.Sphone.eq(spphone)).list();
            mEtPhone.setText(spphone);
            if (listpwdphone.size() > 0) {
                for (int i = 0; i < listpwdphone.size(); i++) {
                    mEtPwd.setText(listpwdphone.get(i).getSpwd());
                }
            }
            //如果登录过直接跳转到首页
            startActivity(new Intent(this, Mainpage.class));
        }

        if (cgph != null) {
            ArrayList<Student> listpwd2 = (ArrayList<Student>) studentDao.queryBuilder().where(StudentDao.Properties.Sphone.eq(cgph)).list();
            mEtPhone.setText(sidsp);
            if (listpwd2.size() > 0) {
                for (int i = 0; i < listpwd2.size(); i++) {
                    mEtPwd.setText(listpwd2.get(i).getSpwd());
                }
            }
            //如果登录过直接跳转到首页
            startActivity(new Intent(this, Mainpage.class));
        }
    }

    //初始化布局
    private void initView() {
        videoview = findViewById(R.id.videoView);
        //拿到全局的单例 HttpProxyCacheServer
        HttpProxyCacheServer proxy = VideoCache.getProxy(getApplicationContext());
        //传入网络视频的url
        String proxyUrl = proxy.getProxyUrl("http://v.weishi.qq.com/shg_963631052_1047_95a3f461591a4d7e8929bdd7e364vide.f20.mp4?dis_k=1089ab84d16272519b8f20198c432279&dis_t=1550669111&guid=0508AFC000E081E13F01036CF26192E5&pver=4.9.1&fromtag=0&personid=h5&&wsadapt=h5_0220212511_257998436_.196.143_2_2_2_0_0_0_0");
        videoview.setVideoPath(proxyUrl);  //给videoview设置proxyUrl
        videoview.start();//开始播放
        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoview.start();
            }
        });
    }

    //返回键动作
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();//结束
            finishAffinity();//杀死进程
            System.exit(0);//退出
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
