package zz.itcast.mediaplayer11.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.provider.MediaStore.Video.Media;

/**
 * 视频数据对应的Javabean
 * @author wangdh
 *
 */
public class VideoItem implements Serializable{
    /**
     * id
     * 标题
     * 路径
     * 时长
     * 大小
     */
    public int id;
    public String title;
    public String path; // data
    public int duration;
    public int size;
    @Override
    public String toString() {
        return "VideoItem [id=" + id + ", title=" + title + ", path=" + path + ", duration=" + duration + ", size="
                + size + "]";
    }
    /**
     * 将cursor转换为Javabean
     * @param cursor
     * @return
     */
    public static VideoItem instanceFromCursor(Cursor cursor){
        VideoItem videoItem = new VideoItem();
        videoItem.id = cursor.getInt(cursor.getColumnIndex(Media._ID));
        videoItem.duration = cursor.getInt(cursor.getColumnIndex(Media.DURATION));
        videoItem.size = cursor.getInt(cursor.getColumnIndex(Media.SIZE));
        videoItem.title = cursor.getString(cursor.getColumnIndex(Media.TITLE));
        videoItem.path = cursor.getString(cursor.getColumnIndex(Media.DATA));
        return videoItem;
    }
    public static List<VideoItem> instanceListFromCursor(Cursor cursor){
        
        List<VideoItem> videoItems = new ArrayList<VideoItem>();
//        cursor.moveToPosition(0);
        //循环所有cursor
        for (int i = 0; i < cursor.getCount(); i++) {
            //当前cursor
            cursor.moveToPosition(i);
            //获取当前的videoItem
            VideoItem videoItem = instanceFromCursor(cursor);
            videoItems.add(videoItem);
        }
        return videoItems;
    }
    
}
