package com.onoff.wechatofficialaccount.entity.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/9/1 13:42
 * @VERSION 1.0
 **/
@AllArgsConstructor
@Data
public class SignInQR implements Serializable {

    private String id;

    //文章发表时间
    private Long releaseTime;

    //二维码过期时间
    private Long overTime;
}
