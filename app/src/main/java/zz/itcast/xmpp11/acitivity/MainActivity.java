package zz.itcast.xmpp11.acitivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zz.itcast.xmpp11.R;
import zz.itcast.xmpp11.fragment.BuddyListFragment;
import zz.itcast.xmpp11.fragment.SessionFragment;
import zz.itcast.xmpp11.tools.Tabs;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private LinearLayout ll_tabs;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        fragments.add(new SessionFragment());
        fragments.add(new BuddyListFragment());
        viewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        final Tabs tabs = new Tabs();
        viewpager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    tv_title.setText("会话");
                } else {
                    //alt + shift + ↑   ↓
                    tv_title.setText("好友");
                }
                tabs.changeTab(position);
            }
        });


        tabs.createTabs(ll_tabs, R.layout.view_tab, new String[]{"会话", "好友"},
                new int[]{R.drawable.tab_message_btn, R.drawable.tab_selfinfo_btn});
        tabs.setOnTabClickListener(new Tabs.OnTabClickListener() {
            @Override
            public void onItemClick(int position) {
                viewpager.setCurrentItem(position);
            }
        });
    }

    private void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        ll_tabs = (LinearLayout) findViewById(R.id.ll_tabs);

        tv_title = (TextView) findViewById(R.id.tv_title);

    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
