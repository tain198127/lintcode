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
    public void getSystemLock(FunctionLambda functionLambda){
        String str = functionLambda.sayMyName();
        String a = System.getProperty(str);
        System.out.println(a);
    }

    public static void main(String[] args) {
        SystemLock systemLock = new SystemLock();
        systemLock.getSystemLock(()->"JAVA_HOME");
    }
}
