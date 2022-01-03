package com.danebrown.jvm;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


/**
 * Created by danebrown on 2021/7/13
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Slf4j
public class JOLTest {
    /**
     * 打印对象占用内存信息
     * @param object
     * @param mesg
     */
    public static void printObjSize(Object object, String mesg) {
        ClassLayout classLayout = ClassLayout.parseInstance(object);
        GraphLayout graphLayout = GraphLayout.parseInstance(object);
        System.out.println(mesg);
        System.out.println("all size ["+graphLayout.totalSize()+"] bytes");
        System.out.println("head size ["+classLayout.headerSize()+"]");
        System.out.println("content size ["+(graphLayout.totalSize()-classLayout.headerSize())+"]");

        if(graphLayout.totalSize() <= 100) {
            System.out.println("struct:↓");
            System.out.println(classLayout.toPrintable());
        }
//        System.out.println(graphLayout.toPrintable());
        System.out.println("--------------------------");
    }

    public static void main(String[] args) {

        int[][] multiDimArr = new int[128][2];
        for (int i = 0; i < multiDimArr.length; i++) {
            Arrays.fill(multiDimArr[i], 1);
        }
        printObjSize(multiDimArr, "多维数组new int[2][128]");

        int[] signalArr = new int[256];
        Arrays.fill(signalArr, 1);
        printObjSize(signalArr, "1维数组new int[256]");

        String str = "1";
        printObjSize(str, "String '1'");

        Thread thread = new Thread(() -> {
            System.out.println("I'm thread");
        });
        //        thread.start();

        printObjSize(thread, "new Thread");

        Object obj = new Object();
        printObjSize(obj,"普通对象");

        synchronized (obj){
            printObjSize(obj,"经过synchronized修饰的obj");
        }
        printObjSize(obj,"synchronized完成后的obj");
        log.warn("synchronized 的锁信息被记录在instance的头8个字节-markword上了；markword里面还记录了*gc*信息、hashcode信息等");


        int len = 500000;
        String firstMapIdx = "1";
        String secondMapIdx = "2";
        ArrayList<Object> arrayList = new ArrayList<Object>();
        Map<String,Object> map = new HashMap<>();
        Object[] array = new Object[len];
        Map<String,Map<String,Object>> inhericMap= new HashMap<>();
        inhericMap.put(firstMapIdx,new HashMap<>());
        inhericMap.put(secondMapIdx,new HashMap<>());
        for(int i =0; i < len;i++){
            arrayList.add(new Object());
            map.put(String.valueOf(i),new Object());
            array[i] = new Object();
            if(i < len/2){
                inhericMap.get(firstMapIdx).put(String.valueOf(i),new Object());
            }else{
                inhericMap.get(secondMapIdx).put(String.valueOf(i),
                        new Object());
            }
        }
        printObjSize(arrayList,len+"数据量arraylist 对象大小");
        printObjSize(map,len+"数据量 map 对象大小");
        printObjSize(array,len+"数据量 数组对象大小");
        printObjSize(inhericMap,len+"数据量 二级map对象大小");


    }
}
