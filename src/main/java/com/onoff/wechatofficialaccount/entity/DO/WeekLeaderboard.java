package com.onoff.wechatofficialaccount.entity.DO;

import com.onoff.wechatofficialaccount.entity.VO.Leaderboard;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/8/14 14:44
 * @VERSION 1.0
 **/
@Data
public class WeekLeaderboard implements Serializable {

    private String id;
    /**
     * 期数
     */
    private int period;

    /**
     * 保存列表数据
     */
    private List<Leaderboard> leaderboards;

    /**
     * 保存时间
     */
    private String Date;

    /**
     * 总参与人数
     */
    private int count;

}
