package com.danebrown.reactor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;

import java.util.Date;

/**
 * Created by danebrown on 2020/8/14
 * mail: tain198127@163.com
 */
@Data
@Builder
public class Employer {
    private String id;
    private String local;
    private Date birthday;

}
