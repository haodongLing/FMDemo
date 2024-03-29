package com.team108.fmdemo.myplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.exoplayer2.*;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.*;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.team108.fmdemo.R;
import com.team108.fmdemo.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * description
 * 2019-05-16
 * linghailong
 */
public class Main2Activity extends AppCompatActivity
        implements RadioLIstAdapter.IOnClickRadioItemListener,
        CountryListAdapter.IOnClickCountryItemListener, Player.EventListener {
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.btn_change_country)
    Button btnChangeCountry;
    private int count = 0;
//    @BindView(R.id.pv_view)
//    PlayerView playerView;

    @OnClick(R.id.btn_change_country)
    void onChangeCountryClicked() {
        rlListCountry.setTranslationX(DisplayUtil.dip2px(this, isCountryListShow ? 100f : 0f));
        btnChangeCountry.setText(isCountryListShow ? "切换国家" : "收起");
        isCountryListShow = !isCountryListShow;
    }

    private String info[] = new String[]{"http://www.fm101.cn/zxdt/wqht/index.html", "https://tingfm.com/radio/5", "https://tingfm.com/radio/682", "https://tingfm.com/radio/3"};
    int i1 = 0;

    private static final String TAG = "lhl";

    @BindView(R.id.rl_list_country)
    RecyclerView rlListCountry;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_play)
    Button btnPlay;

    @OnClick(R.id.btn_play)
    void onPlayclicked() {
        ExoPlayerManager.getDefault().init(this, null);

    }

    @BindView(R.id.btn_last)
    Button btnLast;

    @OnClick(R.id.btn_last)
    void onLastClicked() {
        selectedIndex--;
        if (selectedIndex < 0) {
            selectedIndex = dataList.size() - 1;
        }
        selectedItem = dataList.get(selectedIndex);
        play(selectedItem);
    }

    @BindView(R.id.btn_next)
    Button btnNext;

    @OnClick(R.id.btn_next)
    void onNextClicked() {
//        selectedIndex++;
//        if (selectedIndex >= dataList.size()) {
//            selectedIndex = 0;
//        }
//        selectedItem = dataList.get(selectedIndex);
//        play(selectedItem);
        if (i1 >= info.length) {
            return;
        } else
            ExoPlayerManager.getDefault().startRadio(info[i1]);
        LogUtil.i(info[i1]);
        i1 = i1 + 1;
    }

    private void play(RadioItem2 radioItem2) {
        Log.i(TAG, "play: ");
//        if (mSimpleExoPlayer != null) {
//            mSimpleExoPlayer.stop();
//            mSimpleExoPlayer.prepare(createMediaSource(Uri.parse(radioItem2.getUrl())));
//        }
//        tvStatus.setText("正在加载频道");
    }

    @BindView(R.id.btn_rise)
    Button btnRise;

    @OnClick(R.id.btn_rise)
    void onRiseClick() {

    }

    @BindView(R.id.btn_low)
    Button btnLow;

    @OnClick(R.id.btn_low)
    void onLowClick() {

    }

    @BindView(R.id.rl_list)
    RecyclerView recyclerView;
    private List<RadioItem2> dataList = new ArrayList<>();
    private List<Integer> countryList = new ArrayList<>();
    private List<String> countryDataList = new ArrayList<>();

    private int selectedCountryIndex = 0;
    private RadioLIstAdapter adapter;
    private CountryListAdapter countryListAdapter;
    private RadioItem2 selectedItem;
    private int selectedIndex = 0;
    private boolean isCountryListShow = false;
    private boolean isReseted = false;
    /*player相关*/
    private SimpleExoPlayer mSimpleExoPlayer;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    TrackSelection.Factory trackSelectionFactory;
    // step1. 创建一个默认的TrackSelector
    // 创建带宽
    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    // 创建轨道选择工厂
