package com.example.administrator.onlinelearning.Page.Answer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.onlinelearning.Page.More.MoreMainActivity;
import com.example.administrator.onlinelearning.Page.home.HomeMainActivity;
import com.example.administrator.onlinelearning.Page.home.InteractionMainpage;
import com.example.administrator.onlinelearning.Page.home.PkMainpage;
import com.example.administrator.onlinelearning.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnswerMainActivityshibai extends AppCompatActivity {
    @BindView(R.id.study)
    TextView study;
    @BindView(R.id.interaction)
    TextView interaction;
    @BindView(R.id.qiangda)
    TextView qiangda;
    @BindView(R.id.pk)
    TextView pk;
    @BindView(R.id.me)
    TextView me;
    @BindView(R.id.bottom)
    LinearLayout bottom;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.kuang)
    TextView kuang;
    @BindView(R.id.timu)
    TextView timu;
    @BindView(R.id.xuanze1)
    TextView xuanze1;
    @BindView(R.id.xuanze2)
    TextView xuanze2;
    @BindView(R.id.xuanze3)
    TextView xuanze3;
    @BindView(R.id.xuanze4)
    TextView xuanze4;
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.ll_main_home1)
    LinearLayout llMainHome1;
    @BindView(R.id.ll_main_home2)
    LinearLayout llMainHome2;
    @BindView(R.id.ll_main_home3)
    LinearLayout llMainHome3;
    @BindView(R.id.ll_main_home4)
    LinearLayout llMainHome4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmentanswer2);
        ButterKnife.bind(this);
        initWindow();
    }

    private void initWindow() {
        //沉浸式
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //改变虚拟键盘颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setNavigationBarColor(Color.parseColor("#6200ED"));
        }
        xuanze1.setText("很遗憾!");
        xuanze1.getPaint().setFakeBoldText(true); //加粗
        xuanze1.setTextSize(30);
        xuanze2.setText("挑战失败");
        xuanze2.getPaint().setFakeBoldText(true); //加粗
        xuanze2.setTextSize(40);
        time.setBackgroundResource(R.drawable.coner2);
        time.setText("fail");
        time.getPaint().setFakeBoldText(true);//加粗

    }

    @OnClick({R.id.ll_main_home1, R.id.ll_main_home2, R.id.ll_main_home3, R.id.ll_main_home4, R.id.btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_main_home1:
                startActivity(new Intent(this, HomeMainActivity.class));
                break;
            case R.id.ll_main_home2:
                startActivity(new Intent(this, InteractionMainpage.class));
                break;
            case R.id.ll_main_home3:
                startActivity(new Intent(this, PkMainpage.class));
                break;
            case R.id.ll_main_home4:
                startActivity(new Intent(this, MoreMainActivity.class));
                break;
            case R.id.btn:
                startActivity(new Intent(AnswerMainActivityshibai.this, AnswerMainActivity.class));
                break;
        }
    }

    private long exitTime;

    //返回键动作
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                //获取布局xml
                View tempView = LayoutInflater.from(this).inflate(R.layout.login_message, null);
                TextView textView = (TextView) tempView.findViewById(R.id.error);
                //添加需要显示的信息
                textView.setText("\n" + "再按一次返回键切换" + "\n" + "到桌面" + "\n");
                //設置alpha值（透明度）
                tempView.getBackground().setAlpha(170);
                Toast toast = new Toast(this);
                //显示在屏幕的中间
                toast.setGravity(Gravity.CENTER, 0, 0);
                //消失到消失的時常
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(tempView);
                toast.show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                exitTime = System.currentTimeMillis();
            } else {
                finishAffinity();//杀死进程
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
