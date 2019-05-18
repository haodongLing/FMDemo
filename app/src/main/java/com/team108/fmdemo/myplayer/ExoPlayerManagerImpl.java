package com.team108.fmdemo.myplayer;

import android.net.Uri;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.team108.fmdemo.RadioItem2;
import com.team108.fmdemo.database.DatabaseProvider;
import com.team108.zzfm.model.response.RadioItem;

import java.io.File;

/**
 * description
 * 2019-05-17
 * linghailong
 */
public class ExoPlayerManagerImpl extends ExoPlayerManager {
    private DatabaseProvider databaseProvider;
    private File downloadDirectory;
    private Cache downloadCache;
    private DownloadManager downloadManager;
    private DownloadTracker downloadTracker;

    @Override
    public void addListener(Player.EventListener listener) {
        if (checkExoPlayerIsInited())
            mSimpleExoPlayer.addListener(listener);
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
            mSimpleExoPlayer.prepare(createMediaSource(Uri.parse(responseRadio.getVoiceUrl())));
            mSimpleExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void startRadio(RadioItem2 radioItem2) {
        if (checkExoPlayerIsInited()) {
            mSimpleExoPlayer.stop(true);
            mSimpleExoPlayer.prepare(createMediaSource(Uri.parse(radioItem2.getUrl())));
            mSimpleExoPlayer.setPlayWhenReady(true);
        }
    }


    @Override
    public void stopRedio() {
        if (checkExoPlayerIsInited())
            mSimpleExoPlayer.stop();
    }

    @Override
    public void pauseRedio() {
        if (checkExoPlayerIsInited())
            mSimpleExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void resumeRedio() {
        if (checkExoPlayerIsInited()) {
            mSimpleExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public boolean checkExoPlayerIsInited() {
        return mSimpleExoPlayer != null;
    }

    private MediaSource createMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

}
