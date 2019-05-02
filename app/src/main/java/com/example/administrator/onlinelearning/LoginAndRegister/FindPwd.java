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

public class FindPwd extends AppCompatActivity implements View.OnClickListener {

    private ImageView mLv;
    private EditText mEtRzpwd;
    private EditText mEtCfpwd;
    private Button mBtnFinish;
    DaoMaster daoMaster;
    DaoSession daoSession;
    StudentDao studentDao;
    Common common;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpwd);
        mLv = (ImageView) findViewById(R.id.lv);
        mEtRzpwd = (EditText) findViewById(R.id.et_rzpwd);
        mEtCfpwd = (EditText) findViewById(R.id.et_cfpwd);
        mBtnFinish = (Button) findViewById(R.id.btn_finish);
        mBtnFinish.setOnClickListener(this);

        common.diaLog();//对话框
        initView();//初始化
        initGreenDao(); //初始化GreenDao

    }

    //初始化GreenDao
    public void initGreenDao() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "onlinelearning.db", null);
        daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
        studentDao = daoSession.getStudentDao();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_finish:
                ResortPwd();//重置密码
                break;
        }
    }

    //初始化
    private void initView() {
        //获取手机屏幕宽高
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        //加载网络图片
        String url = "https://img-blog.csdnimg.cn/20190126234536462.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzgwMDgx,size_16,color_FFFFFF,t_70";
        RequestOptions options = new RequestOptions().centerCrop().override(width, height);
        Glide.with(FindPwd.this).load(url).apply(options).into(mLv);
    }

    //重置密码
    private void ResortPwd() {
        String rzpwd = mEtRzpwd.getText().toString().trim();
        String cfpwd = mEtCfpwd.getText().toString().trim();
        SharedPreferences sp = getSharedPreferences("findphone", Context.MODE_PRIVATE);
        String getphone = sp.getString("findpwd", null);

        if (rzpwd.isEmpty() && cfpwd.isEmpty()) {
            TipDialog.show(this, "信息填写不能为空", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
        } else if (rzpwd.isEmpty()) {
            TipDialog.show(this, "重置密码不能为空", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
        } else if (cfpwd.isEmpty()) {
            TipDialog.show(this, "确认密码不能为空", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
        }
        //判断
        else if (!rzpwd.equals(cfpwd)) {
            TipDialog.show(this, "密码不一致", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
        } else {
            //更新指定数据
            Student student = studentDao.queryBuilder().where(StudentDao.Properties.Sphone.eq(getphone)).build().unique();
            if (student != null) {
                student.setSpwd(cfpwd);
                studentDao.update(student);//进行更新

                WaitDialog.show(this, "加载中...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        WaitDialog.dismiss();
                        TipDialog.show(FindPwd.this, "密码重置成功", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                    }
                }, 2000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(FindPwd.this, LoginPage.class));
                    }
                }, 4000);
            }
        }
    }


}
