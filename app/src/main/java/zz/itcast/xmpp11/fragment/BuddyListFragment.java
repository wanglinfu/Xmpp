package zz.itcast.xmpp11.fragment;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import zz.itcast.xmpp11.R;
import zz.itcast.xmpp11.provider.ContactProvider;

/**
 * 会话  ctrl + alt + o
 */
public class BuddyListFragment extends BaseFragment {

    private ListView listview;
    private Cursor cursor;
    CursorAdapter adapter;
    // 观察 数据库内容变化
    ContentObserver observer = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            //低版本
            setAdapterOrNotify();
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            // 高版本
            setAdapterOrNotify();
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        context.getContentResolver().unregisterContentObserver(observer);
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buddylist, container, false);
        listview = (ListView) view.findViewById(R.id.listview);


        //注册内容观察者
        //    content://zz.itcast.xmpp11.provider/contact
//    content://zz.itcast.xmpp11.provider/contact/id   如果也想观察到  第二个参数 写true
        //观察到 内容变化 重新查询

        context.getContentResolver().registerContentObserver(ContactProvider.CONTACT_URI, true, observer);

        // 通过数据库拿到 数据进行展示
        setAdapterOrNotify();

        return view;
    }



    /**
     * 1.适配数据
     * 2. 更新数据
     */
    private void setAdapterOrNotify() {
        if (adapter != null) {
            //重新查询
            adapter.getCursor().requery();
            return;
        }
        //按照拼音排序后的  结果集
        cursor = context.getContentResolver().query(ContactProvider.CONTACT_URI, null, null, null, ContactProvider.Contact.SORT + " asc");
        if (cursor == null) {
            return;
        }

        adapter = new CursorAdapter(context, cursor) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_buddy, null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                //TODO 移动Cursor
                cursor.moveToPosition(position);
                holder.account.setText(cursor.getString(cursor.getColumnIndex(ContactProvider.Contact.ACCOUNT)));
                holder.nick.setText(cursor.getString(cursor.getColumnIndex(ContactProvider.Contact.NICK)));

                return convertView;
            }

            //   convertView == null  会调用
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return null;
            }

            // 适配数据
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
            }
        };
        listview.setAdapter(adapter);
    }

    public static class ViewHolder {
        public View rootView;
        public ImageView head;
        public TextView nick;
        public TextView account;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.head = (ImageView) rootView.findViewById(R.id.head);
            this.nick = (TextView) rootView.findViewById(R.id.nick);
            this.account = (TextView) rootView.findViewById(R.id.account);
        }

    }
}
