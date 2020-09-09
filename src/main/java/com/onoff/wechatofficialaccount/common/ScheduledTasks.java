package com.onoff.wechatofficialaccount.common;

import com.onoff.wechatofficialaccount.entity.BAM.Integral;
import com.onoff.wechatofficialaccount.entity.DO.PromotionQR;
import com.onoff.wechatofficialaccount.entity.DO.SignIn;
import com.onoff.wechatofficialaccount.entity.DO.WeekLeaderboard;
import com.onoff.wechatofficialaccount.entity.User;
import com.onoff.wechatofficialaccount.entity.VO.Leaderboard;
import com.onoff.wechatofficialaccount.mapper.BAMMapper;
import com.onoff.wechatofficialaccount.mapper.DAO.BAMDao;
import com.onoff.wechatofficialaccount.utils.CommonUtils;
import com.onoff.wechatofficialaccount.utils.MD5Utils;
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
    @Scheduled(cron = "00 59 23 *  * ?")
    public void Time() {
        log.info("========================结算周期榜");
        //将打卡未激活数据的用户进行激活
        List<SignIn> list = bamMapper.querySignInNonactivated();
        for (SignIn signIn : list) {
            Integral integral;
            User user = bamMapper.getUser_Unionid(signIn.getUnionid());
            if (user != null) {
                if (signIn.getQr_type() == 0) {
                    long time = signIn.getTime() - signIn.getQr_time();
                    if (time >= 86400000) {
                        //给用户添加打卡积分
                        integral = new Integral(user.getOpenId(), 9, 3, signIn.getTime().toString(), signIn.getPeriod());
                    } else {
                        integral = new Integral(user.getOpenId(), 18, 3, signIn.getTime().toString(), signIn.getPeriod());

                    }
                    int result = bamMapper.saveIntegral(integral);
                    if (result == 1) {
                        log.info("成功更新未激活数据：添加用户打卡记录："+signIn.toString());
                        bamMapper.putSignIn(signIn.getUnionid(),signIn.getQr_time());
                    } else {
                        log.error("更新未激活数据失败：用户打卡记录："+signIn.toString());
                    }
                } else if (signIn.getQr_type() == 1) {
                    PromotionQR promotionQR = bamDao.queryPromotionQR(signIn.getQr_time());
                    integral = new Integral(user.getOpenId(), promotionQR.getIntegral(), 4, signIn.getTime().toString(), signIn.getPeriod());
                    int result = bamMapper.saveIntegral(integral);
                    if (result == 1) {
                        log.info("成功更新未激活数据：添加用户打卡记录"+signIn.toString());
                        bamMapper.putSignIn(signIn.getUnionid(),signIn.getQr_time());
                    } else {
                        log.error("用户扫码推广码加分失败：用户记录"+signIn.toString());
                    }
                }
            }
        }
        //获取排行榜
        List<Leaderboard> array = bamMapper.getLeaderboard(CommonUtils.period);
        //排名
        CommonUtils.setRanking(array);
        WeekLeaderboard weekLeaderboard = new WeekLeaderboard();
        weekLeaderboard.setId(MD5Utils.MD5Encode(System.currentTimeMillis() + "", "utf8"));
        weekLeaderboard.setLeaderboards(array);
        int count=(int)bamDao.countCollection(CommonUtils.MONGODB_WEEK);
        weekLeaderboard.setPeriod(count+1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(new Date());
        weekLeaderboard.setDate(date);
        //获取本周参与人数
        int c = bamMapper.getParticipants(CommonUtils.period);
        weekLeaderboard.setCount(c);
        //保存周榜信息
        bamDao.saveLeaderboard(weekLeaderboard, CommonUtils.MONGODB_WEEK);
        //设置新期
        CommonUtils.setPeriod(CommonUtils.period + 1);
        //结算月榜
        if (CommonUtils.period % 4 == 1 && CommonUtils.period > 1) {
            //获取月榜排行榜
            array = bamMapper.getLeaderboard(0);
            //排名
            CommonUtils.setRanking(array);
            weekLeaderboard.setId(MD5Utils.MD5Encode(System.currentTimeMillis()+1 + "", "utf8"));
            weekLeaderboard.setLeaderboards(array);
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = formatter.format(new Date());
            weekLeaderboard.setDate(date);
            count=(int)bamDao.countCollection(CommonUtils.MONGODB_MONTH);
            weekLeaderboard.setPeriod(count+1);
            //获取本月参与人数
             c = bamMapper.getParticipants(0);
            weekLeaderboard.setCount(c);
            //保存月榜信息
            bamDao.saveLeaderboard(weekLeaderboard, CommonUtils.MONGODB_MONTH);
            //清空积分表
            bamMapper.delIntegral();
            //修改打卡类型
            bamMapper.putSignIn(null,null);
            CommonUtils.setPeriod(1);
        }
    }

}
