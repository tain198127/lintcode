package com.danebrown.lintcode.sort;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.SplittableRandom;

import static org.apache.commons.lang3.StringUtils.split;

/**
 * 十亿级排序，采用归并算法
 * 设计思路——>
 * 1. 先将 Trillion 数据放到外部文件A中
 * 2. 对文件 A 进行分块，分成10000份
 * 3. 对每块进行快速排序
 * 4. 对每块进行 MERGE并写入到文件中
 */
public class TrillionMergeSort {
    /**
     * 创建文件时，flush 的大小，表示每行有多少个数字
     */
    private static int STACK_SIZE = 10000;
    /**
     * 原始文件名称
     */
    private static String SOURCE_DATA_NAME = "SOURCE.txt";
    private static String TEMP_FILE_NAME = "tmp-[%s].txt";
    /**
     * 拆分为小文件时，每个文件的行数
     */
    private static int SPLIT_FILE_LINE_SIZE = 3;
    SplittableRandom sr = new SplittableRandom();

    public static void main(String args[]) throws IOException {
        TrillionMergeSort sort = new TrillionMergeSort();
//        sort.generateTrillionData(10000000);
        sort.splitTrillionData();
    }

    /**
     * 生成十亿数据，写入到文件中[完成]
     *
     * @param size 要生成的数据大小
     */
    private void generateTrillionData(int size) throws IOException {
        /**
         * 分段生成随机数，并写入文件
         */
        File f = new File("SOURCE_DATA_NAME");

        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(SOURCE_DATA_NAME), true))) {
            int j = 0;
            int k = 0;
            if (size < STACK_SIZE)
                return;
            while (j < size) {

                for (int i = 0; i < STACK_SIZE; i++) {
                    int rdm = sr.nextInt(Integer.MAX_VALUE);
                    pw.printf("%d,", rdm);
                    if (i == STACK_SIZE - 1) {
                        pw.println();
                    }
                    j++;

                }//end for
                k++;
                pw.flush();
                System.out.printf("写入了%s次\n", k);
            }//end whild
        }


    }

    /**
     * 将十亿数据分布到10000份中[完成]
     */
    private void splitTrillionData() throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(SOURCE_DATA_NAME)))) {
            String line = null;
            int count = 0;
            int times = 0;
            PrintWriter br = null;
            LinkedList<Integer> tempSort = null;
            String fileName = null;
            while ((line = reader.readLine()) != null) {
                if (count == 0) {
                    fileName = String.format(TEMP_FILE_NAME, times);
                    br = new PrintWriter(new FileWriter(new File(fileName), false));
                    tempSort = new LinkedList<>();
                }
                count++;
                String[] tmpStr = line.split(",");
                for (int i = 0; i < tmpStr.length; i++) {
                    tempSort.add(Integer.parseInt(tmpStr[i]));
                }
                //br.write(line);

                if (count > SPLIT_FILE_LINE_SIZE) {
                    times++;
                    count = 0;
                    tempSort.sort(Comparator.comparingInt(o -> o));
                    for (int i = 0; i < tempSort.size(); i++) {
                        br.printf("%s,", tempSort.get(i));
                        if ((i + 1) % STACK_SIZE == 0) {
                            br.println();
                        }
                    }
                    if (br != null) {

                        br.flush();
                        br.close();
                        System.out.println(fileName + "写入完毕");
                    }
                }
            }

        }
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
