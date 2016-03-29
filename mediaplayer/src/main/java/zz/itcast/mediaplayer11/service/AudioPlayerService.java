package zz.itcast.mediaplayer11.service;

import java.util.ArrayList;
import java.util.Random;

import zz.itcast.mediaplayer11.R;
import zz.itcast.mediaplayer11.activity.AudioPlayerActivity;
import zz.itcast.mediaplayer11.activity.MainActivity;
import zz.itcast.mediaplayer11.bean.AudioItem;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;
/**
 * 音乐播放服务
 * @author wangdh
 *
 */
public class AudioPlayerService extends Service {
    public static final String ACTION_AUDIO_PREPARED = "zz.itcast.mediaplayer11.audio_prepared";
    /**
     * 播放模式
     *  1. 列表循环
        2. 单曲循环
        3. 随即播放
     */
    public static final int play_mode_repeat = 0;
    public static final int play_mode_single = 1;
    public static final int play_mode_random = 2;
    //通知的消息：上一首
    private static final int msg_notification_play_pre = 0;
    //通知的消息：下一首
    private static final int msg_notification_play_next = 1;
    //通知的消息：整个布局
    private static final int msg_notification_play_content = 2;
    //当前播放模式
    public int currentPlayMode = play_mode_repeat;//默认列表循环
    
