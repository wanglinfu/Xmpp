package zz.itcast.mediaplayer11.lyrc;
/**
 * 歌词对应的Javabean
 * @author wangdh
 *
 */
public class LyrcBean implements Comparable<LyrcBean> {
    /**
     * 开始时间
     * 歌词内容
     * 演唱时长
     */
    public int startTime;
    public String content;
    public int duration;
    @Override
    public String toString() {
        return "LyrcBean [startTime=" + startTime + ", content=" + content + ", duration=" + duration + "]";
    }
    public LyrcBean(int startTime, String content, int duration) {
        super();
        this.startTime = startTime;
        this.content = content;
        this.duration = duration;
    }
    @Override
    public int compareTo(LyrcBean another) {
        //startTime
        return startTime - another.startTime;//正序
    }
    
    
}
