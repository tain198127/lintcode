package com.danebrown.jvm;

import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class UTF8VSGB18303 {

    public static void printAllCharactersets(){
                Charset.availableCharsets().forEach(new BiConsumer<String, Charset>() {
            @Override
            public void accept(String s, Charset charset) {
                String name = charset.name();
                if(charset.aliases()!=null && !charset.aliases().isEmpty()){
                    name = charset.aliases().stream().findFirst().get();
                }
                System.out.println(String.format("name:%s,charset:%s",s,name));
            }
        });
    }

    public static final void printEnv(){
        System.out.println("<====环境信息====>");
        Charset gb18030Charset = Charset.forName("GB18030");
        System.out.println(String.format("GB10380版本:%s",gb18030Charset.aliases().stream().findFirst().get()));
        Charset utf8Charset = Charset.forName("UTF-8");
        System.out.println(String.format("UTF-8版本:%s",utf8Charset.aliases().stream().findFirst().get()));
        System.out.println(String.format("jdk版本:%s",System.getProperty("java.version")));
        System.out.println(String.format("操作系统版本:%s",System.getProperty("os.name")));
        System.out.println("<====环境信息====>");

    }

    public static Set<String> readFromResource(String filename) {
        Set<String> result =new HashSet<>();
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:"+filename);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String line = null;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                result.add(line.trim());
            }
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }
        return result;
    }
    public static void main(String[] args) throws UnsupportedEncodingException {
//        printAllCharactersets();
        printEnv();


        System.out.println("<===全字符对比===>");
        Set<String> allutf8 = genAllUtf8();
        Set<String> allGB18030 =genAllGB18030();

        System.out.println("\n=============================\n");
//        Set<String> onlyAllUtf8 = new HashSet<>();
//        Set<String> onlyAllGB18030 = new HashSet<>();
//        onlyAllUtf8.addAll(allutf8);
//        onlyAllUtf8.removeAll(allGB18030);
//
//        onlyAllGB18030.addAll(allGB18030);
//        onlyAllGB18030.removeAll(allutf8);
//
//        System.out.println("全字符对比");
//        System.out.println("只在gb18030中出现的字符有:" + onlyAllGB18030.size() + "个,分别是:");
//        onlyAllGB18030.stream().forEach(item -> System.out.print(item));
//        System.out.println();
//        System.out.println("只在UTF8中出现的字符有:" + onlyAllUtf8.size() + "个,分别是:");
//        onlyAllUtf8.stream().forEach(item -> System.out.print(item));
//        System.out.println();


        System.out.println("<===中文对比====>");
        Set<String> gb18030 = genA180ll30ChineseCharacter();
        Set<String> utf8 = genAllUtf8ChineseCharacter();





        Set<String> gbonly = new HashSet<>();
        Set<String> utf8only = new HashSet<>();
        gbonly.addAll(gb18030);
        gbonly.removeAll(utf8);

        utf8only.addAll(utf8);
        utf8only.removeAll(gb18030);

        System.out.println("只在gb18030中出现的字符有:" + gbonly.size() + "个,分别是:");
        gbonly.stream().forEach(item -> System.out.print(item));
        System.out.println();
        System.out.println("只在UTF8中出现的字符有:" + utf8only.size() + "个,分别是:");
        utf8only.stream().forEach(item -> System.out.print(item));
        System.out.println();
    }

    public static void strange(String charset){
        Set<String> strange = readFromResource("生僻字.txt");
//        Charset utf8Charset = Charset.forName("UTF-8");
        Charset charset1 = Charset.forName(charset);
        CharsetEncoder charsetEncoder = charset1.newEncoder();
        AtomicInteger strangeCount = new AtomicInteger();
        strange.forEach(item->{
            if(!charsetEncoder.canEncode(item)){
                strangeCount.getAndIncrement();
            }
        });

            System.out.println(String.format("%s在5000个生僻字中,不兼容个数为:%s",charset,strangeCount));

    }
    public static Set<String> genAllUtf8ChineseCharacter() throws UnsupportedEncodingException {
        System.out.println("UTF-8--------->");

        strange("UTF-8");

        Set<String> utf8HanCharacters = new HashSet<>();

        // 遍历基本汉字（\u4E00 到 \u9FFF）
        for (int codePoint = 0x4E00; codePoint <= 0x9FFF; codePoint++) {
            addUtf8Character(codePoint, utf8HanCharacters);
        }

        // 遍历扩展A区（\u3400 到 \u4DBF）
        for (int codePoint = 0x3400; codePoint <= 0x4DBF; codePoint++) {
            addUtf8Character(codePoint, utf8HanCharacters);
        }

        // 遍历扩展B区（0x20000 到 0x2A6DF）
        for (int codePoint = 0x20000; codePoint <= 0x2A6DF; codePoint++) {
            addUtf8Character(codePoint, utf8HanCharacters);
        }

        // 遍历扩展C区（0x2A700 到 0x2B73F）
        for (int codePoint = 0x2A700; codePoint <= 0x2B73F; codePoint++) {
            addUtf8Character(codePoint, utf8HanCharacters);
        }

        // 遍历扩展D区（0x2B740 到 0x2B81F）
        for (int codePoint = 0x2B740; codePoint <= 0x2B81F; codePoint++) {
            addUtf8Character(codePoint, utf8HanCharacters);
        }

        // 遍历扩展E区（0x2B820 到 0x2CEAF）
        for (int codePoint = 0x2B820; codePoint <= 0x2CEAF; codePoint++) {
            addUtf8Character(codePoint, utf8HanCharacters);
        }

        // 输出 UTF-8 支持的汉字数量
        System.out.println("UTF-8 支持的汉字数量: " + utf8HanCharacters.size());

        // 打印前 10 个汉字作为示例
        System.out.println("UTF-8 支持的汉字示例:");
        utf8HanCharacters.stream().limit(10).forEach(System.out::print);
        System.out.println();

        StringBuilder allStringByteSize = new StringBuilder();
        utf8HanCharacters.forEach(item->allStringByteSize.append(item));
        String allString = allStringByteSize.toString();
        byte[] allbyte = allString.getBytes("UTF-8");
        System.out.println(String.format("存储%s个汉字，占用内存：%s位字节",allString.length(),allbyte.length));
        return utf8HanCharacters;
    }

    private static void addUtf8Character(int codePoint, Set<String> utf8HanCharacters) {
        if (Character.isDefined(codePoint)) {
            String character = new String(Character.toChars(codePoint));
            utf8HanCharacters.add(character);
        }
    }

    public static Set<String> genAllGB18030() throws UnsupportedEncodingException {
        Charset gb18030Charset = Charset.forName("GB18030");
        CharsetDecoder decoder = gb18030Charset.newDecoder();

        Set<String> gb18030Characters = new HashSet<>();

        // 遍历单字节字符
        for (int byte1 = 0x00; byte1 <= 0x7F; byte1++) {
            byte[] gb18030Bytes = {(byte) byte1};
            String character = decodeGB18030(gb18030Bytes, decoder);
            if (character != null) {
                gb18030Characters.add(character);
            }
        }

        // 遍历双字节字符
        for (int byte1 = 0x81; byte1 <= 0xFE; byte1++) {
            for (int byte2 = 0x40; byte2 <= 0xFE; byte2++) {
                if (byte2 == 0x7F) continue; // 跳过无效的 0x7F
                byte[] gb18030Bytes = {(byte) byte1, (byte) byte2};
                String character = decodeGB18030(gb18030Bytes, decoder);
                if (character != null) {
                    gb18030Characters.add(character);
                }
            }
        }

        // 遍历四字节字符
        for (int byte1 = 0x81; byte1 <= 0xFE; byte1++) {
            for (int byte2 = 0x30; byte2 <= 0x39; byte2++) {
                for (int byte3 = 0x81; byte3 <= 0xFE; byte3++) {
                    for (int byte4 = 0x30; byte4 <= 0x39; byte4++) {
                        byte[] gb18030Bytes = {(byte) byte1, (byte) byte2, (byte) byte3, (byte) byte4};
                        String character = decodeGB18030(gb18030Bytes, decoder);
                        if (character != null) {
                            gb18030Characters.add(character);
                        }
                    }
                }
            }
        }

        // 输出字符总数
        System.out.println("GB18030 支持的字符总数: " + gb18030Characters.size());

        StringBuilder allStringByteSize = new StringBuilder();
        gb18030Characters.forEach(item->allStringByteSize.append(item));
        String allString = allStringByteSize.toString();
        byte[] allbyte = allString.getBytes("UTF8");
        System.out.println(String.format("GB18030存储%s个全字符，占用内存：%s位字节，平均每个字符占用%s个字节\n",allString.length(),allbyte.length,allbyte.length/allString.length()));
        // 输出前 10 个字符作为示例
//        System.out.println("GB18030 支持的字符示例:");
//        gb18030Characters.stream().limit(10).forEach(System.out::print);
        return gb18030Characters;
    }
    // 解码 GB18030 字节数组为字符
    private static String decodeGB18030(byte[] gb18030Bytes, CharsetDecoder decoder) {
        try {
            return new String(gb18030Bytes, decoder.charset());
        } catch (Exception e) {
            return null;
        }
    }

    public static Set<String> genAllUtf8() throws UnsupportedEncodingException {
        Set<String> utf8 = new HashSet<>();

        for (int codePoint = Character.MIN_CODE_POINT; codePoint <= Character.MAX_CODE_POINT; codePoint++) {
            if (Character.isDefined(codePoint)) {  // 检查码点是否为有效字符
                String character = new String(Character.toChars(codePoint));  // 将码点转换为字符
                utf8.add(character);

            }
        }
        System.out.println("UTF8 支持的字符数量: " + utf8.size());
        StringBuilder allStringByteSize = new StringBuilder();
        utf8.forEach(item->allStringByteSize.append(item));
        String allString = allStringByteSize.toString();
        byte[] allbyte = allString.getBytes("UTF8");
        System.out.println(String.format("UTF-8存储%s个全字符，占用内存：%s位字节，平均每个字符占用%s个字节",allString.length(),allbyte.length,allbyte.length/allString.length()));


        return utf8;
    }
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
    public static Set<String> genA180ll30ChineseCharacter() throws UnsupportedEncodingException {
        System.out.println("GB18030--------->");

        strange("GB18030");
        Set<String> gb18030HanCharacters = new HashSet<>();

        // 遍历双字节区间
        for (int byte1 = 0x81; byte1 <= 0xFE; byte1++) {
            for (int byte2 = 0x40; byte2 <= 0xFE; byte2++) {
                if (byte2 == 0x7F) continue; // 跳过无效字节
                byte[] gb18030Bytes = {(byte) byte1, (byte) byte2};
                String character = decodeGB18030(gb18030Bytes);
                if (character != null && isChineseCharacter(character)) {
                    gb18030HanCharacters.add(character);
                }
            }
        }

        // 遍历四字节区间
        for (int byte1 = 0x81; byte1 <= 0xFE; byte1++) {
            for (int byte2 = 0x30; byte2 <= 0x39; byte2++) {
                for (int byte3 = 0x81; byte3 <= 0xFE; byte3++) {
                    for (int byte4 = 0x30; byte4 <= 0x39; byte4++) {
                        byte[] gb18030Bytes = {(byte) byte1, (byte) byte2, (byte) byte3, (byte) byte4};
                        String character = decodeGB18030(gb18030Bytes);
                        if (character != null && isChineseCharacter(character)) {
                            gb18030HanCharacters.add(character);
                        }
                    }
                }
            }
        }

        // 输出结果
        System.out.println("GB18030 支持的汉字数量: " + gb18030HanCharacters.size());

        // 打印前 10 个汉字作为示例
        System.out.println("GB18030 支持的汉字示例:");
        gb18030HanCharacters.stream().limit(10).forEach(System.out::print);
        System.out.println();
        StringBuilder allStringByteSize = new StringBuilder();
        gb18030HanCharacters.forEach(item->allStringByteSize.append(item));
        String allString = allStringByteSize.toString();
        byte[] allbyte = allString.getBytes("GB18030");
        System.out.println(String.format("存储%s个汉字，占用内存：%s位字节",allString.length(),allbyte.length));
        return gb18030HanCharacters;
    }

    // 解码 GB18030 字节数组为字符
    private static String decodeGB18030(byte[] gb18030Bytes) {
        Charset gb18030 = Charset.forName("GB18030");
        CharsetDecoder decoder = gb18030.newDecoder();
        try {
            return new String(gb18030Bytes, gb18030);
        } catch (Exception e) {
            return null;
        }
    }

    // 判断字符串是否是汉字
    private static boolean isChineseCharacter(String str) {
        if (str.codePointCount(0, str.length()) != 1) return false;  // 确保只有一个字符
        int codePoint = str.codePointAt(0);  // 获取 Unicode 码位
        return (codePoint >= 0x4E00 && codePoint <= 0x9FFF) ||       // 基本汉字
                (codePoint >= 0x3400 && codePoint <= 0x4DBF) ||       // 扩展A区
                (codePoint >= 0x20000 && codePoint <= 0x2A6DF) ||     // 扩展B区
                (codePoint >= 0x2A700 && codePoint <= 0x2B73F) ||     // 扩展C区
                (codePoint >= 0x2B740 && codePoint <= 0x2B81F) ||     // 扩展D区
                (codePoint >= 0x2B820 && codePoint <= 0x2CEAF);       // 扩展E区
    }
}

