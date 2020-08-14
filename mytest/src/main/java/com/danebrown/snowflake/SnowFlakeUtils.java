package com.danebrown.snowflake;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by danebrown on 2020/7/28
 * mail: tain198127@163.com
 */
public class SnowFlakeUtils {

    /**
     * 时间byte长度
     */
    private static final int TIME_LEN = 41;
    /**
     * 数据长度
     */
    private static final int DATA_LEN = 5;
    /**
     * worker id长度
     */
    private static final int WORK_LEN = 5;
    /**
     * 序号长度
     */
    private static final int SEQ_LEN = 12;

//    private static DateTimeFormatter dataFormat = DateTimeFormat.forPattern("yyyyMMddHHmmssSSS");
//    private static String str = dataFormat.print(DateTime.now());

    /**
     * 起始时间1981-12-27
     */
    private static final long START_TIME = 380908800000L;
//    static{
//
//        GregorianCalendar gregorianCalendar = new GregorianCalendar(1981,12,27);
//        System.out.println(gregorianCalendar.getTimeInMillis());
//
//
//    }
    /**
     * 时间部分向左移动位数
     */
    private static final int TIME_LEFT_BIT = 61 - 1 - TIME_LEN;
    /**
     * 数据中心ID，0-31之间
     */
    private static final long DATA_ID = getDataId();
    /**
     * 机器ID（0-31之间）
     */
    private static final long WORK_ID = getWorkId();
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
    private static final byte ldus = ('L');
    private static final byte cdus = ('C');
    private static final byte gdus = ('G');
    private static final byte bdus = ('B');
    private static final byte idch = ('H');
    private static final byte idcy = ('Y');
    private static final byte idcf = ('F');
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
    private static long getDataId() {

        return 31;
    }

    /**
     * 获取机器ID
     *
     * @return
     */
    private static long getWorkId() {
        return 18;
    }

    public static long genID() {
//        DateTimeFormatter dataFormat = DateTimeFormat.forPattern("yyyyMMddHHmmssSSS");
//        String str = dataFormat.print(DateTime.now());

//        long now = Long.parseLong(str);
        long now = System.currentTimeMillis();
        /**
         * 发生时钟回拨
         */
        if (now < LAST_TIME_STAMP) {
            //重新计算WORKID
        }
        //同一毫秒内的请求
        if (now == LAST_TIME_STAMP) {
            LAST_SEQ = (LAST_SEQ + 1) & SEQ_MAX_NUM;
        } else {
            //时间不同则序号重置
            LAST_SEQ = 0;
        }
        LAST_TIME_STAMP = now;
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(now);
//        stringBuilder.append(DATA_ID);
//        stringBuilder.append(WORK_ID);
//        stringBuilder.append(LAST_SEQ);
//     return   Long.parseLong(stringBuilder.toString());
        /**
         * 当前时间-起始时间在 左移（61-1-时间长度）
         *
         */
//
        return ((now - START_TIME) << TIME_LEFT_BIT) | (DATA_ID << DATA_LEFT_BIT) | (WORK_ID << WORK_LEFT_BIT) | LAST_SEQ;
    }

    public static int getIDByString(String mixDusStr) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Preconditions.checkArgument(!Strings.isNullOrEmpty(mixDusStr));
        Preconditions.checkArgument(mixDusStr.length() == 5);
        stopwatch.stop();
        System.out.println("check:" + stopwatch.elapsed(TimeUnit.NANOSECONDS) + "纳秒");
        stopwatch.start();

        char[] mixDusChars = mixDusStr.toUpperCase().toCharArray();
        byte dusType = (byte) mixDusChars[0];
        byte idcNum = (byte) mixDusChars[4];
        int dusId = Integer.parseInt(mixDusStr.substring(1, 4));
        stopwatch.stop();
        System.out.println("parseInt:" + stopwatch.elapsed(TimeUnit.NANOSECONDS) + "纳秒");
        stopwatch.start();
        int checkIsCorrectDusType = (dusType - ldus) & (dusType - cdus) & (dusType - gdus) & (dusType - bdus);
        int checkIsCorrectIdcType = (idcNum - idcf) & (idcNum - idch) & (idcNum - idcy);

        Preconditions.checkArgument(checkIsCorrectDusType + checkIsCorrectIdcType == 0, "错误的类型");
        System.out.println("checkArgument:" + stopwatch.elapsed(TimeUnit.NANOSECONDS) + "纳秒");

        int compondResult = (dusType % 7) * 10000 + dusId * 10 + (idcNum + 1) % 4;
        System.out.println("计算:" + stopwatch.elapsed(TimeUnit.NANOSECONDS) + "纳秒");

        stopwatch.stop();
        System.out.println(stopwatch.elapsed(TimeUnit.NANOSECONDS) + "纳秒");
        return compondResult;

    }


    public static void main(String[] args) {
        HashSet<Long> ids = new HashSet<>();
        long start = System.nanoTime();
        int realId = getIDByString("B001H");
        Set<String> dusType = new HashSet<>();
        dusType.add("B");
        dusType.add("L");
        dusType.add("G");
        dusType.add("C");
        dusType.add("b");
        dusType.add("l");
        dusType.add("g");
        dusType.add("c");
        Set<String> idc = new HashSet<>();
        idc.add("H");
        idc.add("Y");
        idc.add("F");
        idc.add("h");
        idc.add("y");
        idc.add("f");
        for (String dus : dusType) {
            for (String dc : idc) {
                String dusanddc = String.format("%s%s%s", dus, "010", dc);
                int num = getIDByString(dusanddc);
                System.out.println(dusanddc + "-->" + num);
            }
        }
//        System.out.println(realId);


//        for(int i =0;i<3000000;i++) {
//            ids.add(SnowFlakeUtils.genID());
//        }
        System.out.println(SnowFlakeUtils.genID());
        long end = System.nanoTime();
        System.out.println(String.format("共生成%d条，耗时:%d毫秒", ids.size(), (end - start) / (1000 * 1000)));
    }

}
