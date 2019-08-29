package com.team108.fmdemo.myplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.PluralRules;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultControlDispatcher;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.exoplayer2.util.Util;
import com.team108.fmdemo.R;

import java.util.Formatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Main4Activity extends AppCompatActivity {
    Unbinder unbinder;
    @BindView(R.id.play)
    ImageView ivSwitch;
    @BindView(R.id.exo_progress)
    DefaultTimeBar timeBar;
    @BindView(R.id.tv_start_time)
    TextView tvStart;
    @BindView(R.id.tv_end_time)
    TextView tvEnd;
    private StringBuilder formatBuilder;
    private Formatter formatter;
    private Timeline.Window window;
    private Handler mHandler;
    private DefaultControlDispatcher controlDispatcher;
    private Timeline.Period period;
    private ExoPlayerService.MusicBinder myBinder;

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
        mHandler = new Handler();
        controlDispatcher = new DefaultControlDispatcher();
        period = new Timeline.Period();
        window = new Timeline.Window();
        ExoPlayerManager.getDefault().init(this, "");

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
        ExoPlayerManager.getDefault().addMediaListener(new ExoPlayerManager.MediaControlListener() {
            @Override
            public void setCurPositionTime(long curPositionTime) {
                timeBar.setPosition(curPositionTime);
            }

            @Override
            public void setDurationTime(long durationTime) {
                timeBar.setDuration(durationTime);
            }

            @Override
            public void setBufferedPositionTime(long bufferedPosition) {
                timeBar.setBufferedPosition(bufferedPosition);
            }

            @Override
            public void setCurTimeString(String curTimeString) {
                tvStart.setText(curTimeString);
            }

            @Override
            public void setDurationTimeString(String durationTimeString) {
                tvEnd.setText(durationTimeString);
            }
        });
        ExoPlayerManager.getDefault().addListener(null);

    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
