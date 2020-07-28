package com.danebrown.snowflake;

import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import static org.joda.time.format.DateTimeFormat.forPattern;

/**
 * Created by danebrown on 2020/7/28
 * mail: tain198127@163.com
 */
public class SnowFlakeUtils {

    /**
     * 时间长度
     */
    private static final int TIME_LEN = 17;
    /**
     * 数据长度
     */
    private static final int DATA_LEN = 7;
    /**
     * worker id长度
     */
    private static final int WORK_LEN = 7;
    /**
     * 序号长度
     */
    private static final int SEQ_LEN = 6;

    private static DateTimeFormatter dataFormat = DateTimeFormat.forPattern("yyyyMMddHHmmssSSS");
    private static String str = dataFormat.print(DateTime.now());

    /**
     * 起始时间
     */
    private static final long START_TIME =  Long.parseLong(str);;
    /**
     * 剩余时间
     */
    private static final int TIME_LEFT_BIT = 61 - 1 - TIME_LEN;
    /**
     * 数据中心ID，0-31之间
     */
    private static final String DATA_ID = getDataId();


    /**
     * 机器ID（0-31之间）
     */
    private static final String WORK_ID = getWorkId();


    /**
     * 数据中心最大值31
     */
    private static final int DATA_MAX_NUM = ~(-1 << DATA_LEN);
    /**
     * 机器ID最大值31
     */
    private static final int WORK_MAX_NUM = ~(-1 << WORK_LEN);
    /**
     * 随机获取数据中心ID，32位
     */
    private static final int DATA_RANDOM = DATA_MAX_NUM + 1;
    /**
     * 随机获取机器ID，32位
     */
    private static final int WORK_RANDOM = DATA_RANDOM + 1;
    /**
     * 数据中心左移位数，17
     */
    private static final int DATA_LEFT_BIT = TIME_LEFT_BIT - DATA_LEN;
    /**
     * 机器ID左移位数，12
     */
    private static final int WORK_LEFT_BIT = DATA_LEFT_BIT - WORK_LEN;

    /**
     * 毫秒内最大序列值 4095
     */
    private static final long SEQ_MAX_NUM = ~(-1 << SEQ_LEN);
    /**
     * 上一毫秒内的序列值
     */
    private static long LAST_SEQ = 0L;
    /**
     * 上次生成的雪花的时间戳
     */
    private static long LAST_TIME_STAMP = -1L;

    /**
     * 获取数据中心ID
     *
     * @return
     */
    private static String getDataId() {

        return String.format("%02d", 2);
    }

    /**
     * 获取机器ID
     *
     * @return
     */
    private static String getWorkId() {
        return String.format("%07d",1);
    }

    public synchronized static String genID() {
        DateTimeFormatter dataFormat = DateTimeFormat.forPattern("yyyyMMddHHmmssSSS");
        String str = dataFormat.print(DateTime.now());

        long now = Long.parseLong(str);
        /**
         * 发生时钟回拨
         */
        if (now < LAST_TIME_STAMP) {
            //重新计算WORKID
            LAST_SEQ = (LAST_SEQ + 1) & SEQ_MAX_NUM;
        }
        //同一毫秒内的请求
        if (now == LAST_TIME_STAMP) {
            LAST_SEQ = (LAST_SEQ + 1) & SEQ_MAX_NUM;
        }else{
            LAST_SEQ=0;
        }
        LAST_TIME_STAMP=now;
        StringBuffer stringBuilder = new StringBuffer();
        stringBuilder.append(now);
        stringBuilder.append(DATA_ID);
        stringBuilder.append(WORK_ID);
        stringBuilder.append(LAST_SEQ);
     return   stringBuilder.toString();
//        return ((now-START_TIME)<<TIME_LEFT_BIT)|(DATA_ID<<DATA_LEFT_BIT)|(WORK_ID<<WORK_LEFT_BIT)|LAST_SEQ;
    }

    public static void main(String[] args) {
        HashSet<String> ids = new HashSet<>();
        long start=System.nanoTime();
//        for(int i =0;i<1000;i++) {
//            ids.add(SnowFlakeUtils.genID());
//        }
        System.out.println(SnowFlakeUtils.genID());
        long end = System.nanoTime();
        System.out.println(String.format("共生成%d条，耗时:%d微秒",ids.size(),(end-start)/(1000)));
    }

}
