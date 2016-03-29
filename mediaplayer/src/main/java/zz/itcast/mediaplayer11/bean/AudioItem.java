package zz.itcast.mediaplayer11.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;

/**
 * 音频对应的Javabean
 * @author wangdh
 *
 */
public class AudioItem implements Serializable {
    /**
     * //id、歌曲名称、演唱者、路径、时长
     */
    public int id;
    public String title;
    public String artist;
    public String path;
    public int duration;
    @Override
    public String toString() {
        return "AudioItem [id=" + id + ", title=" + title + ", artist=" + artist + ", path=" + path + ", duration="
                + duration + "]";
    }
    /**
     * cursor 转换为Javabean
     * @param cursor
     * @return
     */
    public static AudioItem instanceFromCursor(Cursor cursor){
        AudioItem audioItem = new AudioItem();
        audioItem.id = cursor.getInt(cursor.getColumnIndex(Media._ID));
        audioItem.duration = cursor.getInt(cursor.getColumnIndex(Media.DURATION));
        audioItem.title = cursor.getString(cursor.getColumnIndex(Media.TITLE));
        audioItem.artist = cursor.getString(cursor.getColumnIndex(Media.ARTIST));
        audioItem.path = cursor.getString(cursor.getColumnIndex(Media.DATA));
        return audioItem;
    }
    /**
     * cursor转换list集合
     */
    public static ArrayList<AudioItem> instanceListFromCursor(Cursor cursor){
        ArrayList<AudioItem> audioItems = new ArrayList<AudioItem>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            AudioItem audioItem= instanceFromCursor(cursor); 
            audioItems.add(audioItem);
        }
        return audioItems;
    }
    
    
}