//    TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
    TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
    // 创建轨道选择器实例
    TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
    // 创建解析数据的工厂
    ExtractorsFactory extractorsFactory;
    DataSource.Factory dataSourceFactory;
    private String userAgent;
    private static final CookieManager DEFAULT_COOKIE_MANAGER;

    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rlListCountry.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager2);
        checkPermissions();
    }

    private void checkPermissions() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = (String[]) permissionDeniedList.toArray();
            if (deniedPermissions != null)
                ActivityCompat.requestPermissions(this, deniedPermissions, 1603);
        } else {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1603 && permissions.length > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);

                }
            } else {
                init();
            }
        }
    }

    private void init() {
        countryDataList.add("美国");
        countryDataList.add("法国");
        countryDataList.add("德国");
        countryDataList.add("印度");
        countryDataList.add("日本");
        countryDataList.add("韩国");
        countryDataList.add("中国台湾");
        countryDataList.add("泰国");
        countryDataList.add("英国");
        countryDataList.add("越南");

        countryList.add(R.raw.fm_usa);
        countryList.add(R.raw.fm_france);
        countryList.add(R.raw.fm_germany);
        countryList.add(R.raw.fm_india);
        countryList.add(R.raw.fm_japan);
        countryList.add(R.raw.fm_korea);
        countryList.add(R.raw.fm_taiwan);
        countryList.add(R.raw.fm_thailand);
        countryList.add(R.raw.fm_uk);
        countryList.add(R.raw.fm_vietnam);


        if (countryListAdapter == null) {
            countryListAdapter = new CountryListAdapter(countryDataList);
            countryListAdapter.setOnClickItemListener(this);
            rlListCountry.setTranslationX(DisplayUtil.dip2px(this, 100F));
            countryListAdapter.notifyDataSetChanged();
            rlListCountry.setAdapter(countryListAdapter);
        }
        readLocalRadio();
        selectedItem = dataList.get(0);
        if (adapter == null) {
            adapter = new RadioLIstAdapter(dataList);
            adapter.notifyDataSetChanged();
            adapter.setOnClickItemListener(this);
            recyclerView.setAdapter(adapter);
        }
        ExoPlayerManager.getDefault().init(Main2Activity.this, "Demo");
        ExoPlayerManager.getDefault().addListener(this);
        String uri = "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3";
//        String uri = "http://zztool.qj.com/a.mp3";
        ExoPlayerManager.getDefault().startRadio(uri);
    }

    private void readLocalRadio() {
        InputStream inputStream = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            inputStream = getResources().openRawResource(countryList.get(selectedCountryIndex));
            byte[] buffer = new byte[8 * 1024];

            //读取文件内容
            int len;
            while (((len = inputStream.read(buffer)) != -1)) {
                stringBuilder.append(new String(buffer, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Gson gson = new Gson();
        ArrayList<RadioItem2> radioItems = gson.fromJson(stringBuilder.toString(), new TypeToken<ArrayList<RadioItem2>>() {
        }.getType());
        dataList.addAll(radioItems);
    }

    private void setPlayPause(boolean play) {
        mSimpleExoPlayer.setPlayWhenReady(play);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSimpleExoPlayer != null)
            mSimpleExoPlayer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mSimpleExoPlayer != null)
            mSimpleExoPlayer.release();
    }

    @Override
    public void onClickCountryItm(int index) {
        Log.i(TAG, "onClickCountryItm: ");
        selectedCountryIndex = index;
        dataList.clear();
        readLocalRadio();
        adapter.notifyDataSetChanged();
        countryListAdapter.notifyDataSetChanged();
    }

    @Override
    public int getCurrentSelectedIndex() {
        return selectedCountryIndex;
    }

    @Override
    public void onClickRadioItm(@NotNull RadioItem2 model) {
        selectedItem = model;
        selectedIndex = dataList.indexOf(model);
        play(selectedItem);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

        Log.i(TAG, "onPointerCaptureChanged: ");
    }

    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
        Log.i(TAG, "onTimelineChanged: ");

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        Log.i(TAG, "onTracksChanged: ");
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.i(TAG, "onLoadingChanged: ");
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.i(TAG, "onPlayerStateChanged: conunt" + count + "playWhenReady-->" + playWhenReady + "playbackState-->" + playbackState);
        count = count + 1;
        Log.i(TAG, "onPlayerStateChanged: ");

        if (playWhenReady && playbackState == Player.STATE_READY) {
            Log.i(TAG, "onPlayerStateChanged: --->setPlayWhenReady");
//            ExoPlayerManager.getDefault().seekTo("http://zztool.qj.com/a.mp3",0.3);
//            tvStatus.setText("正在播放");
        }
        if (playWhenReady && playbackState == Player.STATE_ENDED) {
            LogUtil.i("结束播放");
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {
        Log.i(TAG, "onRepeatModeChanged: ");
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        Log.i(TAG, "onShuffleModeEnabledChanged: ");
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.i("lhl", "onPlayerError: " + error);
        tvStatus.setText("播放失败");
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        Log.i(TAG, "onPositionDiscontinuity: +reason" + reason);
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        Log.i(TAG, "onPlaybackParametersChanged: ");
    }

    @Override
    public void onSeekProcessed() {

    }
}
