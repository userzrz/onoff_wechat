package com.onoff.wechatofficialaccount.common;

import com.onoff.wechatofficialaccount.entity.DO.Cycle;
import com.onoff.wechatofficialaccount.mapper.BAMMapper;
import com.onoff.wechatofficialaccount.mapper.DAO.BAMDao;
import com.onoff.wechatofficialaccount.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/8/15 17:18
 * @VERSION 1.0
 **/
@Slf4j
@Component
public class Initialize implements ApplicationRunner {

    @Autowired
    BAMMapper mapper;

    @Autowired
    BAMDao dao;

    /**
     * 项目启动初始化数据period
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        int period=mapper.queryPeriod();
        CommonUtils.setPeriod(period);
        //首次项目启动开启；之后关闭
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Cycle cycle=new Cycle("1",formatter.format(new Date()),"01",formatter.format(new Date()),"01");
//        dao.putCycle(cycle);
    }
}
