package zz.itcast.xmpp11.tools;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * =========================================
 * 版权所有 违法必究
 * 作者: wxj.
 * <p/>
 * 工程: Xmpp11.
 * <p/>
 * 文件名: ViewUtils.
 * <p/>
 * 时间: 2016/3/21.
 * <p/>
 * 修订历史:
 * <p/>
 * 修订时间:
 * <p/>
 * =========================================
 */
public class ViewUtils {
    /**
     * 将当前控件从父控件中移除
     * @param view
     */
    public static void removeFromParent(View view){
        if (view == null){
            return;
        }
        ViewParent parent = view.getParent();
        if (parent != null && parent instanceof ViewGroup){
            ViewGroup group = (ViewGroup) parent;
            group.removeView(view);
        }
    }
}
