package zz.itcast.mediaplayer11.activity;

import zz.itcast.mediaplayer11.R;
import zz.itcast.mediaplayer11.utils.LogUtils;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
/**
 * Activity的基类
 * 1.规范代码结构
 * 2.处理相同逻辑 ：返回，点击之后关闭当前界面
 * 3.精简代码
 * @author wangdh
 *
 */
/**
 * Activity的基类
 * 1.规范代码结构
 * 2.处理相同逻辑 ：返回，点击之后关闭当前界面
 * 3.精简代码
 * @author wangdh
 *
 */
public abstract class BaseActivity extends FragmentActivity implements OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
//        Button btn = findViewById(R.id.xxx);
        initView();
//        btn.setOnClickListener(xxx);
        initListener();
//        btn.setText("返回");
        initData();
        registerCommonBtn();
        
    }

    /**
     * 获取Activity显示的布局：
     * @return：布局id
     */
    public abstract int getLayoutResID();
    /**
     * 初始化View：findViewById
     */
    public abstract void initView() ;
    /**
     * 初始化监听：点击监听、设置适配器、设置条目点击监听
     */
    public abstract void initListener() ;

    /**
     * 初始化数据
     */
    public abstract void initData() ;
 
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //如果点击的是返回按钮，关闭自己
            case R.id.back:
                finish();
            break;
            
            default:
                onInnerClick(v);
            break;
        }
    }

    /**
     * 注册相同逻辑
     * 返回按钮
     */
    private void registerCommonBtn() {
        View backView=  findViewById(R.id.back);
        if(backView!=null){
            backView.setOnClickListener(this);
        }
        
    }
    /**
     * 子类给控件设置点击监听，不要重写onClick需要重写此方法
     * 不是抽象：子类可能不需要处理某个控件的点击
     * @param v
     */
    public void onInnerClick(View v) {
        
    }
    /**
     * 弹出吐司
     * @param msg
     */
    public void toast(String msg){
        Toast.makeText(getApplicationContext(), 
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
