package com.danebrown.otherlib;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.digest.SM3;
import cn.hutool.crypto.symmetric.SM4;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Log4j2
public class Sm3 {

    public static void main(String[] args) {

        Sm3 sm3 = new Sm3();
        String random = UUID.randomUUID().toString();
        Map<String,String> secEvp = sm3.send(random);
        System.out.println(secEvp.get("dig"));
        System.out.println(secEvp.get("random"));
        sm3.verify(secEvp);
    }
    public Map<String,String> send(String random){

        MD5 md5 = new MD5();
        md5.setSalt("3e1cac3abd27659ef802afc77400c217".getBytes());

        String dig = md5.digestHex(random);
        Map<String,String> map = new HashMap<>();
        map.put("random",random);
        map.put("dig",dig);

        return map;
    }

    public void verify(Map<String,String> msg){
        String random = msg.get("random");
        String dig = msg.get("dig");
        MD5 md5 = new MD5();
        md5.setSalt("3e1cac3abd27659ef802afc77400c217".getBytes());

        String digVerify = md5.digestHex(random);
        if(dig.equals(digVerify)){
            System.out.println("验证成功");
        }else{
            throw new SecurityException("验签失败");
        }

    }
}
