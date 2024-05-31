package com.danebrown.tree;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TMTest {
    public static void main(String[] args) {
        test("10101N1B001",true);
        test("10101N1B001",false);
        test("10101",true);
        test("10101",false);
        test("10",true);
        test("10",false);
        test("1",true);
        test("1",false);
    }
    public static void test(String workid, boolean isPreHeat) {
        TreeMap<String,String> treeMap = new TreeMap<>();
        List<String> keySet = new ArrayList<>();
        String date = "20231010131359";
        String sysid = "102219";
        //            String unitid = "N1B001";
    
        String dir = "01";
        for(int i = 0;i<10000000;i++){
            
            String seq = StringUtils.leftPad(String.valueOf(i),5,"0");
            String key = date+workid+sysid+dir+seq;
            keySet.add(key);
            treeMap.put(key, UUID.randomUUID().toString());
        }
        long avg = 0;
        int epochTimes = 100;
        int length = keySet.get(0).length();
        int times = 100000;
        for(int epoch = 0; epoch < epochTimes; epoch++){
            
            long start = System.currentTimeMillis();
            
            for (int i = 0; i < times; i++){
                ThreadLocalRandom random = ThreadLocalRandom.current();
                String key = keySet.get(random.nextInt(0,keySet.size()-1));
                String value = treeMap.get(key);
            }
            long end = System.currentTimeMillis();
            avg+=end-start;
//            System.out.printf("长度[%d]的B+树搜索[%d]次耗时[%s]毫秒\n",length,times,(end-start));
        }
        if(isPreHeat){
            return;
        }
        System.out.printf("长度位[%d]的B+树[%d]次搜索平均耗时:%s\n",length,times,(double) avg/epochTimes);
        
        
    }
    
}
