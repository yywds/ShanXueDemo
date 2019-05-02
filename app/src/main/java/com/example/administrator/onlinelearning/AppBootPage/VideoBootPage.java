package com.example.administrator.onlinelearning.AppBootPage;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.danikula.videocache.HttpProxyCacheServer;
import com.example.administrator.onlinelearning.CommonAndPlugin.CustomVideoView;
import com.example.administrator.onlinelearning.CommonAndPlugin.VideoCache;
import com.example.administrator.onlinelearning.LoginAndRegister.Guide;
import com.example.administrator.onlinelearning.R;


public class VideoBootPage extends AppCompatActivity {
    private CustomVideoView videoview;
    private ImageView imageView;
    private MediaPlayer mediaPlayer;
    boolean isChanged = true;
     String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏，这句话要写在onCreate前面，否则报错
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoview = findViewById(R.id.videoview);
        imageView =  findViewById(R.id.imageView);

        initView();//初始化
        onClickImageView();

    }

    //点击声音图标来回切换，实现引导页视频声音的静音和播放效果
    private void onClickImageView() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer !=null) {
                    //切换图片并恢复声音
                    if (isChanged) {
                        imageView.setImageResource(R.drawable.bofang);
                        mediaPlayer.setVolume(1f,1f);
                    }
                    //切换图标并静音
                    else {
                        imageView.setImageResource(R.drawable.jingyin);
                        mediaPlayer.setVolume(0f, 0f);
                    }
                    isChanged = !isChanged;
                }
            }
        });
    }

    /**
     * 初始化
     */
    private void initView() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏播放
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//虚拟键盘透明化
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);//隐藏虚拟按键

        //设置本地视频播放加载路径
        //videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));

        //拿到全局的单例 HttpProxyCacheServer
        HttpProxyCacheServer proxy = getProxy();
        //传入网络视频的url
        String proxyUrl = proxy.getProxyUrl("http://v.weishi.qq.com/tjg_2002277561_1047_e9e1794c217a4b788ca1aecb4a00vide.f20.mp4?dis_k=6ab9d0dc437a07ceb51d568edbfa36b1&dis_t=1556630861&guid=0508AFC000E081E13F01036CF26192E5&pver=5.2.4&fromtag=0&personid=h5&&wsadapt=h5_0430212742_374746308_8.52.135_2_2_2_0_0_0_0");
        videoview.setVideoPath(proxyUrl); //给videoview设置proxyUrl
       
        //播放时静音
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer=mp;//用系统私有成员medioPlayer获取videoview的MediaPlayer
                // ，这样控制声音才有效果，这步很重要
                mp.setVolume(0f,0f);
            }
        });

                videoview.start();//播放

        //播放结束
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                startActivity(new Intent(VideoBootPage.this,Guide.class));
                finish();
            //    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//播放结束退出全屏
            }
        });
    }

    private HttpProxyCacheServer getProxy() {
        return VideoCache.getProxy(getApplicationContext());
    }

}
