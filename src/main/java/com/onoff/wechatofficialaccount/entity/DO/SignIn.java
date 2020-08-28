package com.onoff.wechatofficialaccount.entity.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/8/24 15:26
 * @VERSION 1.0
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignIn implements Serializable {

    //id
    private String id;

    //用户唯一标识
    private String unionid;

    //打卡时间
    private Long time;

    //二维码生成时间
    private Long qr_time;

    //期号
    private int period;

    public SignIn(String unionid, Long time, Long qr_time, int period) {
        this.unionid = unionid;
        this.time = time;
        this.qr_time = qr_time;
        this.period = period;
    }
}
