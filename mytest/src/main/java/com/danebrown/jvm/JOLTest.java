package com.danebrown.jvm;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

import java.lang.instrument.Instrumentation;
import java.util.*;
import java.util.function.Supplier;


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
        System.out.println("content size ["+(graphLayout.totalSize()-classLayout.headerSize())+"] bytes");
        System.out.println("content size ["+(graphLayout.totalSize()-classLayout.headerSize())/(1024*1024)+"] MB");

        if(graphLayout.totalSize() <= 100) {
            System.out.println("struct:↓");
            System.out.println(classLayout.toPrintable());
        }
//        System.out.println(graphLayout.toPrintable());
        System.out.println("--------------------------");
    }
    @Data
    public static class InnerMap6{
        private int age;
        private String name;
        private String address;
        private String address1;
        private String address2;
        private String address3;
    }
    @Data
    public static class InnerMap5{
        private int age;
        private String name;
        private String address;
        private String address1;
        private String address2;
    }
    @Data
    public static class InnerMap4{
        private int age;
        private String name;
        private String address;
        private String address1;
    }
    @Data
    public static class InnerMap3{
        private int age;
        private String name;
        private String address;
    }

    @Data
    public static class InnerMap2{
        private int age;
        private String name;
    }
    @Data
    public static class InnerMap1{
        private String name;
    }
    @Data
    public static class MixInnerMap{
        private InnerMap5 innerMap5;
        private InnerMap6 innerMap6;
        private InnerMap4 innerMap4;
        private InnerMap3 innerMap3;
        private InnerMap2 innerMap2;
        private InnerMap1 innerMap1;
    }
    @Data
    public static class InheriteMixInnerMap extends MixInnerMap{
        private String testAge;
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

//        printObjSize(thread, "new Thread");

        Object obj = new Object();
        printObjSize(obj,"普通对象");

        synchronized (obj){
            printObjSize(obj,"经过synchronized修饰的obj");
        }
        printObjSize(obj,"synchronized完成后的obj");
        log.warn("synchronized 的锁信息被记录在instance的头8个字节-markword上了；markword里面还记录了*gc*信息、hashcode信息等");


        printObjSize(new InnerMap6(),"6个字段的对象大小");
        printObjSize(new InnerMap5(),"5个字段的对象大小");
        printObjSize(new InnerMap4(),"4个字段的对象大小");
        printObjSize(new InnerMap3(),"3个字段的对象大小");
        printObjSize(new InnerMap2(),"2个字段的对象大小");
        printObjSize(new InnerMap1(),"1个字段的对象大小");
        printObjSize(new MixInnerMap(), "复合对象展会用空间");
        printObjSize(new InheriteMixInnerMap(), "继承的复合对象占用空间");


        int len = 500000;
        String firstMapIdx = "1";
        String secondMapIdx = "2";
        ArrayList<Object> arrayList = new ArrayList<Object>();
        Map<String,Object> map = new HashMap<>();
        Object[] array = new Object[len];
        Map<String,Map<String,Object>> inhericMap= new HashMap<>();
        inhericMap.put(firstMapIdx,new HashMap<>());
        inhericMap.put(secondMapIdx,new HashMap<>());
        Supplier<String> generate = new Supplier<String>() {
            @Override
            public String get() {

                String string = UUID.randomUUID().toString()+UUID.randomUUID().toString()+UUID.randomUUID().toString()+UUID.randomUUID().toString();
                return string;
            }
        };
        String conenteLength = generate.get();
        for(int i =0; i < len;i++){
            arrayList.add(generate.get());
            map.put(String.valueOf(i),generate.get());
            array[i] = generate.get();
            if(i < len/2){
                inhericMap.get(firstMapIdx).put(String.valueOf(i),generate.get());
            }else{
                inhericMap.get(secondMapIdx).put(String.valueOf(i),
                        generate.get());
            }
        }
        printObjSize(arrayList,len+"条数据量arraylist 对象大小,每个元素字符长度:"+conenteLength.length());
        printObjSize(map,len+"条数据量 map 对象大小,每个元素字符长度:"+conenteLength.length());
        printObjSize(array,len+"条数据量 数组对象大小,每个元素字符长度："+conenteLength.length());
        printObjSize(inhericMap,len+"条数据量 二级map对象大小，每个元素字符长度:"+conenteLength.length());


    }
}
