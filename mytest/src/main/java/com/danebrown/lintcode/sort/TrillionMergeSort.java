/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.danebrown.lintcode.sort;


import com.google.gson.Gson;

import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;


/**
 * 十亿级排序，采用归并算法
 * 设计思路——>
 * 1. 先将 Trillion 数据放到外部文件A中
 * 2. 对文件 A 进行分块，分成10000份
 * 3. 对每块进行快速排序
 * 4. 对每块进行 MERGE并写入到文件中
 */
public class TrillionMergeSort {
    Gson g = new Gson();
    /**
     * 创建文件时，flush 的大小，表示每行有多少个数字
     */
    private static int STACK_SIZE = 100;
    /**
     * 原始文件名称
     */
    private static String SOURCE_DATA_NAME = "SOURCE.txt";
    private static String TEMP_FILE_NAME = "tmp-[%s].txt";

    /**
     * 拆分为小文件时，每个文件的行数，所以每个文件的内容为STACK_SIZE*SPLIT_FILE_LINE_SIZE那么多整数
     */
    private static int SPLIT_FILE_LINE_SIZE = 3;
    private static int ALL_DATA_SIZE = 10000;
    private List<String> filesName = new ArrayList<>();

    SplittableRandom sr = new SplittableRandom();

    private static void print_menu() {
        System.out.print(
                "1:generate trillion data\n" +
                        "2:generate trillion bindata\n" +
                        "3:split trillion data\n" +
                        "4:split trillion bindata\n" +
                        "5:sort trillion and combin file\n" +
                        "6:sort trillion and combin binfile\n" +
                        "0:quite\n" +
                        "please input:");
    }

    public static void main(String args[]) throws Exception {
        TrillionMergeSort sort = new TrillionMergeSort();

        HashMap<String, Callable> menu = new HashMap<>();
        menu.put("1", new Callable() {
            @Override
            public Object call() throws Exception {
                sort.generateTrillionData(ALL_DATA_SIZE);
                return null;
            }
        });
        menu.put("2", new Callable() {
            @Override
            public Object call() throws Exception {
                sort.generateTrillionDataBin(ALL_DATA_SIZE);
                return null;
            }
        });
        menu.put("3", new Callable() {
            @Override
            public Object call() throws Exception {
                sort.splitTrillionData();
                return null;
            }
        });
        menu.put("4", new Callable() {
            @Override
            public Object call() throws Exception {
                sort.splitTrillionBinData();
                return null;
            }
        });
        menu.put("5", new Callable() {
            @Override
            public Object call() throws Exception {
                sort.merge();
                return null;
            }
        });
        menu.put("6", new Callable() {
            @Override
            public Object call() throws Exception {
                sort.display();
                return null;
            }
        });
        String read = "";
        Scanner scanner = new Scanner(System.in);
        while (!read.equals("0")) {
            print_menu();
            read = scanner.nextLine();
            if (read.equals("exit") || read.equals("0"))
                break;
            menu.get(read).call();

        }

    }

