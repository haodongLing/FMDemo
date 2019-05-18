package com.team108.fmdemo.myplayer;

import android.content.Context;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.team108.fmdemo.RadioItem2;
import com.team108.zzfm.model.response.RadioItem;

/**
 * description
 * 2019-05-17
 * linghailong
 */
public abstract class ExoPlayerManager {
    protected Context mContext;
    protected SimpleExoPlayer mSimpleExoPlayer;
    protected TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
    // 创建轨道选择器实例
    protected TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
    protected DataSource.Factory dataSourceFactory;
    private String userAgent;
    private static final class Holder {
        private static final ExoPlayerManager sInstance = new ExoPlayerManagerImpl();
    }

    public static ExoPlayerManager getDefault() {
        return Holder.sInstance;
    }

    /**
     * @param pContext        A valid context of the calling application.
     * @param applicationName String that will be prefix'ed to the generated user agent.
     */
    public void init(Context pContext, String applicationName) {
        /*如果mContext!=null,那么说明已经实例化*/
        if (mContext != null) {
            return;
        }
        mContext = pContext;
        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
        userAgent = Util.getUserAgent(mContext, applicationName.replace("ExoPlayerLib", "Blah"));
        dataSourceFactory=new DefaultDataSourceFactory(mContext, userAgent, new TransferListener() {
            @Override
            public void onTransferInitializing(DataSource source, DataSpec dataSpec, boolean isNetwork) {

            }

            @Override
            public void onTransferStart(DataSource source, DataSpec dataSpec, boolean isNetwork) {

            }

            @Override
            public void onBytesTransferred(DataSource source, DataSpec dataSpec, boolean isNetwork, int bytesTransferred) {

            }

            @Override
            public void onTransferEnd(DataSource source, DataSpec dataSpec, boolean isNetwork) {

            }
        });
    }

    /**
     * 添加Player的listener
     * @param listener
     */
    public abstract void addListener(Player.EventListener listener);

    /**
     * 释放Player
     */
    public abstract void releasePlayer();

    /**
     * 开始播放
     * @param responseRadio
     */
    public abstract void startRadio(RadioItem responseRadio);

    /**
     * 测试方法，不用管
     * @param radioItem2
     */
    public abstract void startRadio(RadioItem2 radioItem2);

    /**
     * 停止
     */
    public abstract void stopRedio();
    public abstract void pauseRedio();
    public abstract void resumeRedio();

    /**
     * 检查当亲Player是否被实例化
     * @return
     */
    public abstract boolean checkExoPlayerIsInited();
}
