package com.example.administrator.onlinelearning.Page.Tool;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.example.administrator.onlinelearning.Page.Answer.AnswerMainActivity;
import com.example.administrator.onlinelearning.Page.More.MoreMainActivity;
import com.example.administrator.onlinelearning.Page.Tool.DrawingView.MainActivity;
import com.example.administrator.onlinelearning.Page.Tool.OCR.GeneralActivity;
import com.example.administrator.onlinelearning.Page.Tool.Study.StudyActivity;
import com.example.administrator.onlinelearning.Page.Tool.Translate.TranslateMainActivity;
import com.example.administrator.onlinelearning.Page.home.HomeMainActivity;
import com.example.administrator.onlinelearning.Page.home.InteractionMainpage;
import com.example.administrator.onlinelearning.R;
import com.kongzue.dialog.v2.MessageDialog;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ToolMainActivity extends AppCompatActivity {
    @BindView(R.id.tv_saoyisao)
    TextView tvSaoyisao;
    @BindView(R.id.tv_wenzitique)
    TextView tvWenzitique;
    @BindView(R.id.tv_yinghanhuyi)
    TextView tvYinghanhuyi;
    @BindView(R.id.tv_xuebamoshi)
    TextView tvXuebamoshi;
    @BindView(R.id.tv_jihehuaban)
    TextView tvJihehuaban;
    @BindView(R.id.study)
    TextView study;
    @BindView(R.id.interaction)
    TextView interaction;
    @BindView(R.id.pk)
    TextView pk;
    @BindView(R.id.me)
    TextView me;
    @BindView(R.id.bottom)
    LinearLayout bottom;
    @BindView(R.id.qiangda)
    TextView qiangda;
    @BindView(R.id.iv_main_home3)
    ImageView ivMainHome3;
    @BindView(R.id.ll_main_home1)
    LinearLayout llMainHome1;
    @BindView(R.id.ll_main_home2)
    LinearLayout llMainHome2;
    @BindView(R.id.ll_main_home3)
    LinearLayout llMainHome3;
    @BindView(R.id.ll_main_home4)
    LinearLayout llMainHome4;
    private long exitTime;
    private int REQUEST_CODE_SCAN = 111;
    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pk);
        ButterKnife.bind(this);
        initWindow();
        initAccessToken();
        initAccessTokenWithAkSk();
        permission();
        firstRun();
        ivMainHome3.setImageResource(R.drawable.gongjulv);
        pk.setTextColor(Color.parseColor("#2ecc71"));

    }


    private void firstRun() {
        SharedPreferences sharedPreferences = getSharedPreferences("FirstPagetool", 0);
        Boolean firstRun = sharedPreferences.getBoolean("firstpagetool", true);
        if (firstRun) {
            sharedPreferences.edit().putBoolean("firstpagetool", false).commit();
            diaLogshuoming(); //如果是第一次进入此页面，则出现对话框，
        } else {
            //否则不出现对话框

        }
    }

    //对话框
    public void diaLogshuoming() {
        MessageDialog.show(this, "温馨提示", "在使用本页相应功能时，在出现的对话框中请允许相应权限！", "好的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    //权限
    private void permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int ret = ActivityCompat.checkSelfPermission(ToolMainActivity.this, Manifest.permission
                    .READ_EXTERNAL_STORAGE);
            if (ret != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ToolMainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1000);
                return;
            }
        }
    }

    //动态处理权限问题
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAccessToken();
        } else {
            Toast.makeText(getApplicationContext(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
        }
    }


    //
    private void alertText(String title, String message) {
        boolean isNeedLoop = false;
        if (Looper.myLooper() == null) {
            Looper.prepare();
            isNeedLoop = true;
        }
        alertDialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", null)
                .show();
        if (isNeedLoop) {
            Looper.loop();
        }
    }

    //初始化AK和SK
    private void initAccessTokenWithAkSk() {
        OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                // 调用成功，返回AccessToken对象
                String token = result.getAccessToken();
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError子类SDKError对象
            }
        }, getApplicationContext(), "YXLKhwzrKmkLDpyNimde5lm5", "qWb8GAy0IZweNxgFhXK8XQtFE8duLSvg");

    }

    //初始化AccessToken
    private void initAccessToken() {

        OCR.getInstance(this).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("licence方式获取token失败", error.getMessage());
            }
        }, getApplicationContext());
    }

    //扫一扫
    private void saoyisao() {
        AndPermission.with(this)
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Intent intent = new Intent(ToolMainActivity.this, CaptureActivity.class);
                        /*ZxingConfig是配置类
                         *可以设置是否显示底部布局，闪光灯，相册，
                         * 是否播放提示音  震动
                         * 设置扫描框颜色等
                         * 也可以不传这个参数
                         * */
                        ZxingConfig config = new ZxingConfig();
                        // config.setPlayBeep(false);//是否播放扫描声音 默认为true
                        //  config.setShake(false);//是否震动  默认为true
                        // config.setDecodeBarCode(false);//是否扫描条形码 默认为true
//                                config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
//                                config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
//                                config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
                        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                        startActivityForResult(intent, REQUEST_CODE_SCAN);
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Uri packageURI = Uri.parse("package:" + getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(ToolMainActivity.this, "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                    }
                }).start();
    }

    //扫一扫返回的结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
