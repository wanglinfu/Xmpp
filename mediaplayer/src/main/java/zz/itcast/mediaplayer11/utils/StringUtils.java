package zz.itcast.mediaplayer11.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.format.DateUtils;

public class StringUtils {
    private static final int HOUR = 1000*60*60;
    private static final int MIN = 1000*60;
    private static final int SEC = 1000;
    /**
     * 时间格式化
     * 00:00:00
     * 00:00
     */
    public static String formatTime(int time){
        //小时
        int hour = time/HOUR;
        //分钟
        int min = time%HOUR/MIN;
        //秒
        int sec = time%MIN/SEC;
        if(hour==0){
            // 00:00 (%占位符，02 ：两位数字，如果不满两位自动补零)
            return String.format("%02d:%02d", min,sec);
        }else{
            return String.format("%02d:%02d:%02d",hour, min,sec);
        }
    }
    /**
     * 获取格式系统时间
     */
    public static String getFormaterSystemTime(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");//hh:12,HH:24
        return format.format(new Date());//System.currentTimeMillis()
    }
    
}
