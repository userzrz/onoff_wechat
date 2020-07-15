package com.onoff.wechatofficialaccount.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/14 19:15
 * @VERSION 1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements Serializable {
    //唯一标识
    private String openId;
    //昵称
    private String nickName;
    //0:男性  1:女性   0:未知
    private String sex;
    //头像url
    private String headImgUrl;
    //关注时间
    private String subScribeTime;
    //来源方式
    private String subscribeScene;
    //二维码场景值
    private String qrScene;
    //二维码描述
    private String qrSceneStr;
}
