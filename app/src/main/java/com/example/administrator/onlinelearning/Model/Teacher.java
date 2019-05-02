package com.example.administrator.onlinelearning.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Teacher {
    @Id(autoincrement = true)
    private Long tid; //账号
    private String tphone; //电话
    private String tpwd; //密码
    private String tname; //昵称
    @Generated(hash = 2008701356)
    public Teacher(Long tid, String tphone, String tpwd, String tname) {
        this.tid = tid;
        this.tphone = tphone;
        this.tpwd = tpwd;
        this.tname = tname;
    }
    @Generated(hash = 1630413260)
    public Teacher() {
    }
    public Long getTid() {
        return this.tid;
    }
    public void setTid(Long tid) {
        this.tid = tid;
    }
    public String getTphone() {
        return this.tphone;
    }
    public void setTphone(String tphone) {
        this.tphone = tphone;
    }
    public String getTpwd() {
        return this.tpwd;
    }
    public void setTpwd(String tpwd) {
        this.tpwd = tpwd;
    }
    public String getTname() {
        return this.tname;
    }
    public void setTname(String tname) {
        this.tname = tname;
    }

}
