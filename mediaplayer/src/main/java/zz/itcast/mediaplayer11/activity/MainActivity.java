package zz.itcast.mediaplayer11.activity;

import java.util.ArrayList;
import java.util.List;

import zz.itcast.mediaplayer11.R;
import zz.itcast.mediaplayer11.adapter.MyFragmentPagerAdapter;
import zz.itcast.mediaplayer11.fragment.AudioListFragment;
import zz.itcast.mediaplayer11.fragment.VideoListFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class MainActivity extends BaseActivity {
    /**
     * viewpager界面改变监听
     * @author wangdh
     *
     */
    private final class OnVideoPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            //标签颜色改变
            //获取颜色
            int green = getResources().getColor(R.color.green);
            int halfwhite = getResources().getColor(R.color.halfwhite);
            main_tv_video.setTextColor(position==0?green:halfwhite);
            main_tv_audio.setTextColor(position==1?green:halfwhite);
            
            //标签做缩放动画  (animate()要做动画的控件)
            ViewPropertyAnimator.animate(main_tv_video).scaleX(position==0?1.2f:1.0f).scaleY(position==0?1.2f:1.0f);
            ViewPropertyAnimator.animate(main_tv_audio).scaleX(position==1?1.2f:1.0f).scaleY(position==1?1.2f:1.0f);
            
        }
        /**
         * 滚动过程中的回调
         * 1.position：屏幕上第一个可见的界面索引
         * 2.positionOffset：手指在屏幕上滚过距离的百分比
         * 3.positionOffsetPixels：手指在屏幕上滚过的距离
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            logI("position:"+position);
//            logI("positionOffset:"+positionOffset);
            /**
             * 偏移距离：百分比*指示器的宽度
             * 起始位置：position*指示器的宽度
             * 最终位置：起始位置+偏移距离
             */
            float offsetX = positionOffset*main_indicate_line.getWidth();
            float startX = position*main_indicate_line.getWidth();
            float endX = startX+offsetX;
            logI("offsetX:"+offsetX+"，startX:"+startX+"，endX:"+endX);
            ViewHelper.setTranslationX(main_indicate_line,endX);
        }
        
        @Override
        public void onPageScrollStateChanged(int state) {
            // TODO Auto-generated method stub
            
        }
    }

    //主界面的viewpager
    private ViewPager main_viewpager;
    //数据源
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private MyFragmentPagerAdapter adapter;
    private TextView main_tv_video;
    private TextView main_tv_audio;
    private View main_indicate_line;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        //初始化Vitamio库
//        if (!LibsChecker.checkVitamioLibs(this))
//            return;
        main_viewpager = (ViewPager) findViewById(R.id.main_viewpager);
        main_tv_video = (TextView) findViewById(R.id.main_tv_video);
        main_tv_audio = (TextView) findViewById(R.id.main_tv_audio);
        main_indicate_line = findViewById(R.id.main_indicate_line);
        
        //初始化 视频标签 变大
        ViewPropertyAnimator.animate(main_tv_video).scaleX(1.2f).scaleY(1.2f);
        
       
    }

    @Override
    public void initListener() {
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        main_viewpager.setAdapter(adapter);
        main_viewpager.setOnPageChangeListener(new OnVideoPageChangeListener());
        main_tv_video.setOnClickListener(this);
        main_tv_audio.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //初始化数据源
        fragmentList.add(new VideoListFragment());
        fragmentList.add(new AudioListFragment());
        //刷新adapter
        adapter.notifyDataSetChanged();
        //初始化指示器的宽度：main_indicate_line
        initIndiateWidth();
    }

    /**
     * 初始化Indicator的宽度：屏幕的一半
     */
    
    private void initIndiateWidth() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
//        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        layoutParams.width = screenWidth/2;
//        main_indicate_line.setLayoutParams(layoutParams);
          main_indicate_line.getLayoutParams().width = screenWidth/fragmentList.size();
          //刷新：
          main_indicate_line.invalidate();//onDraw()
//          main_indicate_line.postInvalidate();//子线程中调用
//          main_indicate_line.requestLayout();//刷新，onMeasure、onLayout、onDraw
    }
    @Override
    public void onInnerClick(View v) {
        super.onInnerClick(v);
        switch (v.getId()) {
            case R.id.main_tv_video:
                main_viewpager.setCurrentItem(0);
            break;
            case R.id.main_tv_audio:
            
            main_viewpager.setCurrentItem(1);
            break;
            
            default:
            break;
        }
    }
    
    
}
