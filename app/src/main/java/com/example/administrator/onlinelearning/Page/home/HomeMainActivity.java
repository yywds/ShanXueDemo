package com.example.administrator.onlinelearning.Page.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.example.administrator.onlinelearning.CommonAndPlugin.Common;
import com.example.administrator.onlinelearning.CommonAndPlugin.VideoCache;
import com.example.administrator.onlinelearning.Page.Answer.AnswerMainActivity;
import com.example.administrator.onlinelearning.R;
import com.kongzue.dialog.v2.MessageDialog;
import com.kongzue.dialog.v2.Notification;
import com.mingle.widget.LoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;

import static com.kongzue.dialog.v2.Notification.TYPE_NORMAL;

public class HomeMainActivity extends AppCompatActivity {
    private static final String TAG = "ViewPagerActivity";
    @BindView(R.id.study)
    TextView study;
    @BindView(R.id.interaction)
    TextView interaction;
    @BindView(R.id.pk)
    TextView pk;
    @BindView(R.id.me)
    TextView me;
    @BindView(R.id.qiangda)
    TextView qiangda;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    MyLayoutManager2 myLayoutManager;
    Common common;
    private long exitTime;
    private int notifactionType = TYPE_NORMAL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homevideo);
        ButterKnife.bind(this);
        initWindow();//初始化窗口
        initView(); //初始化
        initListener();
        firstRun();
    }


    private void firstRun() {
        SharedPreferences sharedPreferences = getSharedPreferences("FirstPagehome", 0);
        Boolean firstRun = sharedPreferences.getBoolean("firstpagehome", true);
        if (firstRun) {
            sharedPreferences.edit().putBoolean("firstpagehome", false).commit();
            diaLogshuoming(); //如果是第一次进入此页面，则出现对话框，
        } else {
            //否则不出现对话框

        }
    }

    //对话框
    public void diaLogshuoming() {
        MessageDialog.show(this, "温馨提示", "可上下滑动视频观看！", "好的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    //初始化窗口
    private void initWindow() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //改变虚拟键盘颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setNavigationBarColor(Color.parseColor("#6200ED"));
        }
    }

    @OnClick({R.id.interaction, R.id.pk, R.id.me,R.id.qiangda}) //点击事件
    //点击事件
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.interaction:
                startActivity(new Intent(this, InteractionMainpage.class));
                break;
            case R.id.pk:
                startActivity(new Intent(this, PkMainpage.class));
                break;
            case R.id.qiangda:
                startActivity(new Intent(this, AnswerMainActivity.class));
                break;
            case R.id.me:
                startActivity(new Intent(this, MeMainpage.class));
                break;
        }
    }

    //初始化
    private void initView() {
        mRecyclerView = findViewById(R.id.recycler);
        myLayoutManager = new MyLayoutManager2(this, OrientationHelper.VERTICAL, false);
        mAdapter = new MyAdapter();
        mRecyclerView.setLayoutManager(myLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        myLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                Log.e(TAG, "释放位置:" + position + " 下一页:" + isNext);
                int index = 0;
                if (isNext) {
                    index = 0;
                } else {
                    index = 1;
                }
                releaseVideo(index);
            }

            @Override
            public void onPageSelected(int position, boolean bottom) {
                Log.e(TAG, "选择位置:" + position + " 下一页:" + bottom);
                playVideo(0);
            }
        });
    }

    //播放视频
    private void playVideo(final int position) {
        final View itemView = mRecyclerView.getChildAt(0);
        final LinearLayout linearLayout = itemView.findViewById(R.id.ll_right);
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final LinearLayout llPlay = itemView.findViewById(R.id.ll_play);
//        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        final RelativeLayout rootView = itemView.findViewById(R.id.root_view);
        final LoadingView loadView = itemView.findViewById(R.id.loadView);
        final MediaPlayer[] mediaPlayer = new MediaPlayer[1];
        videoView.start();

        //解决息屏时视频停止播放，解锁时开始播放
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);// 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);// 屏幕亮屏广播
        filter.addAction(Intent.ACTION_USER_PRESENT); // 屏幕解锁广播
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                Log.d(TAG, "onReceive");
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    Log.d(TAG, "screen on");
                    videoView.pause();
                    imgPlay.animate().alpha(1f).start();
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    Log.d(TAG, "screen off");
                    videoView.pause();
                    imgPlay.animate().alpha(1f).start();
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    Log.d(TAG, "screen unlock");
                    videoView.start();
                    imgPlay.animate().alpha(0f).start();
                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                    Log.i(TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
                }
            }
        };
        Log.d(TAG, "registerReceiver");
        registerReceiver(mBatInfoReceiver, filter);

        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    loadView.setVisibility(View.VISIBLE); //当视频在缓冲时显示加载中

                } else {
                    loadView.setVisibility(View.INVISIBLE);//否则不显示加载中
                }
                mediaPlayer[0] = mp;
                Log.e(TAG, "onInfo");
                mp.setLooping(true);
                linearLayout.animate().alpha(1).start();
