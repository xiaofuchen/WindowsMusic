package com.xiaofu.com.windowsmusic;

import android.app.Application;

import com.google.gson.Gson;

/**
 * Created by XiaoFu on 2017/3/18.
 */

public class MusicApp extends Application{
    private static Gson mGson;
    private static MusicApp mApp;
    public static MusicApp getInstance(){
        return mApp;
    }
    public static synchronized Gson getGson(){
        if(null == mGson){
            mGson = new Gson();
        }
        return mGson;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }
}
