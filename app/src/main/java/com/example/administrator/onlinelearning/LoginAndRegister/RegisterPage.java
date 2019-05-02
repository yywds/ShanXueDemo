package com.example.administrator.onlinelearning.LoginAndRegister;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.administrator.onlinelearning.CommonAndPlugin.Common;
import com.example.administrator.onlinelearning.CommonAndPlugin.DaoMaster;
import com.example.administrator.onlinelearning.CommonAndPlugin.DaoSession;
import com.example.administrator.onlinelearning.CommonAndPlugin.StudentDao;
import com.example.administrator.onlinelearning.Model.Student;
import com.example.administrator.onlinelearning.R;
import com.kongzue.dialog.v2.TipDialog;
import com.kongzue.dialog.v2.WaitDialog;
import com.mob.MobSDK;

import java.util.ArrayList;
import java.util.Random;


public class RegisterPage extends AppCompatActivity implements View.OnClickListener {

    private ImageView mLv;
    private EditText mEtPhone;
    private EditText mEtPwd;
    private Button mBtnLogin;
    private EditText mEtCfpwd;
    private EditText mEtStudent;
    StudentDao studentDao;
    DaoMaster daoMaster;
    DaoSession daoSession;
    Common common;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏，这句话要写在onCreate前面，否则报错
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mLv = (ImageView) findViewById(R.id.lv);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtPhone.clearFocus();  //清除焦点
        mEtPhone.setOnClickListener(this);
        mEtStudent = (EditText) findViewById(R.id.et_student);
        mEtStudent.setOnClickListener(this);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mEtPwd.setOnClickListener(this);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        mEtCfpwd = (EditText) findViewById(R.id.et_cfpwd);
        mEtCfpwd.setOnClickListener(this);

        common.diaLog();//对话框
        //沉浸式
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        //获得存储手机号
        SharedPreferences sharedPreferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
        mEtPhone.setText(sharedPreferences.getString("myphone", null));


        initView();//初始化
        MobSDK.init(this);  //初始化MobSDk
        initGreenDao(); //GreenDao初始化
    }

    //初始化GreenDao
    public void initGreenDao() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "onlinelearning.db", null);
        daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
        studentDao = daoSession.getStudentDao();
    }
    //初始化
    private void initView() {
        //获取手机屏幕宽高
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        int width = displayMetrics.widthPixels;
//        int height = displayMetrics.heightPixels;
//
//        //加载网络图片
//        String url = "https://img-blog.csdnimg.cn/20190126234536462.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzgwMDgx,size_16,color_FFFFFF,t_70";
//        RequestOptions options = new RequestOptions().centerCrop().override(width, height);
//        Glide.with(RegisterPage.this).load(url).apply(options).into(mLv);
    }


    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                registerStudent(); //注册
                break;
        }
    }

    //学生注册
    private void registerStudent() {
        Student student = new Student();
        Random random = new Random();
        String sphone = mEtPhone.getText().toString().trim();
        String sname = mEtStudent.getText().toString().trim();
        String spwd = mEtPwd.getText().toString().trim();
        String scfpwd = mEtCfpwd.getText().toString().trim();
        //查询
        ArrayList<Student> list = (ArrayList<Student>) studentDao.queryBuilder().where(StudentDao.Properties.Sname.eq(sname)).list();
        //判断是否为空
        if (sphone.length() <= 0 && sname.length() <= 0 && spwd.length() <= 0 && scfpwd.length() <= 0) {
            TipDialog.show(this, "信息填写不能为空哦", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
        } else if (sphone.length() <= 0 && sname.length() >= 0 && spwd.length() >= 0 && scfpwd.length() >= 0) {
            TipDialog.show(this, "手机号不能为空", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
        } else if (sname.length() <= 0 && sphone.length() >= 0 && spwd.length() >= 0 && scfpwd.length() >= 0) {
            TipDialog.show(this, "用户名不能为空", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
        } else if (spwd.length() <= 0 && sname.length() >= 0 && sphone.length() >= 0 && scfpwd.length() >= 0) {
            TipDialog.show(this, "密码不能为空", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
        } else if (scfpwd.length() <= 0 && sname.length() >= 0 && spwd.length() >= 0 && sphone.length() >= 0) {
            TipDialog.show(this, "确认密码不能为空", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
        }
        //判断密码是否一致
        else if (!spwd.equals(scfpwd)) {
            TipDialog.show(this, "密码不一致", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
        }

        //进行注册
        else {
            if (list.size() > 0) {
                TipDialog.show(this, "该用户已存在", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
            } else {
                long numlong = Math.abs(random.nextLong());//取一个数的绝对值
                int numint = (int) (Math.random() * 999999999) + 10000000;//产生10000000-999999999以内的随机数
                String sidnum =String.valueOf(numint);
                student.setId(numlong);
                student.setSid(sidnum);
                student.setSname(sname);
                student.setSphone(sphone);
                student.setSpwd(spwd);
                studentDao.insert(student);
                WaitDialog.show(RegisterPage.this, "注册中...");
                //存储用户登录状态
                SharedPreferences sharedPreferences2 = getSharedPreferences("register", Context.MODE_PRIVATE);
                sharedPreferences2.edit().putString("sid", sidnum).putString("sname", sname).putString("spwd", spwd).apply();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        WaitDialog.dismiss();
                        TipDialog.show(RegisterPage.this, "恭喜你！注册成功", TipDialog.SHOW_TIME_LONG, TipDialog.TYPE_FINISH);
                    }
                },2000);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(RegisterPage.this, ToLogin.class));
                    }
                }, 4000);  //4秒后跳转
            }
        }
    }

}
