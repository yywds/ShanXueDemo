package com.example.administrator.onlinelearning.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;

@Entity
public class Student {
    @Id(autoincrement = true)
    private Long id; //标识ID
    private String sid; //账号
    private String sphone; //电话
    private String spwd; //密码
    private String sname; //昵称(用户名)
    private String snickname;//个性签名
    private String ssex;  //性别
    private String sschool; //学校
    private String sbirthday; //生日
    private String swhere;  //地区
    private String scomment; //评论
    @Generated(hash = 706003248)
    public Student(Long id, String sid, String sphone, String spwd, String sname,
            String snickname, String ssex, String sschool, String sbirthday,
            String swhere, String scomment) {
        this.id = id;
        this.sid = sid;
        this.sphone = sphone;
        this.spwd = spwd;
        this.sname = sname;
        this.snickname = snickname;
        this.ssex = ssex;
        this.sschool = sschool;
        this.sbirthday = sbirthday;
        this.swhere = swhere;
        this.scomment = scomment;
    }
    @Generated(hash = 1556870573)
    public Student() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSid() {
        return this.sid;
    }
    public void setSid(String sid) {
        this.sid = sid;
    }
    public String getSphone() {
        return this.sphone;
    }
    public void setSphone(String sphone) {
        this.sphone = sphone;
    }
    public String getSpwd() {
        return this.spwd;
    }
    public void setSpwd(String spwd) {
        this.spwd = spwd;
    }
    public String getSname() {
        return this.sname;
    }
    public void setSname(String sname) {
        this.sname = sname;
    }
    public String getSnickname() {
        return this.snickname;
    }
    public void setSnickname(String snickname) {
        this.snickname = snickname;
    }
    public String getSsex() {
        return this.ssex;
    }
    public void setSsex(String ssex) {
        this.ssex = ssex;
    }
    public String getSschool() {
        return this.sschool;
    }
    public void setSschool(String sschool) {
        this.sschool = sschool;
    }
    public String getSbirthday() {
        return this.sbirthday;
    }
    public void setSbirthday(String sbirthday) {
        this.sbirthday = sbirthday;
    }
    public String getSwhere() {
        return this.swhere;
    }
    public void setSwhere(String swhere) {
        this.swhere = swhere;
    }
    public String getScomment() {
        return this.scomment;
    }
    public void setScomment(String scomment) {
        this.scomment = scomment;
    }
    


}
