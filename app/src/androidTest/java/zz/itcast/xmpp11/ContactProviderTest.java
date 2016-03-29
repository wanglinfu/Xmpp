package zz.itcast.xmpp11;

import android.content.ContentValues;
import android.test.InstrumentationTestCase;

import zz.itcast.xmpp11.provider.ContactProvider;

/**
 * =========================================
 * 版权所有 违法必究
 * 作者: wxj.
 * <p/>
 * 工程: Xmpp11.
 * <p/>
 * 文件名: ContactProviderTest.
 * <p/>
 * 时间: 2016/3/21.
 * <p/>
 * 修订历史:
 * <p/>
 * 修订时间:
 * <p/>
 * =========================================
 */
public class ContactProviderTest extends InstrumentationTestCase {


    public void textInsert1() {
        ContentValues values = new ContentValues();
        values.put(ContactProvider.Contact.ACCOUNT,"laowang@zz.itcast");
        values.put(ContactProvider.Contact.NICK,"老王");
        values.put(ContactProvider.Contact.AVATAR,0);
        // ctrl +shift + u
        values.put(ContactProvider.Contact.SORT, "LAOWANG");
        getInstrumentation().getContext().getContentResolver().insert(ContactProvider.CONTACT_URI,values);
    }
}
