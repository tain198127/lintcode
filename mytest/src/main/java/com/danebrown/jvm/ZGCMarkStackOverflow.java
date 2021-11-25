package com.danebrown.jvm;

/**
 * Created by danebrown on 2021/10/27
 * mail: tain198127@163.com
 *推荐使用这个参数测试： -XX:+UnlockExperimentalVMOptions   -XX:+UseZGC -Xmx100g  -XX:ConcGCThreads=1 ZGCMarkStackOverflow
 * 这个可以复现的概率达到100%。   如果不使用的话，生产环境会偶现。
 * @author danebrown
 */
public class ZGCMarkStackOverflow {
    public static int OBJ_COUNT = 20000000;
    public static int ARRAY_LEN = 100;
    public static Object head = new Object();

    public static class Dummy {
        public Object[] field;
        Dummy() {
            field = new Object[ARRAY_LEN];
        }
    }

    public static void setup() {
        for (int i = 0; i < OBJ_COUNT; i++) {
            Dummy obj = new Dummy();
            for (int j = 0; j < ARRAY_LEN - 1; j++) {
                obj.field[j] = obj;
            }
            obj.field[ARRAY_LEN - 1] = head;
            head = obj;
        }
    }

    public static void main(String[] args) {
        setup();
        System.gc();
        System.out.println("pass");
    }
}
