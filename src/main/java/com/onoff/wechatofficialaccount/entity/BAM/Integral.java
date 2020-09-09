package com.onoff.wechatofficialaccount.entity.BAM;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/8/4 14:19
 * @VERSION 1.0
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Integral implements Serializable {

    //积分表id
    private String id;

    //用户openid
    private String openId;

    //积分记录值
    private int record;

    //积分来源方式  0、初始化  1、关注    2、取关    3、打卡    4、QL
    private int source;

    //添加时间
    private String time;

    //期数
    private int period;

    //关联用户openid
    private String relationId;

    public Integral(String openId, int record, int source, String time,int period) {
        this.openId = openId;
        this.record = record;
        this.source = source;
        this.time = time;
        this.period=period;
    }

    public Integral(String openId, int record, int source, String time,int period,String relationId) {
        this.openId = openId;
        this.record = record;
        this.source = source;
        this.time = time;
        this.period=period;
        this.relationId=relationId;
    }
}
