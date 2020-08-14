package com.danebrown.freemarker;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by danebrown on 2020/8/1
 * mail: tain198127@163.com
 */
@Data
public class TestTarget implements Serializable {
    private int size;
    private int age;
    private String userName;
    private String mac;
    private List<String> books;
    private List<Dto2Json.InnerTestFtl> complexObj;
    private Map<String,Dto2Json.InnerTestFtl> complexMap;
    private String OUTSYS_TX_CODE;
    private String RET_EXPLAIN;
    private String SEND_BANK;
    private String ACCOUNT;
    private String CHNL_CODE;
    private String TRADEFLAG;
    private String SEQNO;
    private String CLR_DATE;
    private String RCV_BKNO;
    private String BUSI_CODE;
    private String TRADEFLAG1;
    private String TRADEFLAG2;
    private String TRADEFLAG3;
    private String TRADEFLAG4;
    private String TRADEFLAG5;
    private String TRADEFLAG6;
    private String TRADEFLAG7;
    private String TRADEFLAG8;
    private String TRADEFLAG9;
    private String TRADEFLAG10;
    private String TRADEFLAG11;
    private String TRADEFLAG12;
    private String TRADEFLAG13;
    private String TRADEFLAG14;
    private String TRADEFLAG15;
    private String TRADEFLAG16;
    private String TRADEFLAG17;
    private String TRADEFLAG18;
    private String TRADEFLAG19;
    private String TRADEFLAG20;


}
