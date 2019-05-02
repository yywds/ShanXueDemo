package com.example.administrator.onlinelearning.Page.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.onlinelearning.LoginAndRegister.LoginPage;
import com.example.administrator.onlinelearning.Page.HomeFragment;
import com.example.administrator.onlinelearning.Page.MoreFragment;
import com.example.administrator.onlinelearning.Page.PkFragment;
import com.example.administrator.onlinelearning.Page.StudyCircleFragment;
import com.example.administrator.onlinelearning.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeMainpage extends FragmentActivity {
    HomeFragment homeFragment;
    StudyCircleFragment studyCircleFragment;
    PkFragment meFragment;
    MoreFragment moreFragment;
    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.tv_main_home)
    TextView tvMainHome;
    @BindView(R.id.tv_main_invest)
    TextView tvMainInvest;
    @BindView(R.id.tv_main_me)
    TextView tvMainMe;
    @BindView(R.id.tv_main_more)
    TextView tvMainMore;
    @BindView(R.id.bottom)
    LinearLayout bottom;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);
        initWindow();//初始化
        setSelect(3);  //默认显示我的页
    }

    private void initWindow() {
        //沉浸式
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //改变虚拟键盘颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#000000"));
        }
    }

    @OnClick({R.id.tv_main_home, R.id.tv_main_invest, R.id.tv_main_me, R.id.tv_main_more})
    public void showTab(View view) {
//        Toast.makeText(this, "选择的具体的Tab", Toast.LENGTH_SHORT).show();
        switch (view.getId()) {
            case R.id.tv_main_home:
                setSelect(0);
                break;
            case R.id.tv_main_invest:
                setSelect(1);
                break;
            case R.id.tv_main_me:
                setSelect(2);
                break;
            case R.id.tv_main_more:
                setSelect(3);
                break;
        }
    }

    //提供相应的fragment显示
    public void setSelect(int i) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        //隐藏所有Fragment的显示
        hideFragments();
        //重置ImageView和TextView的显示状态
        resetTab();

        switch (i) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();//创建对象以后，并不会马上调用生命周期方法。而是在commit()之后，方才调用
                    transaction.add(R.id.fl_main, homeFragment);
                }
                //显示当前的fragment
                transaction.show(homeFragment);
                //改变选中项的图片和文本颜色的变化
                tvMainHome.setTextSize(18);
                tvMainHome.setTextColor(getResources().getColor(R.color.white));

                break;
            case 1:
                if (studyCircleFragment == null) {
                    studyCircleFragment = new StudyCircleFragment();
                    transaction.add(R.id.fl_main, studyCircleFragment);
                }
                transaction.show(studyCircleFragment);
                //改变选中项的图片和文本颜色的变化
                tvMainInvest.setTextSize(18);
                tvMainInvest.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                if (meFragment == null) {
                    meFragment = new PkFragment();
                    transaction.add(R.id.fl_main, meFragment);
                }
                transaction.show(meFragment);
                //改变选中项的图片和文本颜色的变化
                tvMainMe.setTextSize(19);
                tvMainMe.setTextColor(getResources().getColor(R.color.white));

                break;
            case 3:
                if (moreFragment == null) {
                    moreFragment = new MoreFragment();
                    transaction.add(R.id.fl_main, moreFragment);
                }
                transaction.show(moreFragment);
                //改变选中项的图片和文本颜色的变化
                tvMainMore.setTextSize(18);
                tvMainMore.setTextColor(getResources().getColor(R.color.white));
                break;
        }
        transaction.commit();//提交事务
    }

    private void resetTab() {

        //原来文本颜色
        tvMainHome.setTextSize(16);
        tvMainHome.setTextColor(getResources().getColor(R.color.tolbar_text));
        tvMainInvest.setTextSize(16);
        tvMainInvest.setTextColor(getResources().getColor(R.color.tolbar_text));
        tvMainMe.setTextSize(16);
        tvMainMe.setTextColor(getResources().getColor(R.color.tolbar_text));
        tvMainMore.setTextSize(16);
        tvMainMore.setTextColor(getResources().getColor(R.color.tolbar_text));
        //这种方式也可以
//        tvMainMore.setTextColor(ContextCompat.getColor(this, R.color.home_back_unselected));
    }

    private void hideFragments() {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (studyCircleFragment != null) {
            transaction.hide(studyCircleFragment);
        }
        if (meFragment != null) {
            transaction.hide(meFragment);
        }
        if (moreFragment != null) {
            transaction.hide(moreFragment);
        }

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
