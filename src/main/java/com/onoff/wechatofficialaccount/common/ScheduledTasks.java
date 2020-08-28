package com.onoff.wechatofficialaccount.common;

import com.onoff.wechatofficialaccount.entity.DO.WeekLeaderboard;
import com.onoff.wechatofficialaccount.entity.VO.Leaderboard;
import com.onoff.wechatofficialaccount.mapper.BAMMapper;
import com.onoff.wechatofficialaccount.mapper.DAO.BAMDao;
import com.onoff.wechatofficialaccount.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/8/13 17:04
 * @VERSION 1.0
 **/
@Slf4j
@Component
public class ScheduledTasks {

    @Autowired
    BAMMapper bamMapper;

    @Autowired
    BAMDao bamDao;

    //59 59 23 *  * ?
    //0/20 * * *  * ?
    @Scheduled(cron = "59 59 23 *  * ?")
    public void Time() {
        log.info("========================结算周期榜");
        //获取排行榜
        List<Leaderboard> array = bamMapper.getLeaderboard(CommonUtils.period);
        //排名
        CommonUtils.setRanking(array);
        WeekLeaderboard weekLeaderboard = new WeekLeaderboard();
        weekLeaderboard.setLeaderboards(array);
        weekLeaderboard.setPeriod(CommonUtils.period);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(new Date());
        weekLeaderboard.setDate(date);
        //保存周榜信息
        bamDao.saveLeaderboard(weekLeaderboard, CommonUtils.mongodb_week);
        //设置新期
        CommonUtils.setPeriod(CommonUtils.period + 1);
        //结算月榜
        if (CommonUtils.period % 4 == 1 && CommonUtils.period > 1) {
            //清空集合
            bamDao.delCollection(CommonUtils.mongodb_month);
            //获取月榜排行榜
            array = bamMapper.getLeaderboard(0);
            //排名
            CommonUtils.setRanking(array);
            weekLeaderboard.setLeaderboards(array);
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = formatter.format(new Date());
            weekLeaderboard.setDate(date);
            //保存月榜信息
            bamDao.saveLeaderboard(weekLeaderboard, CommonUtils.mongodb_month);
            //清空积分表
            bamMapper.delIntegral();
            //修改打卡类型
            bamMapper.putSignIn(null);
            CommonUtils.setPeriod(1);
        }
    }

}
