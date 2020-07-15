package com.onoff.wechatofficialaccount;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@Slf4j
@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class})
public class WechatOfficialAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatOfficialAccountApplication.class, args);
        log.info("--------------------------------启动成功");

    }

}
