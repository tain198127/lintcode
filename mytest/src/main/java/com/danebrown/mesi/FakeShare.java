package com.danebrown.mesi;


//import sun.misc.Contended;

//import jdk.internal.vm.annotation.Contended;

//import sun.misc.Contended;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

/**
 * Created by danebrown on 2021/9/9
 * mail: tain198127@163.com
 * 伪共享测试
 * 如果编译不过去，可以在Build/compiler/java compiler/pre module中增加
 * -parameters  --add-exports=java.base/jdk.internal.vm.annotation=ALL-UNNAMED  --add-exports=java.base/sun.net=ALL-UNNAMED  --add-exports=java.base/sun.net.util=ALL-UNNAMED  --add-exports=java.base/jdk.internal.misc=ALL-UNNAMED  --add-exports=java.base/sun.net.www=ALL-UNNAMED
 *
 * @author danebrown
 */

public class FakeShare {
    private static Object p3Lock = new Object();
    private static Object p4Lock = new Object();
    private static long COUNT_VAL = 10_000_000L;
    private volatile long p1;

    private long p3;

    private volatile long[] p5 = new long[15];

    private volatile AtomicInteger p7 = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        FakeShare fakeShare = new FakeShare();
        fakeShare.fakeShare();
        fakeShare.synchronizedUnFakeShare();
        fakeShare.paddingUnFakeShare();
        fakeShare.atomicfakeShare();
    }

    public void fakeShare() throws InterruptedException {
        long count = COUNT_VAL;
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (p1 < count) {
                    p1++;
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (p1 < count) {
                    p1++;
                }
            }
        });
        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.printf("伪共享耗时:%d毫秒\n", System.currentTimeMillis() - start);

    }

    public void atomicfakeShare() throws InterruptedException {
        long count = COUNT_VAL;
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (p7.incrementAndGet()<count){
                    ;;
                }
//                p7.updateAndGet(new IntUnaryOperator() {
//                    @Override
//                    public int applyAsInt(int operand) {
//                        if (operand < count){
//                            return operand +1;
//                        }
//                        return operand;
//                    }
//                });

            }
        });
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (p7.incrementAndGet()<count){
                    ;;
                }
//                p7.updateAndGet(new IntUnaryOperator() {
//                    @Override
//                    public int applyAsInt(int operand) {
//                        if (operand < count){
//                            return operand +1;
//                        }
//                        return operand;
//                    }
//                });

            }
        });
        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.printf("atomic伪共享耗时:%d毫秒\n",
                System.currentTimeMillis() - start);
    }

    public void synchronizedUnFakeShare() throws InterruptedException {
        long count = COUNT_VAL;
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (p3 < count) {
                    synchronized (p3Lock){
                        p3++;
                    }
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (p3 < count) {
                    synchronized (p4Lock){
                        p3++;
                    }
                }
            }
        });
        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.printf("synchronized非伪共享耗时:%d毫秒\n", System.currentTimeMillis() - start);

    }

    public void paddingUnFakeShare() throws InterruptedException {
        long count = COUNT_VAL;
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (p5[7] < count) {
                    p5[7]++;
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (p5[7] < count) {
                    p5[7]++;
                }
            }
        });
        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.printf("Padding结构非伪共享耗时:%d毫秒\n", System.currentTimeMillis() - start);

    }
}
