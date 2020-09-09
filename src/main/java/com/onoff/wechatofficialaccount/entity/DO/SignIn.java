package com.onoff.wechatofficialaccount.entity.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/9/1 14:33
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

    //文章发表时间||推广二维码的生成时间
    private Long qr_time;

    //期号
    private int period;

    //二维码类型0、文章，1、推广
    private int qr_type;

    public SignIn(String unionid, Long time, Long qr_time, int period,int qr_type) {
        this.unionid = unionid;
        this.time = time;
        this.qr_time = qr_time;
        this.period = period;
        this.qr_type=qr_type;
    }
}
