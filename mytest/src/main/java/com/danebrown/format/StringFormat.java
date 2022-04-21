package com.danebrown.format;

import java.text.MessageFormat;

/**
 * Created by danebrown on 2022/3/28
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
public class StringFormat {
    public static void main(String[] args) {
        String msg = MessageFormat.format("123-{0,number,##.##}-222",123.33452);
        System.out.println(msg);
    }
}
