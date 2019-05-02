package com.example.administrator.onlinelearning.Page.Test;


/**
 * Created by 钉某人
 * github: https://github.com/DingMouRen
 * email: naildingmouren@gmail.com
 */


public class SlideBean {
    private int mItemBg;
    private String mTitle;
    private String mUserSay;
    private String mUserAnswer;
    private String getmUserfugai;

    public SlideBean(int mItemBg, String mTitle, String mUserSay, String mUserAnswer, String getmUserfugai) {
        this.mItemBg = mItemBg;
        this.mTitle = mTitle;
        this.mUserSay = mUserSay;
        this.mUserAnswer = mUserAnswer;
        this.getmUserfugai = getmUserfugai;
    }

    public int getItemBg() {
        return mItemBg;
    }

    public void setItemBg(int mItemBg) {
        this.mItemBg = mItemBg;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getUserSay() {
        return mUserSay;
    }

    public void setUserSay(String mUserSay) {
        this.mUserSay = mUserSay;
    }

    public String getmUserAnswer() {
        return mUserAnswer;
    }

    public void setmUserAnswer(String mUserAnswer) {
        this.mUserAnswer = mUserAnswer;
    }

    public String getGetmUserfugai() {
        return getmUserfugai;
    }

    public void setGetmUserfugai(String getmUserfugai) {
        this.getmUserfugai = getmUserfugai;
    }

}
