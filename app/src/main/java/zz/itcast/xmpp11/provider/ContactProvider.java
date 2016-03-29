package zz.itcast.xmpp11.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import java.net.URI;

/**
 * =========================================
 * 版权所有 违法必究
 * 作者: wxj.
 * <p/>
 * 工程: Xmpp11.
 * <p/>
 * 文件名: ContactProvider.
 * <p/>
 * 时间: 2016/3/21.
 * <p/>
 * 修订历史:
 * <p/>
 * 修订时间:
 * <p/>
 * =========================================
 */
public class ContactProvider extends ContentProvider {
    // 完整的类名
    private static final String authrotiy = ContactProvider.class.getCanonicalName();
    private static final String DB = "contact.db";
    private static final String TABLE = "contact";
    private ContactOpenHelper openHelper;

    // 表的 规划
    public static class Contact implements BaseColumns {
        public static final String ACCOUNT = "account";
        public static final String NICK = "nick";
        public static final String AVATAR = "avatar";

        public static final String SORT = "sort";// 拼音排序
    }

    // create tablecontact
    private static final String sql_create = "create table " + TABLE +
            "(_id integer primary key autoincrement,account text,nick text,avatar integer,sort text)";


    class ContactOpenHelper extends SQLiteOpenHelper {

        public ContactOpenHelper(Context context) {
            super(context, DB, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //创建表
            db.execSQL(sql_create);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    //  返回值 表示 是否将 openhelper 对象创建成功
    @Override
    public boolean onCreate() {
        // 如果是一个空的Context  可以获取ContactOpenHelper
        //  openHelper.getWritableDatabase() = null

        openHelper = new ContactOpenHelper(getContext());

        return openHelper != null;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    //对外暴露的uri
    //   content://zz.itcast.xmpp11.provider/contact
    public static final Uri CONTACT_URI = Uri.parse("content://" + authrotiy + "/" + TABLE);
    private static UriMatcher uriMatcher = null;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//        content://zz.itcast.xmpp11.provider/contact
        uriMatcher.addURI(authrotiy, TABLE, 0);
    }

    /**
     * C 插入操作
     *
     * @param uri
     * @param values
     * @return
     */
//    content://zz.itcast.xmpp11.provider/contact
//    content://zz.itcast.xmpp11.provider/contact/id
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = -1;
        switch (uriMatcher.match(uri)) {
            case 0: //  操作的是 Contact表
                SQLiteDatabase db = openHelper.getWritableDatabase();
                id = db.insert(TABLE, null, values);
//                content://zz.itcast.xmpp11.provider/contact
                uri = ContentUris.withAppendedId(uri, id);
//                content://zz.itcast.xmpp11.provider/contact/10
                break;
        }
        if (id != -1){
            //    通知内容观察者
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return uri;
    }

    //D
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // 删除和更新操作都是影响的行数
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case 0: //  操作的是 Contact表
                SQLiteDatabase db = openHelper.getWritableDatabase();
                // 删除操作 是 返回影响的行数
                count = db.delete(TABLE, selection, selectionArgs);
                break;
        }
        if (count > 0){
            //    通知内容观察者
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    //U
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case 0: //  操作的是 Contact表
                SQLiteDatabase db = openHelper.getWritableDatabase();
                // 更新操作 是 返回影响的行数
                count = db.update(TABLE, values, selection, selectionArgs);
                break;
        }
        if (count > 0){
            //    通知内容观察者
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }


    //R
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case 0: //  操作的是 Contact表
                SQLiteDatabase db = openHelper.getWritableDatabase();
                // 更新操作 是 返回影响的行数
                cursor = db.query(TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
        }
        return cursor;
    }
}