//        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
                if (data != null) {
                    String content = data.getStringExtra(Constant.CODED_CONTENT);
                    //如果是网址则用浏览器打开，否则显示在界面上
                    if (isUrl(content)) {
                        //用默认浏览器打开扫描得到的地址
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(content.toString());
                        intent.setData(content_url);
                        startActivity(intent);
                    } else {
//                       把content的内容传给Zxingshow中的控件
                        Intent intent = new Intent(this, Zxingshow.class);
                        intent.putExtra("name", content.toString().trim());
                        startActivity(intent);
                    }
                }
            }
        }
    }

    //判断扫描的是网址
    public static boolean isUrl(String str) {
        // 转换为小写
        str = str.toLowerCase();
        String[] regex = {"http://", "https://"};
        boolean isUrl = false;
        for (int i = 0; i < regex.length; i++) {
            isUrl = isUrl || (str.contains(regex[i])) && str.indexOf(regex[i]) == 0;
        }
        return isUrl;
    }

    //初始化窗口
    private void initWindow() {
        //沉浸式
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //改变虚拟键盘颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setNavigationBarColor(Color.parseColor("#6200ED"));
        }
    }

    //释放内存
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放内存资源
        OCR.getInstance(this).release();
    }

    //点击动作
    @OnClick({R.id.qiangda, R.id.tv_saoyisao, R.id.tv_wenzitique, R.id.tv_yinghanhuyi, R.id.tv_xuebamoshi, R.id.tv_jihehuaban,R.id.ll_main_home1, R.id.ll_main_home2, R.id.ll_main_home3, R.id.ll_main_home4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_main_home1:
                startActivity(new Intent(this, HomeMainActivity.class));
                break;
            case R.id.ll_main_home2:
                startActivity(new Intent(this, InteractionMainpage.class));
                break;
            case R.id.ll_main_home4:
                startActivity(new Intent(this, MoreMainActivity.class));
                break;
            case R.id.qiangda:
                startActivity(new Intent(this, AnswerMainActivity.class));
                break;
            case R.id.tv_saoyisao:
                saoyisao();
                break;
            case R.id.tv_wenzitique:
                //跳转到文字识别界面
                startActivity(new Intent(this, GeneralActivity.class));
                break;
            case R.id.tv_yinghanhuyi:
                startActivity(new Intent(this, TranslateMainActivity.class));
                break;
            case R.id.tv_xuebamoshi:
                diaLog();
                break;
            case R.id.tv_jihehuaban:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
    //出现对话框然后跳转
    public void diaLog() {
        MessageDialog.show(this, "温馨提示", "请先开启悬浮窗权限才可以使用本功能哦！", "好的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
         startActivity(new Intent(ToolMainActivity.this, StudyActivity.class));
            }
        });
    }

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



