package com.team108.fmdemo.myplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.Player;
import com.team108.fmdemo.R;
import com.team108.fmdemo.permission.AVCallFloatView;
import com.team108.fmdemo.permission.FloatWindowManager;

/**
 * created by linghaoDo on 2019-08-25
 * <p>
 * description:
 */
public class ExoPlayerService extends Service implements FloatWindowManager.OnWindowLis {
    private static final String TAG = "lhl-->ExoPlayerService";
    private View mFloatingView;
    private ImageView ivCover;
    private CircleTimeBar timeBar;
    private ImageView ivClose;
    private FrameLayout layout_floating;
    private boolean isWindowDismiss = true;
    private WindowManager windowManager = null;
    private WindowManager.LayoutParams mParams = null;
    private AVCallFloatView floatView = null;
    private OnProgressLis onProgressLis;

    public OnProgressLis getOnProgressLis() {
        return onProgressLis;
    }

    public void setOnProgressLis(OnProgressLis onProgressLis) {
        this.onProgressLis = onProgressLis;
    }

    public class MusicBinder extends Binder {
        public ExoPlayerService getService() {
            return ExoPlayerService.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.i("执行了onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i("onCreate");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        ExoPlayerManager.getDefault().init(this, "ss");
        ExoPlayerManager.getDefault().addMediaListener(new ExoPlayerManager.MediaControlListener() {
            @Override
            public void setCurPositionTime(long curPositionTime) {
                if (timeBar != null)
                    timeBar.setPosition(curPositionTime);
                if (onProgressLis != null) {
                    onProgressLis.onBufferedPositionChanged(curPositionTime);
                }
            }

            @Override
            public void setDurationTime(long durationTime) {
                if (timeBar != null)
                    timeBar.setDuration(durationTime);
                if (onProgressLis != null)
                    onProgressLis.onDurationChanged(durationTime);
            }

            @Override
            public void setBufferedPositionTime(long bufferedPosition) {
                if (onProgressLis != null) {
                    onProgressLis.onBufferedPositionChanged(bufferedPosition);
                }

            }

            @Override
            public void setCurTimeString(String curTimeString) {
                if (onProgressLis != null) {
                    onProgressLis.onCurTimeStringChanged(curTimeString);
                }
            }

            @Override
            public void setDurationTimeString(String durationTimeString) {
                if (onProgressLis != null) {
                    onProgressLis.onDurationTimeStringChanged(durationTimeString);
                }
            }
        });
        ExoPlayerManager.getDefault().addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady) {
                    switch (playbackState) {
                        case Player.STATE_READY:
                            if (ivCover != null)
                                ivCover.setImageResource(R.mipmap.image_01);
                            break;
                        case Player.STATE_ENDED:
                            ExoPlayerManager.getDefault().reStart();
                            break;
                    }
                }
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                if (isLoading) {
                    ExoPlayerManager.getDefault().startListenProgress();
                }
            }
        });
        return new MusicBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        ExoPlayerManager.getDefault().releasePlayer();
        return super.onUnbind(intent);
    }

    public void initListener(Player.EventListener eventListener, ExoPlayerManager.MediaControlListener mediaControlListener) {
        ExoPlayerManager.getDefault().addListener(eventListener);
        ExoPlayerManager.getDefault().addMediaListener(mediaControlListener);
    }

    public void doPause() {
        ExoPlayerManager.getDefault().pauseRadio();
    }

    public void doStart(String uri) {
        ExoPlayerManager.getDefault().startRadio(uri);
    }

    public void doResume() {
        ExoPlayerManager.getDefault().resumeRadio();
    }

    public void doStop() {
        ExoPlayerManager.getDefault().stopRadio();
    }

    public void seekToTimeBarPosition(long positionMs) {
        ExoPlayerManager.getDefault().seekToTimeBarPosition(positionMs);
    }

    public void show() {
        initFloatingWindow();
        ExoPlayerManager.getDefault().addMediaListener(new ExoPlayerManager.MediaControlListener() {
            @Override
            public void setCurPositionTime(long curPositionTime) {
                if (timeBar != null)
                    timeBar.setPosition(curPositionTime);
                if (onProgressLis != null) {
                    onProgressLis.onBufferedPositionChanged(curPositionTime);
                }
            }

            @Override
            public void setDurationTime(long durationTime) {
                if (timeBar != null)
                    timeBar.setDuration(durationTime);
                if (onProgressLis != null)
                    onProgressLis.onDurationChanged(durationTime);
            }

            @Override
            public void setBufferedPositionTime(long bufferedPosition) {
                if (onProgressLis != null) {
                    onProgressLis.onBufferedPositionChanged(bufferedPosition);
                }

            }

            @Override
            public void setCurTimeString(String curTimeString) {
                if (onProgressLis != null) {
                    onProgressLis.onCurTimeStringChanged(curTimeString);
                }
            }

            @Override
            public void setDurationTimeString(String durationTimeString) {
                if (onProgressLis != null) {
                    onProgressLis.onDurationTimeStringChanged(durationTimeString);
                }
            }
        });
        ExoPlayerManager.getDefault().addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady) {
                    switch (playbackState) {
                        case Player.STATE_READY:
                            if (ivCover != null)
                                ivCover.setImageResource(R.mipmap.image_01);
                            break;
                        case Player.STATE_ENDED:
                            ExoPlayerManager.getDefault().reStart();
                            break;
                    }
                }
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                if (isLoading) {
                    ExoPlayerManager.getDefault().startListenProgress();
                }
            }
        });
    }

    public interface OnProgressLis {
        void onPositonChanged(long curPositionTime);

        void onDurationChanged(long durationTime);

        void onBufferedPositionChanged(long bufferedPosition);

        void onCurTimeStringChanged(String curTimeString);

        void onDurationTimeStringChanged(String durationTimeString);
    }


    @Override
    public void onDestroy() {
        ExoPlayerManager.getDefault().releasePlayer();
        super.onDestroy();
    }

    private void initFloatingWindow() {
        if (!isWindowDismiss) {
            Log.e(TAG, "view is already added here");
            return;
        }

        isWindowDismiss = false;
        if (windowManager == null) {
            windowManager = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }

        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        mParams = new WindowManager.LayoutParams();
        mParams.packageName = this.getPackageName();
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        int mType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mType = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        }
        mParams.type = mType;
        mParams.format = PixelFormat.RGBA_8888;
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        mParams.x = screenWidth - dp2px(this, 100);
        mParams.y = screenHeight - dp2px(this, 171);
        floatView = new AVCallFloatView(this.getApplicationContext());
        floatView.setParams(mParams);
        floatView.setIsShowing(true);
        /*init*/
        ivClose = floatView.findViewById(R.id.iv_stop);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i("ivClose-->onClick");
            }
        });
        ivCover = floatView.findViewById(R.id.iv_cover);
        layout_floating = floatView.findViewById(R.id.layout_floating);
        timeBar = floatView.findViewById(R.id.circle_time_bar);
        windowManager.addView(floatView, mParams);
    }

    @Override
    public void showWindow() {
        show();
    }

    @Override
    public void dismissWindow() {
        if (isWindowDismiss) {
            Log.e(TAG, "window can not be dismiss cause it has not been added");
            return;
        }

        isWindowDismiss = true;
        floatView.setIsShowing(false);
        if (windowManager != null && floatView != null) {
            windowManager.removeViewImmediate(floatView);
        }
    }

    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
