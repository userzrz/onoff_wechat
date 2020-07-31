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
    //所在城市
    private String city;
    //所在省份
    private String province;
    //所在国家
    private String country;
    //用户的语言
    private String language;
    //关注时间
    private String subScribeTime;
    //来源方式
    private String subscribeScene;
    //unionid
    private String unionId;
    //所在的分组ID
    private String groupId;
    //标签ID列表
    private String tagid_list;
    //运营者对粉丝的备注
    private String remark;

}
