package com.team108.fmdemo.myplayer;

import android.net.Uri;
import android.util.Log;
import androidx.annotation.Nullable;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.util.Util;
import com.team108.fmdemo.RadioItem2;
import com.team108.zzfm.model.response.RadioItem;

import java.io.File;

/**
 * description
 * 2019-05-17
 * linghailong
 */
public class ExoPlayerManagerImpl extends ExoPlayerManager {
    private static final String TAG = "lhl->ExoPlayerManager";
    /*video长度*/
    private long videoDuration;
    private String currentUri;
    /*判断当前fm是否正在播放*/
    private long currentPosition;

    @Override
    public void addListener(Player.EventListener listener) {
        if (checkExoPlayerIsInited())
            mSimpleExoPlayer.addListener(listener);
    }

    @Override
    public boolean getIsPlaying() {
        return mSimpleExoPlayer.getPlayWhenReady();
    }


    @Override
    public void releasePlayer() {
        if (checkExoPlayerIsInited()) {
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
            trackSelector = null;
        }
        if (mContext != null) {
            mContext = null;
        }
    }

    @Override
    public void startRadio(RadioItem responseRadio) {
        if (checkExoPlayerIsInited()) {
            mSimpleExoPlayer.stop(true);
            mSimpleExoPlayer.prepare(buildMediaSource(Uri.parse(responseRadio.getVoiceUrl())));
            mSimpleExoPlayer.setPlayWhenReady(true);
            currentUri = responseRadio.getVoiceUrl();
        }
    }

    @Override
    public void startRadio(RadioItem2 radioItem2) {
        if (checkExoPlayerIsInited()) {
            mSimpleExoPlayer.stop(true);
            mSimpleExoPlayer.prepare(buildMediaSource(Uri.parse(radioItem2.getUrl())));
            mSimpleExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void startRadio(String uri) {
        if (checkExoPlayerIsInited()) {
            mSimpleExoPlayer.stop(true);
            mSimpleExoPlayer.prepare(buildMediaSource(Uri.parse(uri)));
            mSimpleExoPlayer.setPlayWhenReady(true);
            currentUri = uri;
        }
    }

    @Override
    public void stopRadio() {
        if (checkExoPlayerIsInited()) {
            mSimpleExoPlayer.stop();
        }

    }

    @Override
    public void seekTo(double percent) {
        if (checkExoPlayerIsInited()) {
            videoDuration = mSimpleExoPlayer.getDuration();
            Log.i(TAG, "seekTo: " + videoDuration);
            Log.i(TAG, "position" + (long) (videoDuration * percent));
            mSimpleExoPlayer.seekTo((long) (videoDuration * percent));
        }
    }

    @Override
    public void resumeOrPauseRadio() {
        if (checkExoPlayerIsInited()) {
            if (mSimpleExoPlayer.getPlayWhenReady()) {
                currentPosition = mSimpleExoPlayer.getCurrentPosition();
                mSimpleExoPlayer.setPlayWhenReady(false);
            } else {
                mSimpleExoPlayer.setPlayWhenReady(true);
                mSimpleExoPlayer.seekTo(currentPosition);
                currentPosition = 0;
            }
        }
    }

    @Override
    public void seekTo(String uri, double percent) {
        if (uri.equals(currentUri)) {
            videoDuration = mSimpleExoPlayer.getDuration();
            mSimpleExoPlayer.seekTo((long) (videoDuration * percent));
        }
    }

    @Override
    public boolean checkExoPlayerIsInited() {
        return mSimpleExoPlayer != null;
    }

    private MediaSource buildMediaSource(Uri uri) {
        return buildMediaSource(uri, null);
    }

    private MediaSource buildMediaSource(Uri uri, @Nullable String overrideExtension) {
        @C.ContentType int type = Util.inferContentType(uri, overrideExtension);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }
//    private MediaSource createMediaSource(Uri uri) {
//        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
//    }

}
