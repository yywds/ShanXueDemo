package com.example.administrator.onlinelearning.Page.Test;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dingmouren.layoutmanagergroup.slide.ItemConfig;
import com.dingmouren.layoutmanagergroup.slide.ItemTouchHelperCallback;
import com.dingmouren.layoutmanagergroup.slide.OnSlideListener;
import com.dingmouren.layoutmanagergroup.slide.SlideLayoutManager;
import com.example.administrator.onlinelearning.CommonAndPlugin.Common;
import com.example.administrator.onlinelearning.CommonAndPlugin.VideoCache;
import com.example.administrator.onlinelearning.Page.Answer.AnswerMainActivity;
import com.example.administrator.onlinelearning.Page.home.HomeMainActivity;
import com.example.administrator.onlinelearning.Page.home.MeMainpage;
import com.example.administrator.onlinelearning.Page.home.PkMainpage;
import com.example.administrator.onlinelearning.R;
import com.kongzue.dialog.v2.MessageDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by 钉某人
 * github: https://github.com/DingMouRen
 * email: naildingmouren@gmail.com
 */

public class SlideFragment extends Fragment {
    private static final String TAG = "SlideFragment";
    @BindView(R.id.study)
    TextView study;
    @BindView(R.id.interaction)
    TextView interaction;
    @BindView(R.id.pk)
    TextView pk;
    @BindView(R.id.me)
    TextView me;
    Unbinder unbinder;
    @BindView(R.id.qiangda)
    TextView qiangda;
    @BindView(R.id.ll_main_home1)
    LinearLayout llMainHome1;
    @BindView(R.id.ll_main_home2)
    LinearLayout llMainHome2;
    @BindView(R.id.ll_main_home3)
    LinearLayout llMainHome3;
    @BindView(R.id.ll_main_home4)
    LinearLayout llMainHome4;
    @BindView(R.id.iv_main_home2)
    ImageView ivMainHome2;

    private long exitTime;
    private RecyclerView mRecyclerView;
    private SmileView mSmileView;
    private SlideLayoutManager mSlideLayoutManager;
    private ItemTouchHelper mItemTouchHelper;
    private ItemTouchHelperCallback mItemTouchHelperCallback;
    private MyAdapter mAdapter;
    private List<SlideBean> mList = new ArrayList<>();
    private int mLikeCount = 0;
    private int mDislikeCount = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
        View rootView = inflater.inflate(R.layout.fragment_slide, container, false);
        initView(rootView);
        initListener();
        initWindow();
        firstRun(); //判断是否是第一次进入此页面
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    //用于给用户提醒使用操作
    private void initDailog() {
        Common common = new Common();
        common.diaLogmohu();
        MessageDialog.show(getActivity(), "温馨提示！", "左右滑动卡片，向左滑动表示不认识单词，" + "\n" + "向右滑动表示认识单词", "好的，臣妾知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    private void initWindow() {
        //沉浸式
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //改变虚拟键盘颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getActivity().getWindow().setNavigationBarColor(Color.parseColor("#6200ED"));
        }
    }

    private void initView(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mSmileView = rootView.findViewById(R.id.smile_view);

        mSmileView.setLike(mLikeCount);
        mSmileView.setDisLike(mDislikeCount);

        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
        addData();
        mItemTouchHelperCallback = new ItemTouchHelperCallback(mRecyclerView.getAdapter(), mList);
        mItemTouchHelper = new ItemTouchHelper(mItemTouchHelperCallback);
        mSlideLayoutManager = new SlideLayoutManager(mRecyclerView, mItemTouchHelper);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(mSlideLayoutManager);

    }

    private void initListener() {
        mItemTouchHelperCallback.setOnSlideListener(new OnSlideListener() {
            @Override
            public void onSliding(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
                if (direction == ItemConfig.SLIDING_LEFT) {
                } else if (direction == ItemConfig.SLIDING_RIGHT) {
                }
            }

            @Override
            public void onSlided(RecyclerView.ViewHolder viewHolder, Object o, int direction) {
                if (direction == ItemConfig.SLIDED_LEFT) {
                    mDislikeCount++; //每滑一次加1
                    mSmileView.setDisLike(mDislikeCount);
                    mSmileView.disLikeAnimation();
                } else if (direction == ItemConfig.SLIDED_RIGHT) {
                    mLikeCount++;  //每滑一次加1
                    mSmileView.setLike(mLikeCount);
                    mSmileView.likeAnimation();
                }
                int position = viewHolder.getAdapterPosition();
                Log.e(TAG, "onSlided--position:" + position);
            }

            @Override
            public void onClear() {
                addData();
            }
        });
    }

