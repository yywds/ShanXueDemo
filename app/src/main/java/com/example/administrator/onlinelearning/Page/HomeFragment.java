package com.example.administrator.onlinelearning.Page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.idst.util.NlsClient;
import com.example.administrator.onlinelearning.CommonAndPlugin.Common;
import com.example.administrator.onlinelearning.CommonAndPlugin.DaoMaster;
import com.example.administrator.onlinelearning.CommonAndPlugin.DaoSession;
import com.example.administrator.onlinelearning.CommonAndPlugin.StudentDao;
import com.example.administrator.onlinelearning.Page.home.HomeMainActivity;
import com.example.administrator.onlinelearning.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment {
    DaoMaster daoMaster;
    DaoSession daoSession;
    StudentDao studentDao;
    Common common;

    private Context mContext;
//    private SpeechSynthesizer speechSynthesizer;
//    private static final String TAG = "AliSpeechDemo";
//    private static final int SAMPLE_RATE = 16000;
//    // 初始化播放器
//    private int iMinBufSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,
//            AudioFormat.CHANNEL_OUT_MONO,
//            AudioFormat.ENCODING_PCM_16BIT);
//    private AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
//            AudioFormat.CHANNEL_OUT_MONO
//            , AudioFormat.ENCODING_PCM_16BIT,
//            iMinBufSize, AudioTrack.MODE_STREAM);
    NlsClient client;
//    @BindView(R.id.bootView)
//    ImageView bootView;
//    @BindView(R.id.yyw)
//    TextView yyw;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_home, null);
        unbinder = ButterKnife.bind(this, view);
        this.mContext = getActivity();
        //沉浸式
//        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getActivity().startActivity(new Intent(getActivity(),HomeMainActivity.class));
//        initGreenDao(); //初始化GreenDao
//        firstRun(); //判断是否首次进入首页
        return view;
    }

//    //初始化GreenDao
//    public void initGreenDao() {
//        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getActivity(), "onlinelearning.db", null);
//        daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
//        daoSession = daoMaster.newSession();
//        studentDao = daoSession.getStudentDao();
//    }

//    //判断是否首次进入首页
//    private void firstRun() {
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("FirstHome", 0);
//        Boolean firstHome = sharedPreferences.getBoolean("First2", true);
//        if (firstHome) {
//            //如果是第一次进首页进行语音播报
//            sharedPreferences.edit().putBoolean("First2", false).commit();
//            startSynthesizer();
//        } else {
//            //如果不是第一次进首页,进行其他语音播报
//        }
//    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
