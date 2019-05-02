package com.example.administrator.onlinelearning.Page.More;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.onlinelearning.CommonAndPlugin.BitmapUtils;
import com.example.administrator.onlinelearning.CommonAndPlugin.DaoMaster;
import com.example.administrator.onlinelearning.CommonAndPlugin.DaoSession;
import com.example.administrator.onlinelearning.CommonAndPlugin.StudentDao;
import com.example.administrator.onlinelearning.LoginAndRegister.LoginPage;
import com.example.administrator.onlinelearning.Model.Student;
import com.example.administrator.onlinelearning.Page.Answer.AnswerMainActivity;
import com.example.administrator.onlinelearning.Page.home.HomeMainActivity;
import com.example.administrator.onlinelearning.Page.home.InteractionMainpage;
import com.example.administrator.onlinelearning.Page.home.PkMainpage;
import com.example.administrator.onlinelearning.R;
import com.kongzue.dialog.v2.MessageDialog;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import com.yanzhenjie.permission.PermissionListener;

public class MoreMainActivity extends AppCompatActivity {
    private static final int PICTURE = 100;
    private static final int CAMERA = 200;
    @BindView(R.id.qiangda)
    TextView qiangda;
    @BindView(R.id.iv_main_home4)
    ImageView ivMainHome4;
    @BindView(R.id.ll_main_home1)
    LinearLayout llMainHome1;
    @BindView(R.id.ll_main_home2)
    LinearLayout llMainHome2;
    @BindView(R.id.ll_main_home3)
    LinearLayout llMainHome3;
    @BindView(R.id.ll_main_home4)
    LinearLayout llMainHome4;
    @BindView(R.id.tv_nichen)
    TextView tvNichen;
    @BindView(R.id.tv_gxqm)
    TextView tvGxqm;
    @BindView(R.id.tv_xingbie)
    TextView tvXingbie;
    @BindView(R.id.tv_xuexiao)
    TextView tvXuexiao;
    @BindView(R.id.tv_fankui)
    TextView tvFankui;
    private long exitTime;
    @BindView(R.id.top)
    ImageView top;
    @BindView(R.id.touxiang)
    ImageView touxiang;
    @BindView(R.id.yonghuming)
    TextView yonghuming;
    @BindView(R.id.shanxuehao)
    TextView shanxuehao;

    @BindView(R.id.btn_tuichu)
    TextView btnTuichu;

