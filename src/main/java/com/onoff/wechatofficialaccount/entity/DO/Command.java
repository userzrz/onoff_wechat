package com.onoff.wechatofficialaccount.entity.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/9/16 17:44
 * @VERSION 1.0
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Command {

    //口令
    private String id;

    //上限人数
    private int maxUser;

    //设置的积分
    private int integral;

    //有效天数
    private Long days;

    //生成时间
    private Long time;

    //备注信息
    private String remark;
}
