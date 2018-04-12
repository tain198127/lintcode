package com.danebrown.lintcode.sort;

import java.io.*;
import java.util.Collections;
import java.util.SplittableRandom;

/**
 * 十亿级排序，采用归并算法
 * 设计思路——>
 * 1. 先将 Trillion 数据放到外部文件A中
 * 2. 对文件 A 进行分块，分成10000份
 * 3. 对每块进行快速排序
 * 4. 对每块进行 MERGE并写入到文件中
 */
public class TrillionMergeSort {
    private static int STACK_SIZE = 10000;
    private static String SOURCE_DATA_NAME="SOURCE.txt";
    SplittableRandom sr = new SplittableRandom();

    public static void main(String args[]) throws IOException {
        TrillionMergeSort sort = new TrillionMergeSort();
        sort.generateTrillionData(1000000000);
    }

    /**
     * 生成十亿数据，写入到文件中
     *
     * @param size 要生成的数据大小
     */
    private void generateTrillionData(int size) throws IOException {
        /**
         * 分段生成随机数，并写入文件
         */
        File f = new File("SOURCE_DATA_NAME");

        try(PrintWriter pw=new PrintWriter(new FileWriter(new File(SOURCE_DATA_NAME),true))){
            int j = 0;
            if (size < STACK_SIZE)
                return;
            while (j < size) {

                for (int i = 0; i < STACK_SIZE; i++) {
                    int rdm = sr.nextInt(Integer.MAX_VALUE);
                    pw.printf("%d,",rdm);
                    if(i == STACK_SIZE-1){
                        pw.println();
                    }
                    j++;

                }//end for
                pw.flush();

            }//end whild
        }


    }

    /**
     * 将十亿数据分布到10000份中
     */
    private void splitTrillionData() {

    }

    /**
     * 对每一块进行快排
     */
    private void quickSort() {

    }

    /**
     * 合并，并且写入到文件中
     */
    private void merge() {

    }
}