    /**
     * 生成十亿数据，写入到文件中[完成]
     *
     * @param size 要生成的数据大小
     */
    private void generateTrillionDataBin(int size) throws IOException {
        /**
         * 分段生成随机数，并写入文件
         */
        File f = new File("SOURCE_DATA_NAME");

        try (BufferedOutputStream pw = new BufferedOutputStream(new FileOutputStream(new File(SOURCE_DATA_NAME), true))) {
            int j = 0;
            int k = 0;
            if (size < STACK_SIZE)
                return;
            while (j < size) {

                for (int i = 0; i < STACK_SIZE; i++) {
                    int rdm = sr.nextInt(Integer.MAX_VALUE);
                    j++;
                    pw.write(rdm);
                }//end for
                k++;
                pw.flush();
                //System.out.printf("写入了%s次\n", k);
            }//end whild
        }


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
    private void splitTrillionBinData() throws IOException {

        try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream(new File(SOURCE_DATA_NAME)))) {
            Integer num = -1;
            //标记
            int count = 0;
            //分割文件的标记
            int times = 0;
            int size=0;
            BufferedOutputStream br = null;
            LinkedList<Integer> tempSort = null;
            String fileName = null;

            while ((num = reader.read()) != -1) {
                if (count == 0) {
                    fileName = String.format(TEMP_FILE_NAME, times);
                    br = new BufferedOutputStream(new FileOutputStream(new File(fileName), false));
                    tempSort = new LinkedList<>();
                }
                count++;
                size++;

                tempSort.add(num);

                if (count >= STACK_SIZE*SPLIT_FILE_LINE_SIZE||size>=ALL_DATA_SIZE) {
                    times++;
                    count = 0;//开关；表明切分文件了
                    quickSort(tempSort);
                    //tempSort.sort(Comparator.comparingInt(o -> o));
                    for (int i = 0; i < tempSort.size(); i++) {
                        br.write(tempSort.get(i));
                        if ((i + 1) % STACK_SIZE == 0) {
                            br.flush();
                        }
                    }
                    if (br != null) {
                        br.flush();
                        br.close();
                        br = null;
                        filesName.add(fileName);
                        System.out.println(fileName + "写入完毕");
                    }
                    if(size>=ALL_DATA_SIZE){
                        break;
                    }
                }
            }//end while
            if(br != null){
                br.flush();
                br.close();
                br = null;
                filesName.add(fileName);
                System.out.println(fileName + "写入完毕");
            }

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
                    quickSort(tempSort);
                    //tempSort.sort(Comparator.comparingInt(o -> o));
                    for (int i = 0; i < tempSort.size(); i++) {
                        br.printf("%s,", tempSort.get(i));
                        if ((i + 1) % STACK_SIZE == 0) {
                            br.println();
                        }
                    }
                    if (br != null) {

                        br.flush();
                        br.close();
                        filesName.add(fileName);
                        System.out.println(fileName + "写入完毕");
                    }
                }
            }

        }
    }

    /**
     * 对每一块进行快排[需要完善和优化，目前使用的是系统自带的排序方法]
     */
    private void quickSort(LinkedList<Integer> array) {
        array.sort(Comparator.comparingInt(o -> o));
    }

    /**
     * 进行合并的第一步，搜集所有临时文件[完成]
     *
     * @return 文件元信息；
     */
    private List<File> collectionTempFileList() {
        List<File> files = new ArrayList<>();
        int times = 0;
        while (true) {

            File tmpFile = new File(String.format(TEMP_FILE_NAME, times++));
            if (!tmpFile.exists())
                break;
            files.add(tmpFile);
        }
        return files;
    }

    /**
     * 具体的merge
     *
     * @param a
     * @param b
     * @param out
     */
    private void mergeSort(BufferedInputStream a, BufferedInputStream b, BufferedOutputStream out) throws IOException {
        //标志位，是否要读左边A的
        boolean readLeft = true;
        //标志位，是否要读右边的
        boolean readRight = true;
        int left=-1;
        int right = -1;
        while (true){
            if(readLeft)
                left = a.read();
            if(readRight)
                right = b.read();
            if(left != -1 && right!=-1){//都没结束的情况下
                if(left>right){//右小，读右不读左
                    out.write(right);
                    readLeft=false;
                    readRight=true;
                }
                else if(left<right){//左小，读左不读右
                    out.write(left);
                    readLeft=true;
                    readRight=false;
                }
                else if(left ==right){//都读
                    out.write(left);
                    out.write(right);
                    readLeft=true;
                    readRight=true;
                }
            }
            //如果有剩余的，证明剩余的都可以存进去
            if(left == -1){
                int allright = -1;
                while ((allright=a.read())!=-1){
                    out.write(allright);
                }
                break;
            }
            if(right==-1){
                int allleft = -1;
                while ((allleft=a.read())!=-1){
                    out.write(allleft);
                }
                break;
            }
        }//end while
        out.flush();
    }

    /**
     * 调用mergeSort，对file进行转换
     *
     * @param a
     * @param b
     * @return
     */
    private File mergeSortConvert(File a, File b, String FileName) throws IOException {
        try (BufferedInputStream brA = new BufferedInputStream(new FileInputStream(a))) {
            try (BufferedInputStream brB = new BufferedInputStream(new FileInputStream(b))) {
                File out = new File(FileName);
                try (BufferedOutputStream bwOut = new BufferedOutputStream(new FileOutputStream(out))) {
                    mergeSort(brA, brB, bwOut);
                    return out;
                }
            }
        }

    }
    private void display() throws IOException {
        List<Integer> idx = new ArrayList<>();
        System.out.println("要展示的文件是:");
        Scanner scanner= new Scanner(System.in);

        String  fileName = scanner.next();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))){
            Integer num = -1;
            while ((num = reader.read())!=-1){
                System.out.println(num);
                idx.add(num);
            }
        }
        System.out.println(g.toJson(idx));
    }
    /**
     * 合并，并且写入到文件中
     */
    private void merge() throws IOException {
        List<File> files = collectionTempFileList();

        System.out.println("读取完毕");
        System.out.println(g.toJson(files));
        List<File> previousList = new ArrayList<>();
        List<File> currentList = new LinkedList<>();
        previousList = files;
        int time = 0;
        while (true) {
            time++;
            //先把奇数长度的list，最后一个放到下一个循环需要用到的list去，保证当前处理的一定是偶数
            if (previousList.size() % 2 == 1) {
                currentList.add(previousList.get(previousList.size() - 1));
                previousList.remove(previousList.size() - 1);
            }
            //两个两个处理
            for (int i = 0; i < previousList.size(); i = i + 2) {
                File out = mergeSortConvert(previousList.get(i), previousList.get(i + 1), String.format("merge[%d]轮{%d}个-[%d]个.txt", time, i, i + 1));
                currentList.add(out);
            }
            //处理完后，把pre指向到cur，这里可能有问题，需要deepcopy
            previousList = currentList;
            currentList = new LinkedList<>();
            if (previousList.size() == 1) {//表示已经归并到最后一个文件了
                break;
            }
        }

    }
}
