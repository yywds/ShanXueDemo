package com.example.administrator.onlinelearning.CommonAndPlugin;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.mob.MobSDK;

//3.利用videocache视频缓存插件实现视频的缓存
public class VideoCache extends Application {
    private HttpProxyCacheServer proxy;
    public static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        MobSDK.init(this);
    }
    public static HttpProxyCacheServer getProxy(Context context) {
        VideoCache app = (VideoCache) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }
    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }
}

