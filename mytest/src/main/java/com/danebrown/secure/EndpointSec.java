package com.danebrown.secure;

import org.apache.commons.codec.digest.Md5Crypt;

import java.util.concurrent.ThreadLocalRandom;

public class EndpointSec {
    public static void main(String[] args) {
        int random = ThreadLocalRandom.current().nextInt();
        String md5 = send(String.valueOf(random));
        boolean isVerified = receive( String.valueOf(random),md5);
        System.out.println(isVerified);
    }

    public static String send(String random){
        String m5 = Md5Crypt.apr1Crypt(random,"1233");
        return m5;
    }
    public static boolean receive(String random, String verify){
        String testMd5 = Md5Crypt.apr1Crypt(random,"1233");
        if(testMd5.equals(verify)){
            return true;
        }else throw new RuntimeException("verify failed");
    }
}
