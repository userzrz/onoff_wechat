package com.onoff.wechatofficialaccount.common;

import com.onoff.wechatofficialaccount.mapper.BAMMapper;
import com.onoff.wechatofficialaccount.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

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

    /**
     * 项目启动初始化数据period
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        int period=mapper.queryPeriod();
        CommonUtils.setPeriod(period);
    }
}