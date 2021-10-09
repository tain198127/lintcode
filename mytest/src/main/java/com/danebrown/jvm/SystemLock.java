package com.danebrown.jvm;

/**
 * Created by danebrown on 2021/9/26
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
public class SystemLock {
    static {
        System.setProperty("1","1");
    }
    public void getSystemLock(){
        String a = System.getProperty("");
    }
}
