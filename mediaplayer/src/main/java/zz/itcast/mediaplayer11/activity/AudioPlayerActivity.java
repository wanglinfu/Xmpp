package zz.itcast.mediaplayer11.activity;

import java.io.File;
import java.util.List;

import zz.itcast.mediaplayer11.R;
import zz.itcast.mediaplayer11.bean.AudioItem;
import zz.itcast.mediaplayer11.lyrc.LyrcBean;
import zz.itcast.mediaplayer11.lyrc.LyrcParser;
import zz.itcast.mediaplayer11.lyrc.LyrcTextView;
import zz.itcast.mediaplayer11.service.AudioPlayerService;
import zz.itcast.mediaplayer11.service.AudioPlayerService.MyAudioServiceBinder;
import zz.itcast.mediaplayer11.utils.StringUtils;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * 音乐播放界面
 * @author wangdh
 *
 */
public class AudioPlayerActivity extends BaseActivity {
    
    protected static final int msg_update_progress = 0;
    private MyAudioPrapredBroadCastReceiver receiver;
    private ImageView audio_player_iv_pause;
    private TextView audio_player_tv_title;
    private TextView audio_player_tv_arties;
    private ImageView audio_player_wave;
    private AudioPlayerService.MyAudioServiceBinder binder;
    private AnimationDrawable drawable;
    private TextView audio_player_tv_position;
    private SeekBar audio_player_sk_position;
    
