package com.danebrown.jvm;

import org.apache.commons.lang3.CharSet;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashSet;
import java.util.Set;

public class CharsetCompare {


    public static void main(String[] args) {
        // 初始化UTF-8和GB18030-2022的字符集
        Charset utf8Charset = Charset.forName("UTF-8");
        Charset gb18030Charset = Charset.forName("GB18030");

        CharsetEncoder utf8Encoder = utf8Charset.newEncoder();
        CharsetEncoder gb18030Encoder = gb18030Charset.newEncoder();

        Set<Character> utf8Only = new HashSet<>();
        Set<Character> gb18030Only = new HashSet<>();
        Set<String> all = new HashSet<>();
        int start = Character.MIN_CODE_POINT; // 基本汉字的起始码点
        int end = Character.MAX_CODE_POINT; // 扩展区汉字的结束码点

        // 枚举Unicode基本多文种平面（Basic Multilingual Plane, BMP）字符
//        for (int ch = 0x4e00; ch < 0x2FA1D; ch++) {
        for (int ch = Character.MIN_CODE_POINT; ch < Character.MAX_CODE_POINT; ch++) {

            String str = String.valueOf(ch);

            all.add(str);
            boolean inUtf8 = utf8Encoder.canEncode(str);
            boolean inGb18030 = gb18030Encoder.canEncode(str);

            if (inUtf8 && !inGb18030) {
                utf8Only.add((char)ch);
            } else if (!inUtf8 && inGb18030) {
                gb18030Only.add((char)ch);
            }
        }

        // 输出结果
        System.out.println("UTF-8 中有而 GB18030 没有的字符数量: " + utf8Only.size());
        System.out.println("GB18030 中有而 UTF-8 没有的字符数量: " + gb18030Only.size());
        System.out.println("所有字符数量:"+all.size());
//        System.out.println("\nUTF-8 中有而 GB18030 没有的字符示例 (仅显示汉字):");
//        utf8Only.stream().filter(CharsetCompare::isChineseCharacter).limit(10).forEach(ch -> System.out.print(ch + " "));
//
//        System.out.println("\n\nGB18030 中有而 UTF-8 没有的字符示例 (仅显示汉字):");
//        gb18030Only.stream().filter(CharsetCompare::isChineseCharacter).limit(10).forEach(ch -> System.out.print(ch + " "));
    }

    // 判断一个字符是否是汉字
//    public static boolean isChineseCharacter(char ch) {
//        return (ch >= '\u4E00' && ch <= '\u9FFF') ||       // 基本汉字
//                (ch >= '\u3400' && ch <= '\u4DBF') ||       // 扩展A区
//                (ch >= '\u20000' && ch <= '\u2A6DF') ||     // 扩展B区
//                (ch >= '\u2A700' && ch <= '\u2B73F') ||     // 扩展C区
//                (ch >= '\u2B740' && ch <= '\u2B81F') ||     // 扩展D区
//                (ch >= '\u2B820' && ch <= '\u2CEAF') ||     // 扩展E区
//                (ch >= '\u2CEB0' && ch <= '\u2EBEF');       // 扩展F区
//    }
}

