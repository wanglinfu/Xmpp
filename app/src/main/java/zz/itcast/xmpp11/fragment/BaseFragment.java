package zz.itcast.xmpp11.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zz.itcast.xmpp11.tools.ViewUtils;

/**
 * =========================================
 * 版权所有 违法必究
 * 作者: wxj.
 * <p/>
 * 工程: Xmpp11.
 * <p/>
 * 文件名: BaseFragment.
 * <p/>
 * 时间: 2016/3/21.
 * <p/>
 * 修订历史:
 * <p/>
 * 修订时间:
 * <p/>
 * =========================================
 */
public abstract class BaseFragment extends Fragment {
    private View view;
    //view  --  只能有一个父控件　　　　
    // 不允许有多个父控件　

    /**
     * 偶尔 获取  getActivity 为空
     */
    protected Context context;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewUtils.removeFromParent(view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (context == null){
            context = getActivity();
        }
        if (view == null) {
            view = createView(inflater, container, savedInstanceState);
        }
        return view;
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);//{

}
