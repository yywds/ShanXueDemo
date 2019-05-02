package com.example.administrator.onlinelearning.Page.Tool.Study;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.onlinelearning.Page.Tool.ToolMainActivity;
import com.example.administrator.onlinelearning.R;
import com.kongzue.dialog.v2.MessageDialog;
import com.kongzue.dialog.v2.Notification;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kongzue.dialog.v2.Notification.TYPE_NORMAL;


public class StudyActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.edit)
    EditText edit;
    @BindView(R.id.lv_shijian)
    LinearLayout lvShijian;
    @BindView(R.id.lv_daojishi)
    LinearLayout lvDaojishi;
    @BindView(R.id.lv_jiesuo)
    LinearLayout lvJiesuo;
    @BindView(R.id.btn_xuexi)
    Button btnXuexi;
    @BindView(R.id.daojishi)
    TextView daojishi;
    @BindView(R.id.jieshuo)
    TextView jieshuo;
    @BindView(R.id.pengwo)
    TextView pengwo;
    @BindView(R.id.lv_pengwo)
    LinearLayout lvPengwo;
    private int notifactionType = TYPE_NORMAL;
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studymain);
        ButterKnife.bind(this);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        requestOverlayPermission();

    }


    //1.请求授予悬浮窗
    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
//                diaLog(); //如果没有开启悬浮窗权限则提示
                askForPermission();
            } else {

            }
        }
    }

    //2.开启悬浮窗对话提示框


    //3.跳转到悬浮窗权限授予界面
    public void askForPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            } else {

            }
        }
    }

    //4.用户返回的悬浮窗权限提示
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
                if (!Settings.canDrawOverlays(this)) {
                    //如果权限授予失败3秒后跳转
                    Notification.show(this, 1, R.mipmap.apptubiao, getString(R.string.app_name), "权限授予失败，无法使用本功能！", Notification.TYPE_ERROR, notifactionType);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(StudyActivity.this, ToolMainActivity.class));
                        }
                    }, 2000); //2秒后跳转
                } else {

                }
            }
        }
    }

    @OnClick(R.id.btn_xuexi)
    public void onClick(View view) {
        int len = edit.length();
        if (len == 0) {
            Toast.makeText(this, "请先输入时间！", Toast.LENGTH_SHORT).show();
        } else {
            ((ViewGroup) view.getParent()).removeView(view);//每一次回收一个子的view（因为倒计时的时候回没秒调用一次）
            //产生对话，挡住屏幕让用户点击不了屏幕
            final Dialog dialog = new AlertDialog.Builder(getApplicationContext(), R.style.TransparentWindowBg).setView(view).create();
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
            dialog.setCancelable(false);
            dialog.show();

            int i = Integer.parseInt(edit.getText().toString());//把字符型转换为整型
            CountDownTimer countDownTimer = new CountDownTimer(i * 60 * 1000 + 1050, 1000) {
                @Override
                public void onTick(long l) {
                    //监听来电，如果来电则取消解锁屏幕（也就是取消对话）
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    tm.getCallState();
                    if (tm.getCallState() == TelephonyManager.CALL_STATE_RINGING) {

                        dialog.dismiss();
                        finishAffinity();//杀死所有activity
                        //回到桌面
                        Intent home = new Intent(Intent.ACTION_MAIN);
                        home.addCategory(Intent.CATEGORY_HOME);
                        startActivity(home);

                    } else {
                        lvShijian.setVisibility(View.INVISIBLE);
                        daojishi.setText("倒计时");
                        pengwo.setText("你不要碰我了，好好学习吧！ ");
                        jieshuo.setText((l / 1000 - 1) + "S" + " " + "后屏幕解锁");
                        btnXuexi.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onFinish() {

                    daojishi.setText("");
                    jieshuo.setText("");
                    pengwo.setText("");
                    edit.setText("");
                    btnXuexi.setVisibility(View.VISIBLE);
                    lvShijian.setVisibility(View.VISIBLE);
                    dialog.dismiss();//结束对话框（对话框消失）
                    startActivity(new Intent(StudyActivity.this,ToolMainActivity.class));
                }
            }.start();
        }
    }
}




