package zz.itcast.mediaplayer11.adapter;

import zz.itcast.mediaplayer11.utils.CursorUtils;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
/**
 * 视频异步查询handler
 * 视频和音频公用的异步查询
 * @author wangdh
 *
 */
public class MyAsyncQueryHandler extends AsyncQueryHandler {
    public MyAsyncQueryHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);
        //获取到数据
//        CursorUtils.printCursor(cursor);
        //更新cursor，adapter刷新
//        mCursor = cursor;
//        adapter.notifyDataSetChanged();
        if(cookie instanceof CursorAdapter){
            CursorAdapter adapter =  (CursorAdapter) cookie;
            adapter.swapCursor(cursor);//替换新的cursor
        }
    }
    
}
