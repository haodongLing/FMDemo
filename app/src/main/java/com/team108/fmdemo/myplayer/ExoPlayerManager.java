package com.team108.fmdemo.myplayer;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
 * description:
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
    private String userAgent="exoplayer-codelab";
    protected ExoPlayerManager(){}
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
    public void init(@NonNull Context pContext, @Nullable String applicationName) {
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
    public abstract boolean getIsPlaying();

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

    public abstract void startRadio(String uri);
    /**
     * 停止
     */
    public abstract void stopRadio();
    /**
     *
     * @param percent:指定从整体的百分之几开始播放
     */
    public abstract void seekTo(double percent);
    /**
     *
     * @param uri 判断当前连接是否是想要从中间播放的那条链接
     * @param percent 指定从整体的百分之几开始播放
     */
    public abstract void seekTo(String uri,double percent);
    /**
     * 重启
     */
    public abstract void resumeRadio();

    /**
     * 暂停
     */
    public abstract void pauseRadio();

    /**
     * 检查当亲Player是否被实例化
     * @return
     */
    public abstract boolean checkExoPlayerIsInited();
    public abstract void risePlayer();
    public abstract void lowPlayer();
}
