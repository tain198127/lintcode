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
    private static int STACK_SIZE = 10000;
    /**
     * 原始文件名称
     */
    private static String SOURCE_DATA_NAME = "SOURCE.txt";
    private static String TEMP_FILE_NAME = "tmp-[%s].txt";

    /**
     * 拆分为小文件时，每个文件的行数，所以每个文件的内容为STACK_SIZE*SPLIT_FILE_LINE_SIZE那么多整数
     */
    private static int SPLIT_FILE_LINE_SIZE = 3;

    private List<String> filesName= new ArrayList<>();

    SplittableRandom sr = new SplittableRandom();
    private static void  print_menu(){
        System.out.print(
                "1:generate trillion data\n " +
                "2:split trillion data\n" +
                "3:sort trillion and combin file\n" +
                "0:quite\n" +
                "please input:");
    }
    public static void main(String args[]) throws Exception {
        TrillionMergeSort sort = new TrillionMergeSort();

        HashMap<String, Callable> menu = new HashMap<>();
        menu.put("1", new Callable() {
            @Override
            public Object call() throws Exception {
                sort.generateTrillionData(10000000);
                return null;
            }
        });
        menu.put("2", new Callable() {
            @Override
            public Object call() throws Exception {
                sort.splitTrillionData();
                return null;
            }
        });
        menu.put("3", new Callable() {
            @Override
            public Object call() throws Exception {
                sort.merge();
                return null;
            }
        });
        String read = "";
        Scanner scanner = new Scanner(System.in);
        while(!read.equals("0")){
            print_menu();
             read= scanner.nextLine();
             if(read.equals("exit")||read.equals("0"))
                 break;
             menu.get(read).call();

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
     * @return 文件元信息；
     */
    private List<File> collectionTempFileList(){
        List<File> files = new ArrayList<>();
        int times = 0;
        while (true){

            File tmpFile = new File(String.format(TEMP_FILE_NAME, times++));
            if(!tmpFile.exists())
                break;
            files.add(tmpFile);
        }
        return files;
    }
    private void  mergeSort(InputStream a, InputStream b, OutputStream out){

    }
    /**
     * 合并，并且写入到文件中
     */
    private void merge() {
        List<File> files = collectionTempFileList();

        System.out.println("读取完毕");
        System.out.println(g.toJson(files));
    }
}
