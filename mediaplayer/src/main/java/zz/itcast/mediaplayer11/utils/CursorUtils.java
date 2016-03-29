package zz.itcast.mediaplayer11.utils;

import android.database.Cursor;
import android.util.Log;

public class CursorUtils {
    /**
     * 打印cursor
     * @param cursor
     */
    public static void printCursor(Cursor cursor) {
        while(cursor.moveToNext()){
            Log.i("CursorUtils", "--------------------");
            //循环所有列
            for (int i = 0; i < cursor.getColumnCount(); i++) {
             Log.i("CursorUtils", cursor.getColumnName(i)+":"+cursor.getString(i));
            }
        }
    }
    
}
