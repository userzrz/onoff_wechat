package com.onoff.wechatofficialaccount.entity.BAM;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/30 15:43
 * @VERSION 1.0
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Relation implements Serializable {

    private String id;

    //邀请人openid
    private String openId;

    //受邀请人unionid
    private String unionId;

    //关系类型  0、建立关系   1、确立关系  2、失效用户
    private int rType;
}
