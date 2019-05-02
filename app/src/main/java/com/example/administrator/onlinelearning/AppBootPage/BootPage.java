package com.example.administrator.onlinelearning.AppBootPage;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.idst.util.NlsClient;
import com.alibaba.idst.util.SpeechSynthesizer;
import com.alibaba.idst.util.SpeechSynthesizerCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.administrator.onlinelearning.LoginAndRegister.Guide;
import com.example.administrator.onlinelearning.LoginAndRegister.LoginPage;
import com.example.administrator.onlinelearning.Page.HomeFragment;
import com.example.administrator.onlinelearning.R;


public class BootPage extends AppCompatActivity {
    private ImageView imageView;
    private SpeechSynthesizer speechSynthesizer;
    private static final String TAG = "AliSpeechDemo";
    private static final int SAMPLE_RATE = 16000;
    // 初始化播放器
    private int iMinBufSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT);
    private AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
            AudioFormat.CHANNEL_OUT_MONO
            , AudioFormat.ENCODING_PCM_16BIT,
            iMinBufSize, AudioTrack.MODE_STREAM);
    NlsClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏，这句话要写在onCreate前面，否则报错
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bootpage);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏

        imageView = findViewById(R.id.bootView);

        //获取手机屏幕的宽高
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        //2.加载网络图片，用到了图片加载框架Glide
        //在使用前要添加Glide的依赖库 implementation 'com.github.bumptech.glide:glide:4.0.0'
        //重复使用过程Glide-->with()-->load()-->into()
        String url = "https://img-blog.csdnimg.cn/2019043022083822.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzgwMDgx,size_16,color_FFFFFF,t_70";//获取网络图片的加载地址
        //拿到手机设备的宽高超出手机宽高的部分通过override裁剪掉（也就是图片手机屏幕适配）
        RequestOptions options = new RequestOptions().centerCrop().override(width, height);
        Glide.with(BootPage.this).load(url).apply(options).into(imageView);

        //倒计时
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(BootPage.this, LoginPage.class));
                finish();//销毁，防止按返回键返回到此页
            }
        }, 3000);  //3秒后跳转
        startSynthesizer();

    }

    //进行语音播放
    public void startSynthesizer() {

        // 第一步，创建client实例，client只需要创建一次，可以用它多次创建synthesizer
        client = new NlsClient();

        // 第二步，定义语音合成回调类
        SpeechSynthesizerCallback callback = new BootPage.MyCallback(audioTrack);

        // 第三步，创建语音合成对象
        speechSynthesizer = client.createSynthesizerRequest(callback);

        // 第四步，设置token和appkey
        // Token有有效期，请使用https://help.aliyun.com/document_detail/72153.html 动态生成token
        speechSynthesizer.setToken("46bf7f0fe8184ba6bb1d49c87d284bf7");
        // 请使用阿里云语音服务管控台(https://nls-portal.console.aliyun.com/)生成您的appkey
        speechSynthesizer.setAppkey("1hwXKfeK3VUGjSWm");

        // 第五步，设置相关参数，以下选项都会改变最终合成的语音效果，可以按文档调整试听效果
        // 设置人声
        speechSynthesizer.setVoice(SpeechSynthesizer.VOICE_SITONG);
        // 设置语速
        speechSynthesizer.setSpeechRate(0);

        // 设置要转为语音的文本
        speechSynthesizer.setText("欢迎使用闪学");
        //设置音量
        speechSynthesizer.setVolume(50);
        // 设置语音数据采样率
        speechSynthesizer.setSampleRate(SpeechSynthesizer.SAMPLE_RATE_16K);
        // 设置语音编码，pcm编码可以直接用audioTrack播放，其他编码不行
        speechSynthesizer.setFormat(SpeechSynthesizer.FORMAT_PCM);

        // 第六步，开始合成
        if (speechSynthesizer.start() < 0) {
            Toast.makeText(this, "启动语音合成失败！", Toast.LENGTH_LONG).show();
            speechSynthesizer.stop();
            return;
        }
        Log.d(TAG, "speechSynthesizer start done");
        // 第八步，结束合成
        speechSynthesizer.stop();
    }

    private static class MyCallback implements SpeechSynthesizerCallback {
        private AudioTrack audioTrack;

        boolean playing = false;

        public MyCallback(AudioTrack audioTrack) {
            this.audioTrack = audioTrack;
        }

        // 语音合成开始的回调
        @Override
        public void onSynthesisStarted(String msg, int code) {
            Log.d(TAG, "OnSynthesisStarted " + msg + ": " + String.valueOf(code));
        }

        // 第七步，获取音频数据的回调，在这里把音频写入播放器
        @Override
        public void onBinaryReceived(byte[] data, int code) {
            Log.i(TAG, "binary received length: " + data.length);
            if (!playing) {
                playing = true;
                audioTrack.play();
            }
            audioTrack.write(data, 0, data.length);
        }

        // 语音合成完成的回调，在这里关闭播放器
        @Override
        public void onSynthesisCompleted(final String msg, int code) {
            Log.d(TAG, "OnSynthesisCompleted " + msg + ": " + String.valueOf(code));
            audioTrack.stop();
        }

        // 调用失败的回调
        @Override
        public void onTaskFailed(String msg, int code) {
            Log.d(TAG, "OnTaskFailed " + msg + ": " + String.valueOf(code));
        }

        // 连接关闭的回调
        @Override
        public void onChannelClosed(String msg, int code) {
            Log.d(TAG, "OnChannelClosed " + msg + ": " + String.valueOf(code));
        }
    }
}
