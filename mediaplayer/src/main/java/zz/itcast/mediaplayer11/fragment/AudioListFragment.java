package zz.itcast.mediaplayer11.fragment;

import java.util.ArrayList;

import zz.itcast.mediaplayer11.R;
import zz.itcast.mediaplayer11.activity.AudioPlayerActivity;
import zz.itcast.mediaplayer11.adapter.MyAsyncQueryHandler;
import zz.itcast.mediaplayer11.adapter.MyAudioCursorAdapter;
import zz.itcast.mediaplayer11.bean.AudioItem;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
/**
 * 音频列表界面
 * @author wangdh
 *
 */
public class AudioListFragment extends BaseFragment {

    private final class OnAudioItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //点击某个音乐，跳转到音乐播放界面
            //获取点击的这个AudioItem
            Cursor cursor= (Cursor) parent.getItemAtPosition(position);
//            AudioItem audioItem = AudioItem.instanceFromCursor(cursor);
            ArrayList<AudioItem> audioItems = AudioItem.instanceListFromCursor(cursor);
            Intent intent = new Intent(getActivity(), AudioPlayerActivity.class);
            intent.putExtra("audioItems", audioItems);
            intent.putExtra("position", position);
            startActivity(intent);
        }
    }

    private ListView audioListView;
    private MyAudioCursorAdapter adapter;

    @Override
    public int getLayoutResID() {
        return R.layout.audio_list;
    }

    @Override
    public void initView(View view) {
        //音频列表listview
        audioListView = (ListView) view.findViewById(R.id.simple_listview);
        
    }

    @Override
    public void initListener() {
        adapter = new MyAudioCursorAdapter(getActivity(), null);
        audioListView.setAdapter(adapter);
        audioListView.setOnItemClickListener(new OnAudioItemClickListener());
    }

    @Override
    public void initData() {
        //获取数据
        MyAsyncQueryHandler queryHandler = new MyAsyncQueryHandler(getActivity().getContentResolver());
        //id、歌曲名称、演唱者、路径、时长
        queryHandler.startQuery(1, adapter, Media.EXTERNAL_CONTENT_URI, new String[]{
                Media._ID,
                Media.TITLE,
                Media.ARTIST,//艺术家
                Media.DATA,
                Media.DURATION
        }, null, null, null);
        
    }
    
}