    public int lastPosition = -1;//上一次播放位置
    
    
    private final class OnAudioCompletionListener implements OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            //播放完成，切换下一首
            binder.autoPlayNext();
        }
    }
    private final class OnAudioPreparedListener implements OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            //准备好，播放
            mediaPlayer.start();
            
            sendAudioPreparedBoradCast();
            
            //弹出通知
            showNotification();
        }

    }

    private MediaPlayer mediaPlayer = null;
    private AudioItem audioItem;
    private MyAudioServiceBinder binder;
    private ArrayList<AudioItem> audioItems;
    private int position;
    private NotificationManager manager;
    @Override
    public void onCreate() {
        super.onCreate();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        binder = new MyAudioServiceBinder();
        mediaPlayer = new MediaPlayer();
        //准备好的监听
        mediaPlayer.setOnPreparedListener(new OnAudioPreparedListener());
        //播放完成监听
        mediaPlayer.setOnCompletionListener(new OnAudioCompletionListener());
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //从通知打开服务
        int msg = intent.getIntExtra("msg", -1);
        if(msg == -1){
            audioItems = (ArrayList<AudioItem>) intent.getSerializableExtra("audioItems");
            position = intent.getIntExtra("position",0);
//        audioItem = (AudioItem) intent.getSerializableExtra("audioItem");
            //如果当前播放的与正在播放的position一致
            if(lastPosition == position){
                //如果暂停。bug：不播放
                if(!binder.isPlaying()){
                    binder.start();//继续播放
                }
                sendAudioPreparedBoradCast();//发送广播，更新界面
            }else{
                //如果不一致
//                lastPosition = position;
                // 播放
                binder.play();
            }
        }else{
            //从通知打开
            switch (msg) {
                case msg_notification_play_pre:
                
                binder.playPre();
                break;
                case msg_notification_play_next:
                binder.playNext();
                
                break;
                case msg_notification_play_content:
                    sendAudioPreparedBoradCast();
                break;
                
                default:
                break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    /**
     * binder 对象  ： 播放和暂停逻辑
     */
    public class MyAudioServiceBinder extends Binder {
        //播放
        public void play(){
            try {
                audioItem = audioItems.get(position);
                lastPosition = position;//TODO 在播放的时候记录上一次播放position。（条目点击、上一首、下一首，都的走这个逻辑）
                Log.i("AudioPlayerService", "------:" + audioItem.toString());
                // 播放音乐
                // 1.重置
                mediaPlayer.reset();
                // 2.设置数据源
                mediaPlayer.setDataSource(audioItem.path);
                // 3.准备
//                mediaPlayer.prepare();// 本地准备
                mediaPlayer.prepareAsync();
                // 4.播放
//                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * 切换暂停和播放
         */
        public void switchPlayAndPause(){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                //取消通知
                cancleNotification();
            }else{
                //弹出
                showNotification();
                mediaPlayer.start();
            }
        }
        /**
         * 是否正在播放
         * @return
         */
        public boolean isPlaying(){
            return mediaPlayer.isPlaying();
        }
        /**
         * 1. 获取当前播放进度
         * 2. 获取当前总时长
         */
        public int getCurrentPosition(){
            return mediaPlayer.getCurrentPosition();
        }
        public int getDuration(){
            return mediaPlayer.getDuration();
        }
        /**
         * 拖拽播放
         */
        public void seekTo(int position){
            mediaPlayer.seekTo(position);
        }
        /**
         * 1. 上一首
         * 2. 下一首
         */
        public void playPre(){
            if(position !=0){
                position--;
                play();
            }else{
                Toast.makeText(getApplicationContext(), 
                        "已经是第一首了，不要再点了，亲", Toast.LENGTH_SHORT).show();
            }
            
        }
        public void playNext(){
            if(position!=audioItems.size()-1){
                position++;
                play();
            }else{
                Toast.makeText(getApplicationContext(), 
                        "已经是最后一首了，不要再点了，么么哒", Toast.LENGTH_SHORT).show();
            }
        }
        /**
         * 播放模式切换
         *  1. 列表循环
            2. 单曲循环
            3. 随即播放
         */
        public void switchPlayMode(){
            if(currentPlayMode == play_mode_repeat){
                currentPlayMode = play_mode_single;
                
            }else if(currentPlayMode == play_mode_single){
                currentPlayMode = play_mode_random;
                
            }else if(currentPlayMode == play_mode_random){
                currentPlayMode = play_mode_repeat;
            }
        }
        /**
         * 自动切换下一首
         */
        public void autoPlayNext() {
            switch (currentPlayMode) {
                //列表循环
                case play_mode_repeat:
                    //如果是最后一首，从0开始。如果不是，position++
                    if(position == audioItems.size()-1){
                        position = 0;
                    }else{
                        position++;
                    }
//                    play();
                break;
                //单曲循环
                case play_mode_single:
//                    play();
                break;
                //随即播放
                case play_mode_random:
                    Random random = new Random();
                    position = random.nextInt(audioItems.size());//[0-7)
//                    play();
                break;
                
                default:
                break;
            }
            play();//自动播放
        }
        /**
         * 返回当前播放模式
         */
        public int getCurrentPlayMode(){
            return currentPlayMode;
        }
        /**
         * 播放、继续播放
         */
        public void start(){
            mediaPlayer.start();
        }
    }
    /**
     * 发送音频准备好的广播
     */
    private void sendAudioPreparedBoradCast() {
        //发送广播 服务将数据传递给Activity
        Intent intent = new Intent(ACTION_AUDIO_PREPARED);
        //传递当前播放的音乐信息
        intent.putExtra("audioItem", audioItem);
        sendBroadcast(intent);
    }
  // 音乐开始播放的时候 弹出通知
    public void showNotification(){
        Notification notification = showCustomViewNotificationWithNewAPi();
        manager.notify(0, notification);
    }
    //暂停的时候 取消通知  
    public void cancleNotification(){
        manager.cancel(0);
    }
    /**
     * 新的api弹出自定义布局的通知
     * @return
     */
    public Notification showCustomViewNotificationWithNewAPi(){
        NotificationCompat.Builder builder = new Builder(this);
        //icon、标题，when
        builder.setSmallIcon(R.drawable.notification_music_playing);
        builder.setTicker("正在播放："+audioItem.title);
        builder.setWhen(0);
        //flag
        builder.setOngoing(true);//不会消失
        //设置内容，自定义布局
        builder.setContent(getRemoteView());
        return builder.build();
        
    }
    private RemoteViews getRemoteView() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.audio_notify);
        //给自定义布局的控件填充内容
        remoteViews.setTextViewText(R.id.audio_notify_tv_title, audioItem.title);
        remoteViews.setTextViewText(R.id.audio_notify_tv_arties, audioItem.artist);
        //给自定义布局的控件设置点击事件
        remoteViews.setOnClickPendingIntent(R.id.audio_notify_iv_pre, getPrePendingIntent());
        remoteViews.setOnClickPendingIntent(R.id.audio_notify_iv_next, getNextPendingIntent());
        remoteViews.setOnClickPendingIntent(R.id.audio_notify_layout, getContentPendingIntent());
        return remoteViews;
    }
    /**
     * 上一首的延迟意图  打开服务，调用服务的上一首逻辑
     * @return
     */
    public PendingIntent getPrePendingIntent() {
        Intent intent = new Intent(this, AudioPlayerService.class);
        intent.putExtra("msg", msg_notification_play_pre);
        PendingIntent contentIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return contentIntent;
    }
    /**
     * 下一首的延迟意图  打开服务，调用服务的下一首逻辑
     * @return
     */
    private PendingIntent getNextPendingIntent() {
        Intent intent = new Intent(this, AudioPlayerService.class);
        intent.putExtra("msg", msg_notification_play_next);
        PendingIntent contentIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return contentIntent;
    }
    /**
     * 整个布局的延迟意图  打开音乐播放界面
     * @return
     */
    private PendingIntent getContentPendingIntent() {
        Intent intent = new Intent(this, AudioPlayerActivity.class);
        intent.putExtra("msg", msg_notification_play_content);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return contentIntent;
    }
    
}
