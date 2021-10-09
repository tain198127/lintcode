package com.danebrown.mesi;

/**
 * Created by danebrown on 2021/10/8
 * mail: tain198127@163.com
 * CPU中在ALU和L1之间，还有一层缓存，叫做Write Combining Buffer，里面只有4个字节。这四个字节满了以后会直接写入
 * L2。因此一次写操作如果执行4次，速度会比较快.
 * @author danebrown
 */
public class WriteCombiningTest {
    private static final int ITERATIONS = Integer.MAX_VALUE;
    private static final int ITEMS = 1 << 24;
    private static final int MASK = ITEMS - 1;

    private static final byte[] arrayA = new byte[ITEMS];
    private static final byte[] arrayB = new byte[ITEMS];
    private static final byte[] arrayC = new byte[ITEMS];
    private static final byte[] arrayD = new byte[ITEMS];
    private static final byte[] arrayE = new byte[ITEMS];
    private static final byte[] arrayF = new byte[ITEMS];


    private static final int times = Integer.MAX_VALUE;
    private static final int arrayLen = 1<<24;
    private static final int mk = arrayLen-1;
    private static final byte[] a1 = new byte[arrayLen];
    private static final byte[] a2 = new byte[arrayLen];
    private static final byte[] a3 = new byte[arrayLen];
    private static final byte[] a4 = new byte[arrayLen];
    private static final byte[] a5 = new byte[arrayLen];
    private static final byte[] a6 = new byte[arrayLen];


    public static void main(String[] args) {
        for (int i = 1; i <= 3; i++) {
            System.out.println(i + " 普通写入 耗时 (ns) = " + runCaseOne());
            System.out.println(i + " WC写入  耗时 (ns) = " + runCaseTwo());

            System.out.println(i + " 手写普通写入 耗时 (ns) = " + normalWrite());
            System.out.println(i + " 手写WC写入  耗时 (ns) = " + wcTest());
        }
    }
    public static long normalWrite(){
        long start = System.nanoTime();
        int i = times;
        while(--i >= 0) {
            int idx = i & mk;
            byte b = (byte)i;
            a1[idx] = b;
            a2[idx] = b;
            a3[idx] = b;
            a4[idx] = b;
            a5[idx] = b;
            a6[idx] = b;
        }
        return System.nanoTime() - start;
    }
    public static long wcTest(){
        long start = System.nanoTime();
        int i = times;
        while(--i >= 0){
            int idx = i & mk;
            byte b = (byte)i;
            a1[idx] = b;
            a2[idx] = b;
            a3[idx] = b;
        }
        i = times;
        while (--i >= 0){
            int idx = i & mk;
            byte b = (byte)i;
            a4[idx] = b;
            a5[idx] = b;
            a6[idx] = b;
        }
        return System.nanoTime() - start;
    }
    public static long runCaseOne() {
        long start = System.nanoTime();
        int i = ITERATIONS;

        while (--i != 0) {
            int slot = i & MASK;
            byte b = (byte) i;
            arrayA[slot] = b;
            arrayB[slot] = b;
            arrayC[slot] = b;
            arrayD[slot] = b;
            arrayE[slot] = b;
            arrayF[slot] = b;
        }
        return System.nanoTime() - start;
    }

    public static long runCaseTwo() {
        long start = System.nanoTime();
        int i = ITERATIONS;
        while (--i != 0) {
            int slot = i & MASK;
            byte b = (byte) i;
            arrayA[slot] = b;
            arrayB[slot] = b;
            arrayC[slot] = b;
        }
        i = ITERATIONS;
        while (--i != 0) {
            int slot = i & MASK;
            byte b = (byte) i;
            arrayD[slot] = b;
            arrayE[slot] = b;
            arrayF[slot] = b;
        }
        return System.nanoTime() - start;
    }
}
