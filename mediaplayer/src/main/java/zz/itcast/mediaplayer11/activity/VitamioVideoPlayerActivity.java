//package zz.itcast.mediaplayer11.activity;
//
//import io.vov.vitamio.MediaPlayer;
//import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
//import io.vov.vitamio.MediaPlayer.OnCompletionListener;
//import io.vov.vitamio.MediaPlayer.OnErrorListener;
//import io.vov.vitamio.MediaPlayer.OnPreparedListener;
//import io.vov.vitamio.widget.VideoView;
//
//import java.util.ArrayList;
//
//import zz.itcast.mediaplayer11.R;
//import zz.itcast.mediaplayer11.bean.VideoItem;
//import zz.itcast.mediaplayer11.utils.StringUtils;
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.media.AudioManager;
//import android.net.Uri;
//import android.os.Handler;
//import android.view.GestureDetector;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.SeekBar;
//import android.widget.SeekBar.OnSeekBarChangeListener;
//import android.widget.TextView;
//
//import com.nineoldandroids.view.ViewHelper;
//import com.nineoldandroids.view.ViewPropertyAnimator;
//
///**
// * 视频播放界面
// * @author wangdh
// *
// */
//public class VitamioVideoPlayerActivity extends BaseActivity {
//    
//    private final class OnVideoErrorListener implements OnErrorListener {
//        @Override
//        public boolean onError(MediaPlayer mp, int what, int extra) {
//            AlertDialog.Builder builder = new Builder(VitamioVideoPlayerActivity.this);
//            builder.setTitle("提示");
//            builder.setMessage("十分抱歉，不能播放此视频");
//            builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
//            {
//                
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                }
//            });
//            builder.show();
//            return false;
//        }
//    }
//    private final class OnVideoCompletionListener implements OnCompletionListener {
//        @Override
//        public void onCompletion(MediaPlayer mp) {
//            //更新当前进度，为总时长
//            video_player_tv_position.setText(video_player_tv_duration.getText());
//            video_player_sk_position.setProgress(video_player_sk_position.getMax());
//            //移除更新当前进度的handler消息
//            handler.removeMessages(msg_update_progress);
//            
//        }
//    }
//    private final class OnVideoBufferingUpdateListener implements OnBufferingUpdateListener {
//        /**
//         * 缓冲更新回调
//         * percent：当前缓冲的进度百分比
//         */
//        @Override
//        public void onBufferingUpdate(MediaPlayer mp, int percent) {
//            //更新seekbar的第二进度
//            int buffer = (int) (percent*video_player_videoview.getDuration());
//            logI("缓冲进度："+percent);
//            video_player_sk_position.setSecondaryProgress(buffer);
//        }
//    }
//    private final class SimpleOnVideoGestureListener extends GestureDetector.SimpleOnGestureListener {
//        /**
//         * 长按事件
//         */
//        @Override
//        public void onLongPress(MotionEvent e) {
//            super.onLongPress(e);
//            //长按，切换暂停和播放
//            switchPlayAndPause();
//        }
//        /**
//         * 双击
//         */
//        @Override
//        public boolean onDoubleTap(MotionEvent e) {
////            logI("onDoubleTap");
//            switchFullScreen();
//            return super.onDoubleTap(e);
//        }
//        /**
//         * 双击执行过程：事件
//         */
//        @Override
//        public boolean onDoubleTapEvent(MotionEvent e) {
////            logI("onDoubleTapEvent");
//            return super.onDoubleTapEvent(e);
//        }
//        /**
//         * 单击事件
//         */
//        @Override
//        public boolean onSingleTapConfirmed(MotionEvent e) {
//            showOrHideControl();
//            return super.onSingleTapConfirmed(e);
//        }
//        /**
//         * 一次up事件发生
//         */
//        @Override
//        public boolean onSingleTapUp(MotionEvent e) {
//            return super.onSingleTapUp(e);
//        }
//    }
//    private final class OnVideoSeekBarChangeListener implements OnSeekBarChangeListener {
//        /**
//         * 开始拖拽进度
//         */
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar) {
//            //移除自动隐藏
//            handler.removeMessages(msg_hide_control);
//        }
//        /**
//         * 拖拽进度完毕
//         */
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar) {
//            //再次自动隐藏
//            handler.sendEmptyMessageDelayed(msg_hide_control, 2000);
//        }
//        /**
//         * 进度发送改变
//         * progress：改变后的进度值
//         * fromUser: 判断是否是用户拖拽
//         */
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            if(seekBar.getId() == R.id.video_player_sk_volume){
//                //改变系统当前音量(index：当前音量值,flag:1显示系统改变音量对话框)
//                setSystemVolume(progress);
//            }else{
//                if(fromUser){//代码改变不会执行
////                    logI("当前进度："+progress);
//                    //改变当前播放进度
//                    video_player_videoview.seekTo(progress);
//                    //同时改变当前时间 textview
//                    video_player_tv_position.setText(StringUtils.formatTime(progress));
//                }
//            }
//        }
//    }
//    private final class OnVideoPreparedListener implements OnPreparedListener {
//        /**
//         * 准备好的回调
//         */
//        @Override
//        public void onPrepared(MediaPlayer mp) {
//            //准备好之后，隐藏加载中界面
//            video_player_ll_loading.setVisibility(View.GONE);
//            //播放视频
//            video_player_videoview.start();
//            switchPlayAndPausePic();
////            mp.start();
//            //初始化播放进度
//            initProgressView();
//        }
//
//    }
//    class MyBatteryChangeReceiver extends BroadcastReceiver{
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //系统电量发生改变，获取当前level电量等级
//            int level = intent.getIntExtra("level", 0);
////            logI("level:"+level);
//            updateBatteryPic(level);
//        }
//        
//    }
//
//    private static final int msg_update_system_time = 0;
//
//    private static final int msg_update_progress = 1;
//
//    private static final int msg_hide_control = 2;
//    
//    private Handler handler = new Handler(){
//        public void handleMessage(android.os.Message msg) {
//            switch (msg.what) {
//                case msg_update_system_time:
//                    startUpdateSystemTime();
//                break;
//                case msg_update_progress:
//                initProgressView();
//                break;
//                case msg_hide_control:
//                    hideControl();
//                break;
//                
//                default:
//                break;
//            }
//        };
//    };
//
//    private VideoView video_player_videoview;
//    private ImageView video_player_iv_pause;
//    private TextView video_player_tv_title;
//    private MyBatteryChangeReceiver receiver;
//    private ImageView video_player_iv_battery;
//    private TextView video_player_tv_system_time;
//
//    private SeekBar video_player_sk_volume;
//
//    private AudioManager audioManager;
//    
//    private float downY;//手指按下的y坐标
//
//    private int screenHalfHeight;//屏幕一半高度
//    private int screenHalfWidth;//屏幕一半的宽度
//
//    private int downCurrentProgress;
//
//    private View video_player_cover;
//
//    private float downCurrentAlpha;
//
//    private ImageView video_player_iv_mute;
//
//    private int lastVolume;//上一次的音量
//
//    private TextView video_player_tv_position;
//
//    private TextView video_player_tv_duration;
//
//    private SeekBar video_player_sk_position;
//
//    private ArrayList<VideoItem> videoItems;
//
//    private int position;
//
//    private ImageView video_player_iv_pre;
//
//    private ImageView video_player_iv_next;
//
//    private GestureDetector detector;
//
//    private ImageView video_player_iv_fullscreen;
//
//    private LinearLayout video_player_ll_top;
//
//    private LinearLayout video_player_ll_bottom;
//
//    private boolean isShowingControl = false;
//
//    private int topControlHeight;
//
//    private int bottomControlHeight;
//
//    private Uri fromFileMangerUri;
//
//    private LinearLayout video_player_ll_loading;
//
//    @Override
//    public int getLayoutResID() {
//        return R.layout.vitamio_video_player;
//    }
//    
//
//    @Override
//    public void initView() {
//        video_player_videoview = (VideoView) findViewById(R.id.video_player_videoview);
//        video_player_videoview.setOnPreparedListener(new OnVideoPreparedListener());
//        //暂停按钮
//        video_player_iv_pause = (ImageView) findViewById(R.id.video_player_iv_pause);
//        video_player_tv_title = (TextView) findViewById(R.id.video_player_tv_title);
//        //电量图片
//        video_player_iv_battery = (ImageView) findViewById(R.id.video_player_iv_battery);
//        //系统时间
//        video_player_tv_system_time = (TextView) findViewById(R.id.video_player_tv_system_time);
//        //音量的sk
//        video_player_sk_volume = (SeekBar) findViewById(R.id.video_player_sk_volume);
//        //透明度遮罩
//        video_player_cover = findViewById(R.id.video_player_cover);
//        ViewHelper.setAlpha(video_player_cover, 0);//默认透明
//        //静音按钮
//        video_player_iv_mute = (ImageView) findViewById(R.id.video_player_iv_mute);
//        //播放进度
//        video_player_tv_position = (TextView) findViewById(R.id.video_player_tv_position);
//        video_player_tv_duration = (TextView) findViewById(R.id.video_player_tv_duration);
//        video_player_sk_position = (SeekBar) findViewById(R.id.video_player_sk_position);
//        //上一首下一首
//        video_player_iv_pre = (ImageView) findViewById(R.id.video_player_iv_pre);
//        video_player_iv_next = (ImageView) findViewById(R.id.video_player_iv_next);
//        //全屏
//        video_player_iv_fullscreen = (ImageView) findViewById(R.id.video_player_iv_fullscreen);
//        //顶部底部导航
//        video_player_ll_top = (LinearLayout) findViewById(R.id.video_player_ll_top);
//        video_player_ll_bottom = (LinearLayout) findViewById(R.id.video_player_ll_bottom);
//        //加载中线性布局
//        video_player_ll_loading = (LinearLayout) findViewById(R.id.video_player_ll_loading);
//    }
//    
//    @Override
//    public void initListener() {
//        video_player_iv_pause.setOnClickListener(this);
//        video_player_iv_mute.setOnClickListener(this);
//        video_player_iv_pre.setOnClickListener(this);
//        video_player_iv_next.setOnClickListener(this);
//        video_player_iv_fullscreen.setOnClickListener(this);
//        //注册电量改变广播接收者
//        registBatteryChangeReceiver();
//        //音量sk设置进度改变监听
//        video_player_sk_volume.setOnSeekBarChangeListener(new OnVideoSeekBarChangeListener());
//        video_player_sk_position.setOnSeekBarChangeListener(new OnVideoSeekBarChangeListener());
//        //注册手势
//        detector = new GestureDetector(this, new SimpleOnVideoGestureListener());
//        //videoview的缓冲监听
//        video_player_videoview.setOnBufferingUpdateListener(new OnVideoBufferingUpdateListener());
//        //添加播放完成监听
//        video_player_videoview.setOnCompletionListener(new OnVideoCompletionListener());
//        //添加错误监听
//        video_player_videoview.setOnErrorListener(new OnVideoErrorListener());
//    }
//    
//    @Override
//    public void initData() {
//        //播放视频
//        Intent intent = getIntent();
//        fromFileMangerUri = intent.getData();
//        if(fromFileMangerUri==null){
//            //获取到是整个列表的适配数据
//            videoItems = (ArrayList<VideoItem>) intent.getSerializableExtra("videoItems");
//            //当前点击的位置  intent 不能传递bitmap
//            position = intent.getIntExtra("position", 0);
//            playItem();
//        }else{//从文件管理器播放视频
//            video_player_videoview.setVideoURI(fromFileMangerUri);
//            //更新视频名称  (视频路径)
//            video_player_tv_title.setText(fromFileMangerUri.getPath());
//            //显示加载中界面
//            video_player_ll_loading.setVisibility(View.VISIBLE);
//        }
//        //开启更新系统时间
//        startUpdateSystemTime();
//        updateVolumeSk();
//        screenHalfHeight = getResources().getDisplayMetrics().heightPixels/2;
//        screenHalfWidth = getResources().getDisplayMetrics().widthPixels/2;
//        //初始化上一首下一首按钮
//        updatePreAndNextBtnStatus();
////        initHideControl();  TODO
//    }
//
//
//    @Override
//    public void onInnerClick(View v) {
//        super.onInnerClick(v);
//        switch (v.getId()) {
//            case R.id.video_player_iv_pause://暂停播放
//                switchPlayAndPause();
//            
//            break;
//            case R.id.video_player_iv_mute://静音
//                switchVolumeMute();
//            break;
//            case R.id.video_player_iv_pre://上一首
//                playPre();
//            break;
//            case R.id.video_player_iv_next://下一首
//                playNext();
//            break;
//            case R.id.video_player_iv_fullscreen:
//                switchFullScreen();
//            break;
//            
//            default:
//            break;
//        }
//    }
//
//
//    /**
//     * 切换播放和暂停
//     */
//    private void switchPlayAndPause() {
//        //如果正在播放
//        if(video_player_videoview.isPlaying()){
//            //暂停
//            video_player_videoview.pause();
//            //移除进度更新msg
//            handler.removeMessages(msg_update_progress);
//        }else{
//            //播放
//            video_player_videoview.start();//继续播放
//            initProgressView();
//        }
//        switchPlayAndPausePic();
//        
//    }
//    /**
//     * 切换播放和暂停图片
//     */
//    private void switchPlayAndPausePic() {
//        if(video_player_videoview.isPlaying()){
//            //暂停按钮
//            video_player_iv_pause.setImageResource(R.drawable.video_pause_selector);
//        }else{
//            //播放按钮
//            video_player_iv_pause.setImageResource(R.drawable.video_play_selector);
//        }
//        
//    }
//    
//    private void registBatteryChangeReceiver() {
//        // Intent.ACTION_BATTERY_CHANGED;//电量发送改变的广播动作：充电状态，level：电量等级，电池相关信息
//        receiver = new MyBatteryChangeReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
//        registerReceiver(receiver, filter);
//    }
//    /**
//     * 界面销毁反注册
//     */
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(receiver!=null){
//            unregisterReceiver(receiver);
//        }
//        //handler的消息移除
//        handler.removeCallbacksAndMessages(null);
//    }
//    /**
//     * 根据电量等级，更新图片
//     * @param level
//     */
//    public void updateBatteryPic(int level) {
//        if(level<10){
//            video_player_iv_battery.setImageResource(R.drawable.ic_battery_0);
//        }else if(level<20){//10-19 
//            video_player_iv_battery.setImageResource(R.drawable.ic_battery_10);
//        }else if(level<40){
//            video_player_iv_battery.setImageResource(R.drawable.ic_battery_20);
//        }else if(level<60){
//            video_player_iv_battery.setImageResource(R.drawable.ic_battery_40);
//        }else if(level<80){
//            video_player_iv_battery.setImageResource(R.drawable.ic_battery_60);
//        }else if(level<100){
//            video_player_iv_battery.setImageResource(R.drawable.ic_battery_80);
//        }else {
//            video_player_iv_battery.setImageResource(R.drawable.ic_battery_100);
//        }
//        
//    }
//    /**
//     * 更新系统时间
//     */
//    private void startUpdateSystemTime() {
//        //初始化
//        video_player_tv_system_time.setText(StringUtils.getFormaterSystemTime());
////        logI("当前时间："+System.currentTimeMillis());
//        //通过handler
//        handler.sendEmptyMessageDelayed(msg_update_system_time, 500);
//        
//    }
//    /**
//     * 更新音量seekbar
//     */
//    private void updateVolumeSk() {
//        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
//        //获取系统音量
//        int streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//最大音量(STREAM_MUSIC系统的音乐播放音量)
//        int streamVolume = getSystemVolume();//当前音量
//        //sk：指定最大进度，当前进度
//        video_player_sk_volume.setMax(streamMaxVolume);
//        video_player_sk_volume.setProgress(streamVolume);
//    }
//
//    /**
//     * activity的触摸事件
//     * 手指划过屏幕改变音量进度：
//          1.手指在屏幕上划过距离 百分比 = 距离/屏幕高度
//          2.sk改变进度 = 百分比 * sk的最大值
//          3.sk最终进度 = sk原始进度+sk改变进度
//          
//                  手指滑动改变，系统屏幕亮度
//         * 1. 调用系统相关api
//         * 2. 改变控件透明度
//         * 
//         * 手指划过屏幕改变控件透明度：0-1  0完全透明 1不透明
//           1. 百分比  0- 1
//           2.透明度改变值 = 百分比 * 1   ====百分比
//           3.透明度最终值 = 起始透明度+ 透明度改变值
//     */
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        //******必须将事件传递给GestureDetector******
//        detector.onTouchEvent(event);
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//            downY = event.getY();
//            downCurrentProgress = video_player_sk_volume.getProgress();
//            //获取控件的透明度
//            downCurrentAlpha = ViewHelper.getAlpha(video_player_cover);
//            //按下停止隐藏
//            handler.removeMessages(msg_hide_control);
//            break;
//            case MotionEvent.ACTION_MOVE:
//            //1.手指在屏幕上划过距离 百分比 = 距离/屏幕高度
//            float moveY = event.getY();
//            float disY = moveY - downY;
//            float disPrecent = disY / screenHalfHeight;
//            
//            int moveX = (int) event.getX();
//            if(moveX<screenHalfWidth){
//                //屏幕左侧 调整 亮度
//                changeAlpha(disPrecent);
//            }else{
//                //屏幕右侧 调整 音量
//                changeVolume(disPrecent);
//            }
//            break;
//            case MotionEvent.ACTION_UP:
//            //抬起，
//            //再次自动隐藏
//            handler.sendEmptyMessageDelayed(msg_hide_control, 2000);
//            
//            break;
//            
//            default:
//            break;
//        }
//        
//        return super.onTouchEvent(event);
//    }
//
//    private void changeVolume(float disPrecent) {
//        //2.sk改变进度 = 百分比 * sk的最大值
//        int changeProgress = (int) (disPrecent*video_player_sk_volume.getMax());
//        //3.sk最终进度 = sk原始进度+sk改变进度
//        int endProgress = downCurrentProgress+changeProgress;
//        video_player_sk_volume.setProgress(endProgress);
//    }
//
//    private void changeAlpha(float disPrecent) {
//        float endAlpha =  downCurrentAlpha + disPrecent;
//        if(endAlpha>=0&&endAlpha<=1){
//            logI("endAlpha:"+endAlpha);
//            ViewHelper.setAlpha(video_player_cover, endAlpha);
//        }
//    }
//    /**
//     * 切换音量静音
//     */
//    private void switchVolumeMute() {
//        //如果当前音量不等于0，静音
//        if(getSystemVolume() !=0){
//            lastVolume = getSystemVolume();
//            setSystemVolume(0);
//        }else{
//            //如果当前音量等于0，那恢复原来音量
//            setSystemVolume(lastVolume);
//        }
//        
//    }
//    private void setSystemVolume(int progress) {
//        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
//        //系统音量改变，需要通过改变sk的进度
//        video_player_sk_volume.setProgress(progress);
//    }
//    private int getSystemVolume() {
//        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//    }
//    /**
//     * 初始化进度相关的控件
//     */
//    private void initProgressView() {
//        // 获取当前进度、总时长
//        int currentPosition = (int) video_player_videoview.getCurrentPosition();
//        int duration = (int) video_player_videoview.getDuration();
//        // 文本展示的需要是格式化后的时间
//        video_player_tv_position.setText(StringUtils.formatTime(currentPosition));
//        video_player_tv_duration.setText(StringUtils.formatTime(duration));
//        // sk
//        video_player_sk_position.setMax(duration);
//        video_player_sk_position.setProgress(currentPosition);
////        logI("当前播放进度：" + currentPosition);
//        handler.sendEmptyMessageDelayed(msg_update_progress, 500);
//    }
//    
//    /**
//     * 上一首
//     */
//    private void playPre() {
//        if(position!=0){
//            position -- ;
//            playItem();
//        }
//        updatePreAndNextBtnStatus();
//    }
//   
//
//    /**
//     * 下一首
//     */
//    private void playNext() {
//        if(position != videoItems.size()-1){
//            position++;
//            playItem();
//        }
//        updatePreAndNextBtnStatus();
//    }
//    /**
//     * 播放视频逻辑
//     */
//    private void playItem() {
//        VideoItem videoItem = videoItems.get(position);
//        logI(videoItem.toString());
//        //处理播放逻辑
//        //设置视频路径
//        video_player_videoview.setVideoPath(videoItem.path);
//        //播放  如果视频比较大，容易内存溢出 anr
////        video_player_videoview.start();
//        //初始化标题
//        video_player_tv_title.setText(videoItem.title);
//    }
//    /**
//     * 更新上一首和下一首按钮的状态
//     */
//    private void updatePreAndNextBtnStatus() {
//        if(fromFileMangerUri==null){
//            if(position == 0){
//                //上一首按钮变为不可用
//                video_player_iv_pre.setEnabled(false);
//                video_player_iv_next.setEnabled(true);
//            }else if(position == videoItems.size()-1){
//                //下一首按钮变为不可用
//                video_player_iv_pre.setEnabled(true);
//                video_player_iv_next.setEnabled(false);
//            }else{
//                video_player_iv_pre.setEnabled(true);
//                video_player_iv_next.setEnabled(true);
//            }
//        }else{//从外部文件管理器打开，上一首下一首都不可用
//            video_player_iv_pre.setEnabled(false);
//            video_player_iv_next.setEnabled(false);
//        }
//        
//    }
//    /**
//     * 全屏切换
//     */
//    private void switchFullScreen() {
//        video_player_videoview.switchFullScreen();
////        //图片改变
//        if(video_player_videoview.isFullScreen()){
//            video_player_iv_fullscreen.setImageResource(R.drawable.video_defaultscreen_selector);
//        }else{
//            video_player_iv_fullscreen.setImageResource(R.drawable.video_fullscreen_selector);
//        }
//    }
//    
//    /**
//     * 初始化隐藏控制条
//     */
//    private void initHideControl() {
//        //顶部底部控制条的高度
//        video_player_ll_top.measure(0, 0);
//        topControlHeight = video_player_ll_top.getMeasuredHeight();
//        video_player_ll_bottom.measure(0, 0);
//        bottomControlHeight = video_player_ll_bottom.getMeasuredHeight();
//        hideControl();
//    }
//
//
//    /**
//     * 显示和隐藏控制条
//     */
//    public void showOrHideControl() {
//        if(!isShowingControl){
//            //显示
//            showControl();
//        }else{
//            //隐藏
//            hideControl();
//            //手势控制隐藏，不用在自动隐藏
//            handler.removeMessages(msg_hide_control);
//        }
//        
//    }
//
//    /**
//     * 显示
//     */
//    private void showControl() {
//        ViewPropertyAnimator.animate(video_player_ll_top).translationY(0);
//        ViewPropertyAnimator.animate(video_player_ll_bottom).translationY(0);
//        //发送延迟消息，2s之后自动隐藏
//        handler.sendEmptyMessageDelayed(msg_hide_control, 2000);
//        isShowingControl = true;
//    }
//    /**
//     * 隐藏的
//     */
//    private void hideControl() {
//        //顶部 位移动画，位移距离，-顶部控制条的高度
//        ViewPropertyAnimator.animate(video_player_ll_top).translationY(-topControlHeight);
//        //底部 位移距离，+顶部控制条的高度
//        ViewPropertyAnimator.animate(video_player_ll_bottom).translationY(bottomControlHeight);
//        isShowingControl = false;
//    }
// 
//    
//}