    StudentDao studentDao;
    DaoMaster daoMaster;
    DaoSession daoSession;
    @BindView(R.id.study)
    TextView study;
    @BindView(R.id.interaction)
    TextView interaction;
    @BindView(R.id.pk)
    TextView pk;
    @BindView(R.id.me)
    TextView me;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me);
        ButterKnife.bind(this);
        initGreenDao();//初始化GreenDao
        initWindow();
        initview();//初始化界面
        ivMainHome4.setImageResource(R.drawable.txlv);
        me.setTextColor(Color.parseColor("#2ecc71"));
    }

    private void initWindow() {
        //沉浸式
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //改变虚拟键盘颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setNavigationBarColor(Color.parseColor("#6200ED"));
        }
    }

    //初始化
    private void initview() {
        //加载网络图片
        String url = "https://img-blog.csdnimg.cn/20190502113952665.png";
        Glide.with(MoreMainActivity.this).load(url).into(touxiang);

        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String spsid = sharedPreferences.getString("sid", null);

        SharedPreferences sp = getSharedPreferences("LoginPhone", Context.MODE_PRIVATE);
        String spphone = sp.getString("phone", null);

        SharedPreferences cgp = getSharedPreferences("changephone", Context.MODE_PRIVATE);
        String cg = cgp.getString("cgphone", null);

        if (spphone != null) {
            //查询
            ArrayList<Student> listnamephone = (ArrayList<Student>) studentDao.queryBuilder().where(StudentDao.Properties.Sphone.eq(spphone)).list();
            for (int i = 0; i < listnamephone.size(); i++) {
                String sname = listnamephone.get(i).getSname();
                yonghuming.setText(sname);
            }
            for (int i = 0; i < listnamephone.size(); i++) {
                String ssid = listnamephone.get(i).getSid();
                shanxuehao.setText("闪学号：" + ssid);
                SharedPreferences sx = getSharedPreferences("SX", Context.MODE_PRIVATE);
                sx.edit().putString("shanxue",ssid).apply();
            }
        }
        if (spsid != null) {
            ArrayList<Student> listname = (ArrayList<Student>) studentDao.queryBuilder().where(StudentDao.Properties.Sid.eq(spsid)).list();
            for (int i = 0; i < listname.size(); i++) {
                String sname = listname.get(i).getSname();
                yonghuming.setText(sname);
            }
            shanxuehao.setText("闪学号：" + spsid);
            SharedPreferences sx = getSharedPreferences("SX", Context.MODE_PRIVATE);
            sx.edit().putString("shanxue",spsid).apply();
        }

        if (cg != null) {
            ArrayList<Student> cgname = (ArrayList<Student>) studentDao.queryBuilder().where(StudentDao.Properties.Sphone.eq(cg)).list();
            for (int i = 0; i < cgname.size(); i++) {
                String sname = cgname.get(i).getSname();
                yonghuming.setText(sname);
            }
            for (int i = 0; i < cgname.size(); i++) {
                String ssid = cgname.get(i).getSid();
                shanxuehao.setText("闪学号：" + ssid);
                SharedPreferences sx = getSharedPreferences("SX", Context.MODE_PRIVATE);
                sx.edit().putString("shanxue",ssid).apply();
            }
        }



    }

    //初始化GreenDao
    public void initGreenDao() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "onlinelearning.db", null);
        daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
        studentDao = daoSession.getStudentDao();
    }


    @OnClick({R.id.tv_nichen, R.id.tv_gxqm, R.id.tv_xingbie, R.id.tv_xuexiao, R.id.tv_fankui,R.id.touxiang,R.id.btn_tuichu, R.id.qiangda, R.id.ll_main_home1, R.id.ll_main_home2, R.id.ll_main_home3, R.id.ll_main_home4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_nichen:
                startActivity(new Intent(this, Nichen.class));
                break;
            case R.id.tv_gxqm:
                startActivity(new Intent(this, Gxqm.class));
                break;
            case R.id.tv_xingbie:
                break;
            case R.id.tv_xuexiao:
                break;
            case R.id.tv_fankui:
                break;

            case R.id.qiangda:
                startActivity(new Intent(this, AnswerMainActivity.class));
                break;
            case R.id.touxiang:
                perMission();
                break;
            case R.id.ll_main_home1:
                startActivity(new Intent(this, HomeMainActivity.class));
                break;
            case R.id.ll_main_home2:
                startActivity(new Intent(this, InteractionMainpage.class));
                break;
            case R.id.ll_main_home3:
                startActivity(new Intent(this, PkMainpage.class));
                break;
            case R.id.btn_tuichu:
                //  清除Login或者LoginPhone存储的信息
                SharedPreferences login = getSharedPreferences("Login", Context.MODE_PRIVATE);
                SharedPreferences phone = getSharedPreferences("LoginPhone", Context.MODE_PRIVATE);
                SharedPreferences cgphone = getSharedPreferences("changephone", Context.MODE_PRIVATE);

                if (login != null) {
                    SharedPreferences.Editor editor1 = login.edit();
                    editor1.clear();
                    editor1.commit();
                    startActivity(new Intent(this, LoginPage.class));
                }
                if (phone != null) {
                    SharedPreferences.Editor editor2 = phone.edit();
                    editor2.clear();
                    editor2.commit();
                    startActivity(new Intent(this, LoginPage.class));
                }
                if (cgphone != null) {
                    SharedPreferences.Editor editor2 = cgphone.edit();
                    editor2.clear();
                    editor2.commit();
                    startActivity(new Intent(this, LoginPage.class));
                }
                break;
        }
    }

    //开启权限
    public void perMission() {
        AndPermission.with(this)
                .permission(Permission.CAMERA)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        touxiangshow();
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        // 权限申请失败回调。可提示
                        MessageDialog.show(MoreMainActivity.this, "温馨提示", "请允许权限后再进行操作哦！", "好的，臣妾知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    }
                })
                .start();
    }

    private void touxiangshow() {
        String[] items = new String[]{"图库", "相机"};
        //提供一个AlertDialog
        new AlertDialog.Builder(this)
                .setTitle("选择来源")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0://图库
//                                    UIUtils.toast("图库",false);
                                //启动其他应用的activity:使用隐式意图
                                Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(picture, PICTURE);
                                break;
                            case 1://相机
//                                    UIUtils.toast("相机",false);
                                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(camera, CAMERA);
                                break;
                        }
                    }
                })
                .setCancelable(false)
                .show();
    }

    //重写启动新的activity以后的回调方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA && resultCode == RESULT_OK && data != null) {//相机
            //获取intent中的图片对象
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            //对获取到的bitmap进行压缩、圆形处理
            bitmap = BitmapUtils.zoom(bitmap, touxiang.getWidth(), touxiang.getHeight());
            bitmap = BitmapUtils.circleBitmap(bitmap);

            //加载显示
            touxiang.setImageBitmap(bitmap);
            //上传到服务器（省略）

            //保存到本地
            saveImage(bitmap);


        } else if (requestCode == PICTURE && resultCode == RESULT_OK && data != null) {//图库

            //图库
            Uri selectedImage = data.getData();
            //android各个不同的系统版本,对于获取外部存储上的资源，返回的Uri对象都可能各不一样,
            // 所以要保证无论是哪个系统版本都能正确获取到图片资源的话就需要针对各种情况进行一个处理了
            //这里返回的uri情况就有点多了
            //在4.4.2之前返回的uri是:content://media/external/images/media/3951或者file://....
            // 在4.4.2返回的是content://com.android.providers.media.documents/document/image

            String pathResult = getPath(selectedImage);
            //存储--->内存
            Bitmap decodeFile = BitmapFactory.decodeFile(pathResult);
            Bitmap zoomBitmap = BitmapUtils.zoom(decodeFile, touxiang.getWidth(), touxiang.getHeight());
            //bitmap圆形裁剪
            Bitmap circleImage = BitmapUtils.circleBitmap(zoomBitmap);

            //加载显示
            touxiang.setImageBitmap(circleImage);
            //上传到服务器（省略）

            //保存到本地
            saveImage(circleImage);
        }
    }
    //将Bitmap保存到本地的操作

    /**
     * 数据的存储。（5种）
     * Bimap:内存层面的图片对象。
     * <p>
     * 存储--->内存：
     * BitmapFactory.decodeFile(String filePath);
     * BitmapFactory.decodeStream(InputStream is);
     * 内存--->存储：
     * bitmap.compress(Bitmap.CompressFormat.PNG,100,OutputStream os);
     */
    private void saveImage(Bitmap bitmap) {
        File filesDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = this.getExternalFilesDir("");

        } else {//手机内部存储
            //路径：data/data/包名/files
            filesDir = this.getFilesDir();

        }
        FileOutputStream fos = null;
        try {
            File file = new File(filesDir, "icon.png");
            fos = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 根据系统相册选择的文件获取路径
     *
     * @param uri
     */
    @SuppressLint("NewApi")
    private String getPath(Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        //高于4.4.2的版本
        if (sdkVersion >= 19) {
            Log.e("TAG", "uri auth: " + uri.getAuthority());
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(this, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(this, contentUri, selection, selectionArgs);
            } else if (isMedia(uri)) {
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualimagecursor = this.managedQuery(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                return actualimagecursor.getString(actual_image_column_index);
            }


        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * uri路径查询字段
     *
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isMedia(Uri uri) {
        return "media".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
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