    /**
     * 向集合中添加数据
     */
    private void addData() {
        String[] titles = {"real", "painting", "height", "funny", "live", "moose", "stadium", " "};

        String[] says = {
                "真实的; 真的; 真正的; 确实的;",
                "绘画; 油画; 作画; 涂漆; 刷油漆;",
                "(人或物的) 身高; 高; 高度;",
                " 滑稽的; 好笑的; 奇怪的; 非法的; 不诚实的;",
                " 住; 居住; 生存; 活着; (尤指在某时期) 活着;",
                " 驼鹿(产于北美; 在欧洲和亚洲称为麋鹿);",
                " 体育场; 运动场;"
        };

        int[] bgs = {
                R.mipmap.img_slide_1,
                R.mipmap.img_slide_2,
                R.mipmap.img_slide_3,
                R.mipmap.img_slide_4,
                R.mipmap.img_slide_5,
                R.mipmap.img_slide_6,
                R.mipmap.img_slide_1
        };

        String[] answer = {
                "励志名句：Be honest rather clever.",
                "励志名句：Failure is the mother of success.",
                "励志名句：pain past is pleasure",
                "励志名句：The shortest answer is doing.",
                "励志名句：Birds of a feather flock together.",
                "励志名句：I feel strongly that I can make it.",
                "励志名句：Where there is life, there is hope.",
        };

        String[] fugai = {"", "", "", "", "", "", ""};


        for (int i = 0; i < 7; i++) {
            mList.add(new SlideBean(bgs[i], titles[i], answer[i], says[i], fugai[i]));
        }
    }

    @OnClick({R.id.ll_main_home1, R.id.ll_main_home3, R.id.ll_main_home4, R.id.qiangda})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_main_home1:
                startActivity(new Intent(getActivity(), HomeMainActivity.class));
                break;
            case R.id.ll_main_home3:
                startActivity(new Intent(getActivity(), PkMainpage.class));
                break;
            case R.id.ll_main_home4:
                startActivity(new Intent(getActivity(), MeMainpage.class));
                break;
            case R.id.qiangda:
                startActivity(new Intent(getActivity(), AnswerMainActivity.class));
                break;
        }
    }

    /**
     * 适配器
     */
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(VideoCache.sContext).inflate(R.layout.item_slide, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            SlideBean bean = mList.get(position);
            holder.imgBg.setImageResource(bean.getItemBg());
            holder.tvTitle.setText(bean.getTitle());
            holder.userSay.setText(bean.getUserSay());
            holder.userAnswer.setText(bean.getmUserAnswer());
            holder.userfugai.setVisibility(View.VISIBLE);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgBg;
            TextView tvTitle;
            TextView userSay;
            TextView userAnswer;
            TextView userfugai;

            public ViewHolder(View itemView) {
                super(itemView);
                imgBg = itemView.findViewById(R.id.img_bg);
                tvTitle = itemView.findViewById(R.id.tv_title);
                userfugai = itemView.findViewById(R.id.tv_user_fugai);
                userSay = itemView.findViewById(R.id.tv_user_say);
                userAnswer = itemView.findViewById(R.id.tv_user_answer);
                userfugai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //实现淡出动画效果
                        AnimationSet aset = new AnimationSet(true);
                        AlphaAnimation aa = new AlphaAnimation(1, 0);
                        aa.setDuration(1000);
                        aset.addAnimation(aa);
                        userfugai.startAnimation(aset);
                        userfugai.setVisibility(View.GONE);

                    }
                });
            }
        }
    }


    /**
     * 1.引导页：判断APP是否是第一次启动
     */
    private void firstRun() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("FirstPage", 0);
        Boolean firstRun = sharedPreferences.getBoolean("firstpage", true);
        if (firstRun) {
            sharedPreferences.edit().putBoolean("firstpage", false).commit();
            initDailog(); //如果是第一次进入此页面，则出现对话框，
        } else {
            //否则不出现对话框

        }
    }

    //在fragment中按两次回到桌面的方法
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_BACK) {
                    //与上次点击返回键时刻作差
                    if ((System.currentTimeMillis() - exitTime) > 2000) {
                        //大于2000ms则认为是误操作，使用Toast进行提示
                        //获取布局xml
                        View tempView = LayoutInflater.from(getActivity()).inflate(R.layout.login_message, null);
                        TextView textView = (TextView) tempView.findViewById(R.id.error);
                        //添加需要显示的信息
                        textView.setText("\n" + "再按一次返回键切换" + "\n" + "到桌面" + "\n");
                        //設置alpha值（透明度）
                        tempView.getBackground().setAlpha(170);
                        Toast toast = new Toast(getActivity());
                        //显示在屏幕的中间
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        //消失到消失的時常
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(tempView);
                        toast.show();
                        //并记录下本次点击“返回键”的时刻，以便下次进行判断
                        exitTime = System.currentTimeMillis();
                    } else {
                        getActivity().finishAffinity();//杀死进程
                        //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                        System.exit(0);
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
