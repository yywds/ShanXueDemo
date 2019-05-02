package com.example.administrator.onlinelearning.Page.Answer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.kongzue.dialog.v2.MessageDialog;
import com.kongzue.dialog.v2.Notification;
import com.kongzue.dialog.v2.SelectDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kongzue.dialog.v2.Notification.TYPE_NORMAL;

public class AnswerMainActivity2 extends AppCompatActivity {
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
    private int notifactionType = TYPE_NORMAL;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmentanswer);
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
        tiaozhan();
    }

    private void dati1() {
        timu.setText("2.以下四个选项中哪个具有“体育场”的意思?");
        xuanze1.setText("A.stadium");
        xuanze2.setText("B.height");
        xuanze3.setText("C.live");
        xuanze4.setText("D.moose");
    }

    public void tiaozhan() {
        mediaPlayer = MediaPlayer.create(AnswerMainActivity2.this, R.raw.yizhaodi);
        mediaPlayer.start();
        //10秒，每秒一次的跳转
        CountDownTimer countDownTimer = new CountDownTimer(15000 + 1050, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText((millisUntilFinished / 1000 - 1) + "S");
                dati1();
            }

            @Override
            //倒计时结束的动作
            public void onFinish() {
                shijiandao();
            }
        }.start();
    }

    //时间到的对话框
    public void shijiandao() {
        SelectDialog.show(AnswerMainActivity2.this, "时间到", "抢答失败！", "再挑战一次", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mediaPlayer.stop();
                startActivity(new Intent(AnswerMainActivity2.this, AnswerMainActivity.class));
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mediaPlayer.stop();
                startActivity(new Intent(AnswerMainActivity2.this, AnswerMainActivityshibai.class));

            }
        });
    }

    //选择错误的对话框
    public void shibai() {
        MessageDialog.show(AnswerMainActivity2.this, "很遗憾", "回答错误！", "继续抢答", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    //选择正确的对话框
    public void zhengque() {
        MessageDialog.show(AnswerMainActivity2.this, "恭喜你", "回答正确！", "下一题", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mediaPlayer.stop();
                startActivity(new Intent(AnswerMainActivity2.this, AnswerMainActivity3.class));
            }
        });
    }

    @OnClick({R.id.ll_main_home1, R.id.ll_main_home2, R.id.ll_main_home3, R.id.ll_main_home4, R.id.timu, R.id.xuanze1, R.id.xuanze2, R.id.xuanze3, R.id.xuanze4, R.id.btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_main_home1:
                mediaPlayer.stop(); //答题音乐停止
                startActivity(new Intent(this, HomeMainActivity.class));
                break;
            case R.id.ll_main_home2:
                mediaPlayer.stop(); //答题音乐停止
                startActivity(new Intent(this, InteractionMainpage.class));
                break;
            case R.id.ll_main_home3:
                mediaPlayer.stop(); //答题音乐停止
                startActivity(new Intent(this, PkMainpage.class));
                break;
            case R.id.ll_main_home4:
                mediaPlayer.stop(); //答题音乐停止
                startActivity(new Intent(this, MoreMainActivity.class));
                break;
            case R.id.timu:
                break;
            case R.id.xuanze1:
                check1();
                break;
            case R.id.xuanze2:
                check2();
                break;
            case R.id.xuanze3:
                check3();
                break;
            case R.id.xuanze4:
                check4();
                break;
            case R.id.btn:
                btn_check();
                break;
        }
    }

    private void btn_check() {
        //状态栏通知
        Notification.show(this, 1, R.mipmap.apptubiao, getString(R.string.app_name), "请先作答", Notification.TYPE_ERROR, notifactionType);
    }
//        MessageDialog.show(AnswerMainActivity2.this, "提示", "请先作答！", "好的", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });    }

    private void check4() {
        xuanze1.setTextColor(Color.parseColor("#ffffff"));
        xuanze2.setTextColor(Color.parseColor("#ffffff"));
        xuanze3.setTextColor(Color.parseColor("#ffffff"));
        xuanze4.setTextColor(Color.parseColor("#FFFF00"));
        xuanze4.getPaint().setFakeBoldText(true);
        shibai();

    }

    private void check3() {
        xuanze1.setTextColor(Color.parseColor("#ffffff"));
        xuanze2.setTextColor(Color.parseColor("#ffffff"));
        xuanze3.setTextColor(Color.parseColor("#FFFF00"));
        xuanze3.getPaint().setFakeBoldText(true);
        xuanze4.setTextColor(Color.parseColor("#ffffff"));
        shibai();
    }

    private void check2() {
        xuanze2.setTextColor(Color.parseColor("#FFFF00"));
        xuanze2.getPaint().setFakeBoldText(true);
        xuanze1.setTextColor(Color.parseColor("#ffffff"));
        xuanze3.setTextColor(Color.parseColor("#ffffff"));
        xuanze4.setTextColor(Color.parseColor("#ffffff"));
        shibai();
    }

    private void check1() {
        xuanze1.setTextColor(Color.parseColor("#FFFF00"));
        xuanze1.getPaint().setFakeBoldText(true);
        xuanze2.setTextColor(Color.parseColor("#ffffff"));
        xuanze3.setTextColor(Color.parseColor("#ffffff"));
        xuanze4.setTextColor(Color.parseColor("#ffffff"));
        zhengque();
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
