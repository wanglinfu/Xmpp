package zz.itcast.xmpp11.tools;

import android.os.Handler;

/**
 * =========================================
 * 版权所有 违法必究
 * 作者: wxj.
 * <p/>
 * 工程: Xmpp11.
 * <p/>
 * 文件名: ThreadUtil.
 * <p/>
 * 时间: 2016/3/21.
 * <p/>
 * 修订历史:
 * <p/>
 * 修订时间:
 * <p/>
 * =========================================
 */
public class ThreadUtil {
    // Runnable   接口   线程任务类

    /**
     * 子线程执行
     * @param r
     */
    public static void runOnBackThread(Runnable r) {
        new Thread(r).start();
    }
    private static Handler handler = new Handler();

    /**
     * 主线程执行
     * @param r
     */
    public static  void runOnUiThread(Runnable r){
        handler.post(r);
    }
}
