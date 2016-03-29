package zz.itcast.mediaplayer11.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 主界面viewpager的适配器
 * @author wangdh
 *
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    /**
     * 数据源，需要通过构造传递
     */
    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    public MyFragmentPagerAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }}
