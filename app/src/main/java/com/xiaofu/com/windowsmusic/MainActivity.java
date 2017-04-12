package com.xiaofu.com.windowsmusic;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.xiaofu.com.windowsmusic.locatMusic.AudioUtils;
import com.xiaofu.com.windowsmusic.locatMusic.Song;
import com.xiaofu.com.windowsmusic.services.MusicService;

import java.util.ArrayList;

import ezy.assist.compat.SettingsCompat;


/**
 * Created by MarioStudio on 2016/5/24.
 */

public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ServiceConnection serviceConnection;
    private Intent serviceIntent;
    private MusicService.MusicBinder binder;

    private MusicService musicService;

    private ArrayList<Song> listSong;

    private boolean isHasInitDatas = false;

    private static final int LOADER_ID = 1001;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isHasInitDatas) {
            if (SettingsCompat.canDrawOverlays(this)) {
                isHasInitDatas = true;
                initDatas();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("请打开悬浮窗权限");
                builder.setPositiveButton("去打开", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 跳转到悬浮窗权限设置页
                        SettingsCompat.manageDrawOverlays(MainActivity.this);
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.show();
            }
        }
    }

    private void initDatas() {
        startService();
        getDatas();
    }

    private void startService() {
        serviceIntent = new Intent(MainActivity.this, MusicService.class);
        startService(serviceIntent);
    }

    private void getDatas() {
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void bindService() {
        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    binder = (MusicService.MusicBinder) service;
                    musicService = binder.getService();
                    binder.setSongList(listSong);
                    musicService.show();
                    moveTaskToBack(false);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
            bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    private void unbindService() {
        if (null != serviceConnection) {
            unbindService(serviceConnection);
            serviceConnection = null;
        }
    }

    private void destroyLoader() {
        getLoaderManager().destroyLoader(LOADER_ID);
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        destroyLoader();
        unbindService();
        stopService(serviceIntent);
        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return AudioUtils.getSongsLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        listSong = AudioUtils.getAllSongs(cursor);
        bindService();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
