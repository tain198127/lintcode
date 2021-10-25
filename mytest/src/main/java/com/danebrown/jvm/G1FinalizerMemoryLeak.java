package com.danebrown.jvm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by danebrown on 2021/10/13
 * mail: tain198127@163.com
 * 模拟g1内存泄漏的示例
 * @author danebrown
 */
public class G1FinalizerMemoryLeak {

    static {

    }

    public static void main(String[] args) throws InterruptedException {
        G1FinalizerMemoryLeak leak = new G1FinalizerMemoryLeak();
        System.out.println("输入任何值将开始");
        Scanner start = new Scanner(System.in);
        String string =  start.nextLine();
        System.out.println("开始：");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FinMl finMl1 = new FinMl();
                finMl1.test(() -> "String");
                finMl1.test_sample();
                System.out.println("test start");
                for (int i = 0; i < 65535; i++) {
                    FinMl finMl = new FinMl();
                    finMl.test(() -> "String");
                    try {
                        Thread.sleep(500);
                        finMl.weakUp();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        List<Thread> list = new ArrayList<>();
        int length = 20;
        for (int i = 0; i < length; i++) {
            Thread thread1 = new Thread(runnable);
            list.add(thread1);
        }

        for (int i = 0; i < length; i++) {
            list.get(i).start();
//            list.get(i).join();
        }
        Thread.sleep(1000);
        System.out.println("输入任何值将结束.....");
        Scanner scanner = new Scanner(System.in);
        System.out.println("bye"+scanner.nextLine());
        for (int i = 0; i < length; i++) {
            list.get(i).interrupt();
        }
        System.exit(0);

    }
}
