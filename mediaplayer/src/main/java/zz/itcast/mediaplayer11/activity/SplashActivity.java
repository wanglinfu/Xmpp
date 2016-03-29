package zz.itcast.mediaplayer11.activity;

import zz.itcast.mediaplayer11.R;
import android.content.Intent;
import android.os.Handler;
/**
 * 闪屏界面
 * 1.页面跳转逻辑：2s跳转到主界面
 * 2.检查版本更新
 * 3.检查网络是否可用，sd卡是否可用
 * @author wangdh
 *
 */
public class SplashActivity extends BaseActivity {
    
    private Handler handler = new Handler();
    
    @Override
    public int getLayoutResID() {
        return R.layout.splash;
    }
    
    @Override
    public void initView() {
        
    }
    
    @Override
    public void initListener() {
        
    }
    
    @Override
    public void initData() {
        
    }
    @Override
    protected void onResume() {
        super.onResume();
        //发送一个延迟的Runnable任务代码块，到主线程执行
        handler.postDelayed(new Runnable()
        {
            
            @Override
            public void run() {
                //跳转到主界面
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                //关闭自己
                finish();
            }
        }, 2000);
    }
    
}
