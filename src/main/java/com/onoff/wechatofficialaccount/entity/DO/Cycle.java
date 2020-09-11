package com.onoff.wechatofficialaccount.entity.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/9/10 14:20
 * @VERSION 1.0
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Cycle {

    private String id="1";

    //本周活动起始日期
    private String weekStartDate;

    //本周活动期号
    private String weekPeriod;

    //本月活动起始日期
    private String monthStartDate;

    //本月活动期号
    private String monthPeriod;

}
