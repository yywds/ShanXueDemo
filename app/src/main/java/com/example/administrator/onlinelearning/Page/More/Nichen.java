package com.example.administrator.onlinelearning.Page.More;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.onlinelearning.CommonAndPlugin.DaoMaster;
import com.example.administrator.onlinelearning.CommonAndPlugin.DaoSession;
import com.example.administrator.onlinelearning.CommonAndPlugin.StudentDao;
import com.example.administrator.onlinelearning.Model.Student;
import com.example.administrator.onlinelearning.R;
import com.kongzue.dialog.v2.Notification;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kongzue.dialog.v2.Notification.TYPE_NORMAL;

public class Nichen extends AppCompatActivity {
    @BindView(R.id.tv_nichen)
    TextView tvNichen;
    @BindView(R.id.xian)
    TextView xian;
    @BindView(R.id.bjnc)
    TextInputEditText bjnc;
    @BindView(R.id.btn)
    Button btn;
    StudentDao studentDao;
    DaoMaster daoMaster;
    DaoSession daoSession;
    private int notifactionType = TYPE_NORMAL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nichen);
        ButterKnife.bind(this);
        initGreenDao();
        initWindow();
    }

    @OnClick({R.id.tv_nichen, R.id.btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_nichen:
                startActivity(new Intent(this, MoreMainActivity.class));
                break;
            case R.id.btn:
                xiugai();
                break;
        }
    }

    private void xiugai() {
        String change = bjnc.getText().toString().trim();
        if (change.length() <= 0) {
            Notification.show(this, 1, R.mipmap.apptubiao, getString(R.string.app_name), "填写不能为空", Notification.TYPE_ERROR, notifactionType);
        } else {
            SharedPreferences sx = getSharedPreferences("SX", Context.MODE_PRIVATE);
            String sid = sx.getString("shanxue", null);

            ArrayList<Student> list = (ArrayList<Student>) studentDao.queryBuilder().where(StudentDao.Properties.Sid.eq(sid)).list();
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    //更新数据库
                    Student student = new Student();
                    String change2 = bjnc.getText().toString().trim();
                    String name = list.get(i).getSname().replaceAll(change2, null);
                    student.setSname(name);
                    studentDao.insert(student);

                }
            }

            Notification.show(this, 1, R.mipmap.apptubiao, getString(R.string.app_name), "保存成功", Notification.TYPE_ERROR, notifactionType);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Nichen.this, MoreMainActivity.class));
                }
            },3000);

        }
    }

    private void initWindow() {
        //沉浸式
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //改变虚拟键盘颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setNavigationBarColor(Color.parseColor("#6200ED"));
        }
    }

    //初始化GreenDao
    public void initGreenDao() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "onlinelearning.db", null);
        daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
        studentDao = daoSession.getStudentDao();
    }
}
