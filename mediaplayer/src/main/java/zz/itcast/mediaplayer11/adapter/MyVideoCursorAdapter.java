package zz.itcast.mediaplayer11.adapter;

import zz.itcast.mediaplayer11.R;
import zz.itcast.mediaplayer11.bean.VideoItem;
import zz.itcast.mediaplayer11.utils.StringUtils;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * 视频列表的adapter
 * @author wangdh
 *
 */
public class MyVideoCursorAdapter extends CursorAdapter {
    
    public MyVideoCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }
    /**
     * 创建一个新的view 
     * 与holder进行绑定
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = View.inflate(context, R.layout.video_list_item, null);
        VideoItemHolder holder = new VideoItemHolder();
        //标题
        holder.video_list_item_tv_title = (TextView) view.findViewById(R.id.video_list_item_tv_title);
        //时长
        holder.video_list_item_tv_duration = (TextView) view.findViewById(R.id.video_list_item_tv_duration);
        //大小
        holder.video_list_item_tv_size = (TextView) view.findViewById(R.id.video_list_item_tv_size);
        view.setTag(holder);
        return view;
    }
    /**
     * 给view(holder)填充数据
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        VideoItemHolder holder = (VideoItemHolder) view.getTag();
        //cursor转换为Javabean
        VideoItem videoItem = VideoItem.instanceFromCursor(cursor);
        holder.video_list_item_tv_title.setText(videoItem.title);
        //int转换为string：videoItem.duration+"" ，创建出来一个空字符串对象  
        holder.video_list_item_tv_duration.setText(StringUtils.formatTime(videoItem.duration));
        //文件大小格式转换  Formatter.formatFileSize
        holder.video_list_item_tv_size.setText(Formatter.formatFileSize(context, videoItem.size));
    }
    class VideoItemHolder {
        TextView video_list_item_tv_title,video_list_item_tv_duration,video_list_item_tv_size;
    }
    
}