    private Handler handler = new Handler(){
      public void handleMessage(android.os.Message msg) {
          switch (msg.what) {
            case msg_update_progress://更新进度
                startUpdateProgress();
            break;
            
            default:
            break;
        }
      };  
    };
    private ImageView audio_player_iv_pre;
    private ImageView audio_player_iv_next;
    private ImageView audio_player_iv_playmode;
    private MyAudioServiceConnection conn;
    private LyrcTextView audio_player_lyricview;
    private final class OnAudioSeekBarChangeListener implements OnSeekBarChangeListener {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            
        }
        
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            
        }
        
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){//用户拖拽
                //进度改变，让MediaPlayer定位到当前进度，继续播放
                binder.seekTo(progress);
                //拖拽之后，立马更新当前进度
                //tv  00:00/00:00  当前进度/总时长
                String currentPosition = StringUtils.formatTime(binder.getCurrentPosition());
                String duration = StringUtils.formatTime(binder.getDuration());
                audio_player_tv_position.setText(currentPosition+"/"+duration);
            }
        }
    }
    /**
     * 服务连接类
     * @author wangdh
     *
     */
    class MyAudioServiceConnection implements ServiceConnection{
        
        /**
         * 连接
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MyAudioServiceBinder) service;
            //初始化播放模式按钮  (****必须在这里初始化)
            switchPlayModePic();//binder
        }
        /**
         * 断开
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            
        }
        
    }
    class MyAudioPrapredBroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //收到准备好的广播
            //变成暂停
            audio_player_iv_pause.setImageResource(R.drawable.audio_pause_selector);
            //获取当前播放的音乐信息
            AudioItem audioItem = (AudioItem) intent.getSerializableExtra("audioItem");
            //更新界面
            audio_player_tv_title.setText(audioItem.title);
            audio_player_tv_arties.setText(audioItem.artist);
            //开始播放示波器
            startWaveAnimation();
            //更新当前进度
            startUpdateProgress();
            //开始解析歌词
            startParseLyrcFile(audioItem.path);
        }
        
    }
    
    @Override
    public int getLayoutResID() {
        return R.layout.audio_player;
    }

    @Override
    public void initView() {
        //播放暂停按钮
        audio_player_iv_pause = (ImageView) findViewById(R.id.audio_player_iv_pause);
        //歌曲名称
        audio_player_tv_title = (TextView) findViewById(R.id.audio_player_tv_title);
        //演唱者
        audio_player_tv_arties = (TextView) findViewById(R.id.audio_player_tv_arties);
        audio_player_wave = (ImageView) findViewById(R.id.audio_player_wave);
        //播放进度
        audio_player_tv_position = (TextView) findViewById(R.id.audio_player_tv_position);
        audio_player_sk_position = (SeekBar) findViewById(R.id.audio_player_sk_position);
        //上一首下一首
        audio_player_iv_pre = (ImageView) findViewById(R.id.audio_player_iv_pre);
        audio_player_iv_next = (ImageView) findViewById(R.id.audio_player_iv_next);
        //播放模式
        audio_player_iv_playmode = (ImageView) findViewById(R.id.audio_player_iv_playmode);
        //歌词控件
        audio_player_lyricview = (LyrcTextView) findViewById(R.id.audio_player_lyricview);
    }
    
    @Override
    public void initListener() {
        registAudioPreparedBroadCast();
        audio_player_iv_pause.setOnClickListener(this);
        audio_player_sk_position.setOnSeekBarChangeListener(new OnAudioSeekBarChangeListener());
        //上一首下一首
        audio_player_iv_pre.setOnClickListener(this);
        audio_player_iv_next.setOnClickListener(this);
        //播放模式
        audio_player_iv_playmode.setOnClickListener(this);
    }
    

    @Override
    public void initData() {
//        Intent intent = getIntent();
//        AudioItem audioItem=(AudioItem) intent.getSerializableExtra("audioItem");
//        logI("------:"+audioItem.toString());
        //启动服务，来播放音乐
        Intent serviceIntent= new Intent(getIntent());//直接将接受的intent信息，传递到服务中
        serviceIntent.setClass(this, AudioPlayerService.class);
        startService(serviceIntent);//oncreate/onstart(播放逻辑)
        conn = new MyAudioServiceConnection();
        bindService(serviceIntent, conn, Service.BIND_AUTO_CREATE);//BIND_AUTO_CREATE，自动创建服务
//        try {
//            //播放音乐
//            MediaPlayer mediaPlayer = new MediaPlayer();
//            //1.重置
//            mediaPlayer.reset();
//            //2.设置数据源
//            mediaPlayer.setDataSource(audioItem.path);
//            //3.准备
//            mediaPlayer.prepare();//本地准备
//            //4.播放
//            mediaPlayer.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    private void registAudioPreparedBroadCast() {
        receiver = new MyAudioPrapredBroadCastReceiver();
        IntentFilter filter = new IntentFilter(AudioPlayerService.ACTION_AUDIO_PREPARED);
        registerReceiver(receiver, filter );
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiver!=null){
            unregisterReceiver(receiver);
        }
        //解绑服务
        if(conn!=null){
            unbindService(conn);
        }
        handler.removeCallbacksAndMessages(null);
    }
    /**
     * 开启示波器动画
     */
    public void startWaveAnimation() {
        drawable = (AnimationDrawable) audio_player_wave.getDrawable();
        drawable.start();//开启播放
    }
    
    private void stopWaveAnimation() {
        if (drawable != null)
            drawable.stop();
        
    }
    @Override
    public void onInnerClick(View v) {
        super.onInnerClick(v);
        switch (v.getId()) {
            case R.id.audio_player_iv_pause://播放和暂停
                switchPlayAndPause();
            break;
            case R.id.audio_player_iv_pre://上一首
                binder.playPre();
            break;
            case R.id.audio_player_iv_next://下一首
                binder.playNext();
            break;
            case R.id.audio_player_iv_playmode://播放模式
                switchPlayMode();
            break;
            default:
            break;
        }
    }

    private void switchPlayAndPause() {
        binder.switchPlayAndPause();
        switchPlayAndPausePic();
    }



    private void switchPlayAndPausePic() {
        //如果是播放，暂停
        if(binder.isPlaying()){//播放逻辑
            //示波器开始播放
            startWaveAnimation();
            startUpdateProgress();
            //暂停按钮
            audio_player_iv_pause.setImageResource(R.drawable.audio_pause_selector);
        }else{//暂停逻辑
            stopWaveAnimation();
            handler.removeMessages(msg_update_progress);
            //播放按钮
            audio_player_iv_pause.setImageResource(R.drawable.audio_play_selector);
        }
        
    }
    /**
     * 开始更新当前进度
     */
    public void startUpdateProgress() {
        //tv  00:00/00:00  当前进度/总时长
        String currentPosition = StringUtils.formatTime(binder.getCurrentPosition());
        String duration = StringUtils.formatTime(binder.getDuration());
        audio_player_tv_position.setText(currentPosition+"/"+duration);
        //sk
        audio_player_sk_position.setMax(binder.getDuration());
        audio_player_sk_position.setProgress(binder.getCurrentPosition());
        logI("-----ssssssssssaaaaaaaaa"+binder.getCurrentPosition());
        //更新当前行
        audio_player_lyricview.updateCurrentLine(binder.getCurrentPosition());
        //发送之前，先移除（避免，下一首切换之后，handler重复发生msg）
        handler.removeMessages(msg_update_progress);
        //不断更新
        handler.sendEmptyMessageDelayed(msg_update_progress, 500);
    }
    /**播放模式
     * 
     */
    private void switchPlayMode() {
        binder.switchPlayMode();
        switchPlayModePic();
    }
    private void switchPlayModePic() {
        switch (binder.getCurrentPlayMode()) {
            case AudioPlayerService.play_mode_repeat:
                audio_player_iv_playmode.setImageResource(R.drawable.audio_playmode_allrepeat_selector);
            break;
            case AudioPlayerService.play_mode_single:
            audio_player_iv_playmode.setImageResource(R.drawable.audio_playmode_singlerepeat_selector);
            break;
            case AudioPlayerService.play_mode_random:
            audio_player_iv_playmode.setImageResource(R.drawable.audio_playmode_random_selector);
            break;
            
            default:
            break;
        }
        
    }

    /**
     * 解析歌词
     * 
     * @param path 歌曲路径
     */
    public void startParseLyrcFile(String path) {
        String lyrcPath = path.substring(0, path.lastIndexOf("."))+".lrc";
        
        File lyrcFile = new File(lyrcPath);
        //如果lrc后缀的歌词文件不存在，尝试使用.txt
        if(!lyrcFile.exists()){
            lyrcPath = path.substring(0, path.lastIndexOf("."))+".txt";
            lyrcFile = new File(lyrcPath);
        }
        List<LyrcBean> lyrcBeans = LyrcParser.parseLyrcFile(lyrcFile);
        //给自定义控件传递歌词集合数据
        audio_player_lyricview.setLyrcBeans(lyrcBeans);
        logI("解析结果："+lyrcBeans.toString());
        
    }

    
}
