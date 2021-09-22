package com.danebrown.mesi;


//import sun.misc.Contended;

//import jdk.internal.vm.annotation.Contended;

//import sun.misc.Contended;

/**
 * Created by danebrown on 2021/9/9
 * mail: tain198127@163.com
 * 伪共享测试
 * 如果编译不过去，可以在Build/compiler/java compiler/pre module中增加
 * -parameters  --add-exports=java.base/jdk.internal.vm.annotation=ALL-UNNAMED  --add-exports=java.base/sun.net=ALL-UNNAMED  --add-exports=java.base/sun.net.util=ALL-UNNAMED  --add-exports=java.base/jdk.internal.misc=ALL-UNNAMED  --add-exports=java.base/sun.net.www=ALL-UNNAMED
 * @author danebrown
 */

public class FakeShare {
    private volatile long p1;
    private volatile long p2;
//    @Contended
    private volatile long p3;
//    @Contended
    private volatile long p4;

    private volatile long[] p5 = new long[15];
    private volatile long[] p6 = new long[15];
    private static long count=10_000_000L;
    public void fakeShare() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(p1 < count){
                    p1++;
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (p2 < count){
                    p2++;
                }
            }
        });
        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.printf("伪共享耗时:%d毫秒\n",System.currentTimeMillis()-start);

    }

    public void contentedUnFakeShare() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(p3 < count){
                    p3++;
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (p4 < count){
                    p4++;
                }
            }
        });
        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.printf("非伪共享耗时:%d毫秒\n",System.currentTimeMillis()-start);

    }

    public void paddingUnFakeShare() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(p5[7] < count){
                    p5[7]++;
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (p6[7] < count){
                    p6[7]++;
                }
            }
        });
        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.printf("Padding结构非伪共享耗时:%d毫秒\n",System.currentTimeMillis()-start);

    }
    public static void main(String[] args) throws InterruptedException {
        FakeShare fakeShare = new FakeShare();
        fakeShare.fakeShare();
        fakeShare.contentedUnFakeShare();
        fakeShare.paddingUnFakeShare();
    }
}
