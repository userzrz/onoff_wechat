package com.onoff.wechatofficialaccount.entity.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/9/1 16:37
 * @VERSION 1.0
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PromotionQR implements Serializable {

    private String id;

    //上限人数
    private int maxUser;

    //设置的积分
    private int integral;

    //有效天数
    private Long days2;

    //生成时间
    private Long time;

}