//                imgThumb.animate().alpha(0).start();
                return false;
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.e(TAG, "onPrepared");
            }
        });

        llPlay.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = true;

            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    Log.e(TAG, "isPlaying:" + videoView.isPlaying());
                    imgPlay.animate().alpha(1f).start();
                    videoView.pause();
                    isPlaying = false;
                } else {
                    Log.e(TAG, "isPlaying:" + videoView.isPlaying());
                    imgPlay.animate().alpha(0f).start();
                    videoView.start();
                    isPlaying = true;
                }
            }
        });
    }

    //释放视频资源
    private void releaseVideo(int index) {
        View itemView = mRecyclerView.getChildAt(index);
        final LinearLayout linearLayout = itemView.findViewById(R.id.ll_right);
        final VideoView videoView = itemView.findViewById(R.id.video_view);
//        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        final LoadingView loadView = itemView.findViewById(R.id.loadView);
        videoView.stopPlayback();
        linearLayout.animate().alpha(0).start();
//        imgThumb.animate().alpha(1).start();
        imgPlay.animate().alpha(0f).start();
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        //拿到全局的单例 HttpProxyCacheServer
        HttpProxyCacheServer proxy = getProxy();
        //传入网络视频的url
        String proxyUrl1 = proxy.getProxyUrl("http://v.weishi.qq.com/tjg_852543821_1047_8c710acbc8d4468cad1b36d21c44vide.f20.mp4?dis_k=98e24a530cf6e0c120331eeb740faeac&dis_t=1555489041&guid=0508AFC000E081E13F01036CF26192E5&pver=5.0.4&fromtag=0&personid=h5&&wsadapt=h5_0417161721_417897836_113.3.94_2_2_2_0_0_0_0");
        String proxyUrl2 = proxy.getProxyUrl("http://v.weishi.qq.com/tjg_562665165_1047_23bac9f735f44645b43455e91be0vide.f20.mp4?dis_k=b2e21c1caf2317d79ee01e2b161827bb&dis_t=1555489118&guid=0508AFC000E081E13F01036CF26192E5&pver=5.0.4&fromtag=0&personid=h5&&wsadapt=h5_0417161838_241125571_.88.5.69_2_2_2_0_0_0_0");
        String proxyUrl3 = proxy.getProxyUrl("http://v.weishi.qq.com/tjg_1438891342_1047_af52daa6ac6f4f35af60be66867cvide.f20.mp4?dis_k=b7db5e891be9fc0240cc89ea9f2db5c8&dis_t=1555489155&guid=0508AFC000E081E13F01036CF26192E5&pver=5.0.7&fromtag=0&personid=h5&&wsadapt=h5_0417161915_808156989_88.22.43_2_2_2_0_0_0_0");
        String proxyUrl4 = proxy.getProxyUrl("http://v.weishi.qq.com/tjg_1624172294_1047_3c251b55493f47108f370c844342vide.f20.mp4?dis_k=e74cac98cd97d0a84c24608809f222d3&dis_t=1555489219&guid=0508AFC000E081E13F01036CF26192E5&pver=5.0.7&fromtag=0&personid=h5&&wsadapt=h5_0417162019_895118581_.126.252_2_2_2_0_0_0_0");
        String proxyUrl5 = proxy.getProxyUrl("http://v.weishi.qq.com/tjg_359197850_1047_09f5d7f186c748ad850e47963bddvide.f20.mp4?dis_k=85ada99766fc3e01759553d79a18a2f3&dis_t=1555489268&guid=0508AFC000E081E13F01036CF26192E5&pver=5.0.7&fromtag=0&personid=h5&&wsadapt=h5_0417162109_176186045_13.87.90_2_2_2_0_0_0_0");
        String proxyUrl6 = proxy.getProxyUrl("http://v.weishi.qq.com/tjg_2020315832_1047_12acc18e3daa4b7e96554fa7a770vide.f20.mp4?dis_k=415206df8dd3c9541c110b52590901e9&dis_t=1555489292&guid=0508AFC000E081E13F01036CF26192E5&pver=5.0.7&fromtag=0&personid=h5&&wsadapt=h5_0417162132_882882681_4.148.13_2_2_2_0_0_0_0");
        String proxyUrl7 = proxy.getProxyUrl("http://v.weishi.qq.com/tjg_1991072306_1047_6556c2ad22404b9d90fdfca6ef6cvide.f20.mp4?dis_k=ca8124dbc7416fe7906cc1c28408f590&dis_t=1555489340&guid=0508AFC000E081E13F01036CF26192E5&pver=5.0.7&fromtag=0&personid=h5&&wsadapt=h5_0417162220_116736437_8.201.13_2_2_2_0_0_0_0");
        String proxyUrl8 = proxy.getProxyUrl("http://v.weishi.qq.com/tjg_2009450713_1047_f36e043e34f04b5fbd7827dcb9ebvide.f20.mp4?dis_k=650d7d97df52cb951b791deee03aa0b9&dis_t=1556238759&guid=0508AFC000E081E13F01036CF26192E5&pver=5.0.8&fromtag=0&personid=h5&&wsadapt=h5_0426083239_679067987_8.37.123_2_2_2_0_0_0_0");
        String proxyUrl9 = proxy.getProxyUrl("http://v.weishi.qq.com/tjg_933585136_1047_694af3761a8e4d7f8d3fdb684024vide.f20.mp4?dis_k=925b7a42b8fcf58a638149de5612e87b&dis_t=1556238858&guid=0508AFC000E081E13F01036CF26192E5&pver=4.9.1&fromtag=0&personid=h5&&wsadapt=h5_0426083418_550648463_.88.4.76_2_2_2_0_0_0_0");
        String proxyUrl10 = proxy.getProxyUrl("http://v.weishi.qq.com/tjg_1105923068_1047_deeccf408b9d4084bf59b3e0c2f6vide.f20.mp4?dis_k=8a724a0c2fbccf535688e72db47741a8&dis_t=1554044918&guid=0508AFC000E081E13F01036CF26192E5&pver=4.8.0&fromtag=0&personid=h5&&wsadapt=h5_0331230838_996918286_1.56.119_2_2_2_0_0_0_0");
        String proxyUrl11 = proxy.getProxyUrl("http://v.weishi.qq.com/szg_1708164370_1047_bd883bbf745d4f198406bfa47634vide.f20.mp4?dis_k=f0e4b816f7f656c3dfcec522a379b311&dis_t=1556238901&guid=0508AFC000E081E13F01036CF26192E5&pver=4.6.8&fromtag=0&personid=h5&&wsadapt=h5_0426083501_969574202_88.53.29_2_2_2_0_0_0_0");
        String proxyUrl12 = proxy.getProxyUrl("http://v.weishi.qq.com/tjg_1905431703_1047_2c4c8a667542449a955b81d9f6f2vide.f20.mp4?dis_k=60387490feffb073227f6556ac31af03&dis_t=1556238951&guid=0508AFC000E081E13F01036CF26192E5&pver=4.9.2&fromtag=0&personid=h5&&wsadapt=h5_0426083551_814468559_8.37.123_2_2_2_0_0_0_0");
        String proxyUrl13 = proxy.getProxyUrl("http://v.weishi.qq.com/tjg_1425214155_1047_cd52ee0965674689b585eeff68a1vide.f20.mp4?dis_k=097d6eb4efb6a013f4d11238da5a23cb&dis_t=1556238989&guid=0508AFC000E081E13F01036CF26192E5&pver=5.0.7&fromtag=0&personid=h5&&wsadapt=h5_0426083629_731147759_8.52.220_2_2_2_0_0_0_0");
        String proxyUrl14 = proxy.getProxyUrl("http://v.weishi.qq.com/tjg_1324996587_1047_c3bb02915a5442ae8874a1e3184fvide.f20.mp4?dis_k=43ec8c5f24c1aea24b2f131d9cc0ff29&dis_t=1556239028&guid=0508AFC000E081E13F01036CF26192E5&pver=5.0.7&fromtag=0&personid=h5&&wsadapt=h5_0426083708_805598107_88.61.41_2_2_2_0_0_0_0");
        String proxyUrl15 = proxy.getProxyUrl("http://v.weishi.qq.com/tjg_474312700_1047_e83b0d17205343f6bda07531a3d5vide.f20.mp4?dis_k=cef4f295fd852f4756d4a1524311a8e9&dis_t=1556239075&guid=0508AFC000E081E13F01036CF26192E5&pver=5.0.7&fromtag=0&personid=h5&&wsadapt=h5_0426083755_926273390_8.52.135_2_2_2_0_0_0_0");
        String proxyUrl16 = proxy.getProxyUrl("http://v.weishi.qq.com/1047_f33205753b814536b1133aafd34bvide.f20.mp4?dis_k=9f7305ce58d9d6c88b7266ba02f62f12&dis_t=1556239124&guid=0508AFC000E081E13F01036CF26192E5&pver=4.3.1&fromtag=0&personid=h5&&wsadapt=h5_0426083844_393996545_1.62.190_2_2_2_0_0_0_0");
        String proxyUrl17 = proxy.getProxyUrl("http://v.weishi.qq.com/1047_0b6561f287e646219f9b6df62b59vide.f20.mp4?dis_k=534c9a0d11d9a670460c5528b9b46603&dis_t=1556239181&guid=0508AFC000E081E13F01036CF26192E5&pver=4.3.5&fromtag=0&personid=h5&&wsadapt=h5_0426083941_498519568_.144.162_2_2_2_0_0_0_0");

        //显示背景图片
        String imgvideoshow = "https://img-blog.csdnimg.cn/20190221200709254.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzgwMDgx,size_16,color_FFFFFF,t_70";
        //用数组来存储
        private String[] videos = {proxyUrl1, proxyUrl2, proxyUrl3, proxyUrl4, proxyUrl5, proxyUrl6,
                proxyUrl7, proxyUrl8, proxyUrl9, proxyUrl10, proxyUrl11, proxyUrl12,
                proxyUrl13, proxyUrl14, proxyUrl15, proxyUrl16, proxyUrl17};

        public MyAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pager, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
//            Glide.with(HomeMainActivity.this).load(imgvideoshow).into(holder.img_thumb); //滑动的加载背景图
            holder.videoView.setVideoURI(Uri.parse(videos[position % 17])); //滑动加载视频
        }

        @Override
        public int getItemCount() {
            return 50;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout linearLayout;
            //            ImageView img_thumb;
            VideoView videoView;
            ImageView img_play;
            RelativeLayout rootView;
            LoadingView loadView;
            ImageView dianzan;
            ImageView shoucang;
            ImageView zhuangfa;

            public ViewHolder(final View itemView) {

                super(itemView);
                linearLayout = itemView.findViewById(R.id.ll_right);
//                img_thumb = itemView.findViewById(R.id.img_thumb);
                videoView = itemView.findViewById(R.id.video_view);
                img_play = itemView.findViewById(R.id.img_play);
                rootView = itemView.findViewById(R.id.root_view);
                loadView = itemView.findViewById(R.id.loadView);
                dianzan = itemView.findViewById(R.id.im_dianzan);
                shoucang = itemView.findViewById(R.id.im_shoucang);
                zhuangfa = itemView.findViewById(R.id.im_zhuanfa);
                dianzan.setImageResource(R.mipmap.heart_icon);
                shoucang.setImageResource(R.mipmap.shoucang);

                zhuangfa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "http://v.weishi.qq.com/tjg_852543821_1047_8c710acbc8d4468cad1b36d21c44vide.f20.mp4?dis_k=98e24a530cf6e0c120331eeb740faeac&dis_t=1555489041&guid=0508AFC000E081E13F01036CF26192E5&pver=5.0.4&fromtag=0&personid=h5&&wsadapt=h5_0417161721_417897836_113.3.94_2_2_2_0_0_0_0";
                        OnekeyShare oks = new OnekeyShare();
                        //关闭sso授权
                        oks.disableSSOWhenAuthorize();
                        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
                        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
                        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用

                        //    **注意**  *这里需要自己定义一个title*
                        oks.setTitle(getString(R.string.share));

                        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                        oks.setTitleUrl(url);
                        // text是分享文本，所有平台都需要这个字段
                        oks.setText("闪学");
                        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                        // url仅在微信（包括好友和朋友圈）中使用
                        oks.setUrl(url);
                        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                        oks.setComment("闪学");
                        // site是分享此内容的网站名称，仅在QQ空间使用
                        oks.setSite(getString(R.string.app_name));
                        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                        oks.setSiteUrl(url);

                        // 启动分享GUI
                        oks.show(HomeMainActivity.this);
                    }
                });

                shoucang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shoucang.setImageResource(R.mipmap.shoucang2);
                        Notification.show(HomeMainActivity.this, 1, R.mipmap.apptubiao, getString(R.string.app_name), "收藏成功", Notification.TYPE_ERROR, notifactionType);
                    }
                });

                dianzan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dianzan.setImageResource(R.mipmap.dianzan2);
                        Notification.show(HomeMainActivity.this, 1, R.mipmap.apptubiao, getString(R.string.app_name), "点赞成功", Notification.TYPE_ERROR, notifactionType);
                    }
                });
            }
        }
    }

    //videocache
    private HttpProxyCacheServer getProxy() {
        return VideoCache.getProxy(getApplicationContext());
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