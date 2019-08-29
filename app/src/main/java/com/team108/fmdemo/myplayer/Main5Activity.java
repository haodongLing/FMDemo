package com.team108.fmdemo.myplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.DefaultControlDispatcher;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.exoplayer2.util.Util;
import com.team108.fmdemo.R;
import com.team108.fmdemo.permission.FloatWindowManager;

import java.util.Formatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Main5Activity extends AppCompatActivity implements ExoPlayerService.OnProgressLis {
    Unbinder unbinder;
    @BindView(R.id.play)
    ImageView ivSwitch;
    @BindView(R.id.exo_progress)
    DefaultTimeBar timeBar;
    @BindView(R.id.tv_start_time)
    TextView tvStart;
    @BindView(R.id.tv_end_time)
    TextView tvEnd;
    @BindView(R.id.btn_permission)
    Button btnCheck;
    @OnClick(R.id.btn_permission)
    void onPermissionClick(){
        if (FloatWindowManager.getInstance().applyOrShowFloatWindow(this)){
            mService.show();
        }
    }
    private ExoPlayerService.MusicBinder myBinder;
    private ExoPlayerService mService;

    private Intent serviceIntent;
    private ServiceConnection serviceConnection;

    @OnClick(R.id.play)
    void onPlayClicked() {
        if (ExoPlayerManager.getDefault().isPaused) {
            ExoPlayerManager.getDefault().resumeRadio();
            ivSwitch.setImageResource(R.mipmap.icon_audio_play);
        } else {
            ExoPlayerManager.getDefault().pauseRadio();
            ivSwitch.setImageResource(R.mipmap.icon_audio_pause);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        unbinder = ButterKnife.bind(this);
        bindService();
        startService(serviceIntent);
//        initListener();
    }
    void initListener(){
        timeBar.addListener(new TimeBar.OnScrubListener() {
            @Override
            public void onScrubStart(TimeBar timeBar, long position) {

            }

            @Override
            public void onScrubMove(TimeBar timeBar, long position) {
                tvStart.setText(Util.getStringForTime(ExoPlayerManager.getDefault().getFormatBuilder()
                        , ExoPlayerManager.getDefault().getFormatter(), position));
            }

            @Override
            public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                ExoPlayerManager.getDefault().seekToTimeBarPosition(position);
            }
        });
    }


    private void bindService() {
        LogUtil.i();
        serviceIntent = new Intent(Main5Activity.this, ExoPlayerService.class);
        if(serviceConnection == null) {
            serviceConnection = new ServiceConnection() {

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    mService= ((ExoPlayerService.MusicBinder)service).getService();
                    mService.doStart("http://5.595818.com/2015/ring/000/140/6731c71dfb5c4c09a80901b65528168b.mp3");
                    initListener();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
            bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
        }
    }
    private void unbindService() {
        if(null != serviceConnection) {
            unbindService(serviceConnection);
            serviceConnection = null;
        }
    }


    @Override
    protected void onDestroy() {
        unbindService();
        super.onDestroy();
    }

//    @Override
//    protected void onPause() {
//        unbindService();
//        super.onPause();
//    }

    @Override
    protected void onResume() {
        bindService();
        super.onResume();
    }
//
//    @Override
//    protected void onRestart() {
//        bindService();
//        super.onRestart();
//    }
//
    @Override
    protected void onStop() {
        LogUtil.i();
       mService.show();
        super.onStop();
    }

    @Override
    public void onPositonChanged(long curPositionTime) {
        timeBar.setPosition(curPositionTime);
    }

    @Override
    public void onDurationChanged(long durationTime) {
        timeBar.setDuration(durationTime);
    }

    @Override
    public void onBufferedPositionChanged(long bufferedPosition) {
        timeBar.setBufferedPosition(bufferedPosition);
    }

    @Override
    public void onCurTimeStringChanged(String curTimeString) {
        tvStart.setText(curTimeString);
    }

    @Override
    public void onDurationTimeStringChanged(String durationTimeString) {
        tvEnd.setText(durationTimeString);
    }
}
