package zz.itcast.mediaplayer11.lyrc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 歌词解析者
 * 
 * @author wangdh
 * 
 */
public class LyrcParser {
    /**
     * 解析歌词文件
     * @param lyrcFile
     * @return
     */
    public static List<LyrcBean> parseLyrcFile(File lyrcFile) {
        List<LyrcBean> lyrcBeans = new ArrayList<LyrcBean>();
        // 如果文件不存在
        if (lyrcFile == null || !lyrcFile.exists()) {
            // 开始时间和演唱时长都是0
            lyrcBeans.add(new LyrcBean(0, "未找到歌词", 0));
            return lyrcBeans;
        }
        BufferedReader bufferedReader =null;//一行一行读取
        try {
            // 如果歌词文件存在
            // BufferedReader bufferedReader = new BufferedReader(new
            // FileReader(lyrcFile));
            // InputStreamReader第二个参数是歌词文件编码格式，正确编码（获取当前文件的编码）
            bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(lyrcFile), getCharset(lyrcFile)));
            //一行一行读取
            String lineContent ="";
            while((lineContent = bufferedReader.readLine())!=null){
                //解析每一行
                ArrayList<LyrcBean> lineList = parseLine(lineContent);
                lyrcBeans.addAll(lineList);
            }
            //对集合做排序
            Collections.sort(lyrcBeans);
            //获取演唱时长
            getDuration(lyrcBeans);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lyrcBeans;
    }
    /**
     *           //[01:32.23
                    [02:51.73
                    [03:50.85
                        And my heart will go on and on
                        
     *           [02:30.09
                    That the heart does go on
     *           
                //[04:02.75
                
                
     * @param lineContent
     * @return
     */
    private static ArrayList<LyrcBean> parseLine(String lineContent) {
        ArrayList<LyrcBean> lyrcBeans = new ArrayList<LyrcBean>();
        //按照右中括号拆分，将多个时间，拆分出来
        String[] split = lineContent.split("]");
        //如果只有一个开始时间，没有内容  [04:02.75]
        if(split.length==1){
            lyrcBeans.add(new LyrcBean(parseTime(split[0]), "", 0));
            return lyrcBeans;
        }
        //歌词内容  
        String content = split[split.length-1];
        /**
         *       //[01:32.23
                    [02:51.73
                    [03:50.85
                        And my heart will go on and on
         */
        //循环所有开始时间
        for (int i = 0; i <= split.length-2; i++) {
            int startTime = parseTime(split[i]);
            lyrcBeans.add(new LyrcBean(startTime, content, 0));
        }
        
        return lyrcBeans;
    }
    /**
     * 将string字符串的时间，转换为int类型ms值
     * @param string   [03:50.85
     * 1000ms = 1s
     * 100 xs = 1s
     * 1xs = 10ms
     * @return
     */
    private static int parseTime(String timeString) {
        try {
            //替换掉 [
            timeString = timeString.replace("[", "");
            String[] s1 = timeString.split(":");
            //分钟
            int min =Integer.parseInt(s1[0]);
            //50.85  --  50  85
            String[] s2 = s1[1].split("\\.");
            int s = Integer.parseInt(s2[0]);
            int xs = Integer.parseInt(s2[1]);
            return min*60*1000+s*1000+xs*10;
        } catch (Exception e) {
            return -1;
        }
    }
    /**
     * 获取演唱时长：
     * ：下一个歌词的开始时间-当前歌词的开始时间
     */
    private static void getDuration(List<LyrcBean> lyrcBeans) {
        for (int i = 0; i < lyrcBeans.size(); i++) {
            //当前歌词开始时间
            LyrcBean one = lyrcBeans.get(i);
            if((i+1)<lyrcBeans.size()){
                LyrcBean next = lyrcBeans.get(i+1);
                one.duration = next.startTime - one.startTime;
            }
        }
    }
    /**
     * 获取当前文件的编码格式 避免读取的歌词文件，是乱码
     * 
     * @param file
     * @return
     */
    public static String getCharset(File file) {
        String charset = "gbk";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF)
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF)
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }
    
}
