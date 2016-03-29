package zz.itcast.xmpp11.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

import java.util.Collection;

import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;
import zz.itcast.xmpp11.base.MyApp;
import zz.itcast.xmpp11.provider.ContactProvider;
import zz.itcast.xmpp11.tools.ThreadUtil;

/**
 * 联系人同步的服务
 */
public class ContactService extends Service {

    MyRosterListener rosterListener = new MyRosterListener();
    private Roster roster;

    public ContactService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (roster != null) {
            roster.removeRosterListener(rosterListener);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(ContactService.this, "联系人服务启动", Toast.LENGTH_SHORT).show();
        //获取好友对象
        ThreadUtil.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                //  直接获取
                roster = MyApp.conn.getRoster();
                // 添加好友对象的监听
                roster.addRosterListener(rosterListener);
                //添加到数据
//                1. 如果之前有 就更新   //  直接更新     count = 0
                // 2.如果没有就插入
                for (RosterEntry entry : roster.getEntries()) {
                    // ctrl + alt + m
                    saveOrUpdateRosterEntry(entry);
                }
            }
        });
    }

    //CRUD
    class MyRosterListener implements RosterListener {
        //  C
        @Override
        public void entriesAdded(Collection<String> collection) {
            System.out.println("添加好友 ");
            for (String account : collection) {
                //根据帐号 创建  RosterEntry 对象
                RosterEntry entry = roster.getEntry(account);
                saveOrUpdateRosterEntry(entry);
            }
        }

        private void print(Collection<String> collection) {
            for (String account : collection) {
                System.out.println("  account =" + account);
            }
        }

        //U
        @Override
        public void entriesUpdated(Collection<String> collection) {
            System.out.println("重命名 ");
            for (String account : collection) {
                //根据帐号 创建  RosterEntry 对象
                RosterEntry entry = roster.getEntry(account);
                saveOrUpdateRosterEntry(entry);
            }
//            print(collection);
        }

        //D
        @Override
        public void entriesDeleted(Collection<String> collection) {
            System.out.println("删除好友 ");
            for (String account : collection) {
                //根据帐号 创建  RosterEntry 对象
                getContentResolver().delete(ContactProvider.CONTACT_URI,
                        ContactProvider.Contact.ACCOUNT + "=?", new String[]{account});
            }
        }

        // 好友状态改变
        @Override
        public void presenceChanged(Presence presence) {

        }
    }

    /**
     * 既可以插入操作 又可以做更新操作
     *
     * @param entry
     */
    private void saveOrUpdateRosterEntry(RosterEntry entry) {
        ContentValues values = new ContentValues();
        values.put(ContactProvider.Contact.ACCOUNT, entry.getUser());
        values.put(ContactProvider.Contact.NICK, getNick(entry.getUser(), entry.getName()));
        values.put(ContactProvider.Contact.AVATAR, 0);
        // ctrl +shift + u
        values.put(ContactProvider.Contact.SORT, getPinyin(getNick(entry.getUser(), entry.getName())));
        //返回影响的行数
        int count = getContentResolver().update(ContactProvider.CONTACT_URI, values,
                ContactProvider.Contact.ACCOUNT + "=?", new String[]{entry.getUser()});
        if (count < 1) {
            //  做插入操作
            getContentResolver().insert(ContactProvider.CONTACT_URI, values);
        }
    }


    /**
     * 获取用户昵称
     *
     * @param account
     * @param nick
     * @return
     */
    public String getNick(String account, String nick) {
        if ("".equals(nick) || null == nick) {
            //cang@zz.itcast
            nick = account.substring(0, account.indexOf("@"));
        }
        return nick;
    }

    public String getPinyin(String nick) {
        //取消生效
        return PinyinHelper.convertToPinyinString(nick, "", PinyinFormat.WITHOUT_TONE).toUpperCase();
    }

}
