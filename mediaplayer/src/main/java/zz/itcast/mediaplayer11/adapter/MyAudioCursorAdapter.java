package zz.itcast.mediaplayer11.adapter;

import zz.itcast.mediaplayer11.R;
import zz.itcast.mediaplayer11.bean.AudioItem;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * 音频的adapter
 * @author wangdh
 *
 */
public class MyAudioCursorAdapter extends CursorAdapter {
    public MyAudioCursorAdapter(Context context,Cursor cursor){
        super(context, cursor);
    }
    /**
     * 创建一个新view，与holder绑定
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = View.inflate(context, R.layout.audio_list_item, null);
        AudioHolder holder = new AudioHolder();
        holder.main_list_item_tv_title = (TextView) view.findViewById(R.id.main_list_item_tv_title);
        holder.main_list_item_tv_artist = (TextView) view.findViewById(R.id.main_list_item_tv_artist);
        view.setTag(holder);
        return view;
    }
    /**
     * holder填充数据
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        AudioHolder holder = (AudioHolder) view.getTag();
        //cursor 填充数据 ---- Javabean
        AudioItem audioItem = AudioItem.instanceFromCursor(cursor);
        holder.main_list_item_tv_title.setText(audioItem.title);
        holder.main_list_item_tv_artist.setText(audioItem.artist);
    }
    class AudioHolder {
        TextView main_list_item_tv_title,main_list_item_tv_artist;
    }
    
}
