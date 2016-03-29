package zz.itcast.mediaplayer11.fragment;

import zz.itcast.mediaplayer11.utils.LogUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
/**
 * Fragment的基类
 * 1.规范代码结构
 * 2.精简代码
 * @author wangdh
 *
 */
public abstract class BaseFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), getLayoutResID(), null);
        initView(view);
        initListener();
        initData();
        return view;
    }

    /**
     * 获取Activity显示的布局：
     * @return：布局id
     */
    public abstract int getLayoutResID();
    /**
     * 初始化View：findViewById
     */
    public abstract void initView(View view) ;
    /**
     * 初始化监听：点击监听、设置适配器、设置条目点击监听
     */
    public abstract void initListener() ;

    /**
     * 初始化数据
     */
    public abstract void initData() ;
 
    
    /**
     * 弹出吐司
     * @param msg
     */
    public void toast(String msg){
        Toast.makeText(getActivity(), 
                msg, Toast.LENGTH_SHORT).show();
    }
    /**
     * 自定义 log方法
     * @param msg
     * tag：就是当前类名
     */
    public void logI(String msg){
        LogUtils.i(getClass(), msg);
    }

    
}
