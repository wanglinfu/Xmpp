package zz.itcast.mediaplayer11.fragment;

import java.util.ArrayList;

import zz.itcast.mediaplayer11.R;
import zz.itcast.mediaplayer11.activity.VideoPlayerActivity;
import zz.itcast.mediaplayer11.adapter.MyAsyncQueryHandler;
import zz.itcast.mediaplayer11.adapter.MyVideoCursorAdapter;
import zz.itcast.mediaplayer11.bean.VideoItem;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore.Video.Media;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
/**
 * 视频列表界面
 * @author wangdh
 *
 */
public class VideoListFragment extends BaseFragment {

    private final class OnVideoItemClickListener implements OnItemClickListener {
        /**
         * parent:当前的listview
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //打开播放界面
            Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
//            Intent intent = new Intent(getActivity(), VitamioVideoPlayerActivity.class);
            //传递当前的数据
            //根据position获取对应的item
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
//            VideoItem videoItem = VideoItem.instanceFromCursor(cursor);
            ArrayList<VideoItem> videoItems = (ArrayList<VideoItem>) VideoItem.instanceListFromCursor(cursor);
            //传递videoItem的集合，所有视频列表
            intent.putExtra("videoItems", videoItems);//arrayList是序列化的对象，list不是
            intent.putExtra("position",position);
            getActivity().startActivity(intent);
            
        }
    }

    private ListView videoListview;
    private MyVideoCursorAdapter adapter;

    @Override
    public int getLayoutResID() {
        return R.layout.video_list;
    }

    @Override
    public void initView(View view) {
        videoListview = (ListView) view.findViewById(R.id.simple_listview);
        
    }

    @Override
    public void initListener() {
        //cursor 是数据源
        adapter = new MyVideoCursorAdapter(getActivity(), null);
        videoListview.setAdapter(adapter);
        videoListview.setOnItemClickListener(new OnVideoItemClickListener());
    }

    @Override
    public void initData() {
      //通过内容提供者获取数据
        ContentResolver contentResolver = getActivity().getContentResolver();
//        Cursor cursor = contentResolver.query(Media.EXTERNAL_CONTENT_URI, 
//                //id、视频名称、路径、视频大小、视频时长
//                new String[]{Media._ID,Media.TITLE,Media.DATA,Media.SIZE,Media.DURATION}, null, null, null);
//        CursorUtils.printCursor(cursor);
        MyAsyncQueryHandler asyncQueryHandler = new MyAsyncQueryHandler(contentResolver);
        /**
         * int token,   类似message的what 代表哪一次查询
         * object cookie :  类似message的obj  传递的数据
         */
//        asyncQueryHandler.startQuery(0, adapter, Media.EXTERNAL_CONTENT_URI, 
//                //id、视频名称、路径、视频大小、视频时长
//                new String[]{Media._ID,Media.TITLE,Media.DATA,Media.SIZE,Media.DURATION}, null, null, null);
        asyncQueryHandler.startQuery(0 , adapter, Media.EXTERNAL_CONTENT_URI,
                new String[] { Media._ID, Media.TITLE, Media.SIZE,
                        Media.DURATION, Media.DATA }, null, null, null);
        
    }
    
}
