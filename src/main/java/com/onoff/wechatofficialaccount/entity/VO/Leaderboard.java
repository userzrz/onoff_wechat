package com.onoff.wechatofficialaccount.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/8/5 13:09
 * @VERSION 1.0
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Leaderboard implements Serializable {


    /**
     * 名次
     */
    private long ranking;

    /**
     * id
     */
    private String openId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String headImgUrl;

    /**
     * 积分
     */
    private int record;

}
