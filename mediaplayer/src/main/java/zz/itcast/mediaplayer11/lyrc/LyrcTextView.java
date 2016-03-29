package zz.itcast.mediaplayer11.lyrc;

import java.util.ArrayList;
import java.util.List;

import zz.itcast.mediaplayer11.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * 歌词同步控件
 * @author wangdh
 *
 */
public class LyrcTextView extends TextView {
    private Paint drawPaint = null;
    private int halfTextViewWidth;
    private int halfTextViewHeight;
    private List<LyrcBean> lyrcBeans = new ArrayList<LyrcBean>();
    private int currentLine = 0;//当前高亮行
    private int lineHeight;
    private int green;
    private int white;
    private int progress;

    public LyrcTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }


    public LyrcTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LyrcTextView(Context context) {
        super(context);
        initView();
    }
    /**
     * 初始化操作
     */
    private void initView() {
        //指定文字颜色，文字大小
        green = getResources().getColor(R.color.green);
        white = getResources().getColor(R.color.white);
//        int textSize = (int) getResources().getDimension(R.dimen.lyric_text_size);
//        int lineHeight = (int) getResources().getDimension(R.dimen.lyric_line_height);
        int textSize = 16;
        lineHeight = 19;
        drawPaint = new Paint();
        drawPaint.setColor(green);
        drawPaint.setTextSize(textSize);
        //假数据
//        for (int i = 0; i < 50; i++) {
//            lyrcBeans.add(new LyrcBean(2000*i, "我是歌词"+i, 0));
//        }
//        currentLine = 2;
    }
    /**
     * 1. 居中画一行内容 （高亮显示）
        2. 水平居中画多行内容
        3. 按行滚动
        4. 平滑滚动
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        drawSingleLine(canvas);
        //画没有找到歌词
        if(lyrcBeans.size()==1){
            //没有找到歌词
            drawSingleLine(canvas);
            return;
        }
        if(lyrcBeans.size()!=0){
            /**
             *  1. 计算当前行消耗的时间 （已经唱多长时间） 当前进度 - 自己的开始时间
                2. 滚动时间百分比 = 消耗时间 / 演唱时长
                2. 当前行消耗时间内滚动的距离：滚动时间的百分比*行高
                3. 滚 
             */
            //当前歌词
            LyrcBean currentLyrcBean = lyrcBeans.get(currentLine);
            int costTime = progress - currentLyrcBean.startTime;
            float costPrecent = (float)costTime / currentLyrcBean.duration;
            int costY = (int) (costPrecent* lineHeight);
            //滚
            canvas.translate(0, -costY);//dx、dy，x和y滚动的距离(负数网上滚)
            drawMutiLine(canvas);
        }
        
    }


    private void drawMutiLine(Canvas canvas) {
        for (int i = 0; i <lyrcBeans.size(); i++) {
            if(i==currentLine){//高亮
                drawPaint.setColor(green);
            }else{
                drawPaint.setColor(white);
            }
            
            String text = lyrcBeans.get(i).content;
            /**
             * x = textViewWidth/2 - textWidth/2
             * y = textViewHeight/2 + textHeight/2
             */
            //获取文本的大小
            //text文本内容，start、end就是获取的边框的范围
            Rect bounds = new Rect();//矩形
            drawPaint.getTextBounds(text, 0, text.length(), bounds);
            int halfTextWidth  = bounds.width()/2;
            int halfTextHeight = bounds.height()/2;
            int centerX = halfTextViewWidth - halfTextWidth;
            int centerY = halfTextViewHeight + halfTextHeight;
            int drawY = 0;
            //计算当前画的行，与高亮行，查几行
            int disLine = i - currentLine;
            //查距离：行数*行高
            int disY = disLine * lineHeight;
            drawY = centerY+disY;
            canvas.drawText(text, centerX, drawY, drawPaint);
        }
       
    }


    private void drawSingleLine(Canvas canvas) {
        //text 文字内容
        //x、y：文字位置
        //paint  指定文字颜色，文字大小
        String text = "未找到歌词";
        /**
         * x = textViewWidth/2 - textWidth/2
         * y = textViewHeight/2 + textHeight/2
         */
        //获取文本的大小
        //text文本内容，start、end就是获取的边框的范围
        Rect bounds = new Rect();//矩形
        drawPaint.getTextBounds(text, 0, text.length(), bounds);
        int halfTextWidth  = bounds.width()/2;
        int halfTextHeight = bounds.height()/2;
        int drawX = halfTextViewWidth - halfTextWidth;
        int drawY = halfTextViewHeight + halfTextHeight;
        canvas.drawText(text, drawX, drawY, drawPaint);
    }
    /**
     * 测量自己的大小 onMeasure  ---  ondraw
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //自己的宽度
        halfTextViewWidth = MeasureSpec.getSize(widthMeasureSpec)/2;
        halfTextViewHeight = MeasureSpec.getSize(heightMeasureSpec)/2;
        
    }
    /**
     * 根据当前进度，更新currentLine
     */
    public void updateCurrentLine(int progress){
        this.progress = progress;
        //如果是没有找到歌词
        if(lyrcBeans.size()==1){
            currentLine = 0;
            invalidate();
            return;
        }
        System.out.println("当前进度："+progress);
        for (int i = 0; i < lyrcBeans.size(); i++) {
            LyrcBean one = lyrcBeans.get(i);
            if((i+1)<lyrcBeans.size()){
                LyrcBean next = lyrcBeans.get(i+1);
                if(progress>one.startTime&&progress<next.startTime){
                    currentLine = i;
                    System.out.println("当前行："+currentLine);
                    break;
                }
            }
        }
        //ondraw
        invalidate();//刷新
        
    }
    public void setLyrcBeans(List<LyrcBean> list){
        this.lyrcBeans = list;
    }
    
    
}
