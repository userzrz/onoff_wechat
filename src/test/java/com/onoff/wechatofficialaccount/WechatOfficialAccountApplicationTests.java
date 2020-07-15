package com.onoff.wechatofficialaccount;

import com.onoff.wechatofficialaccount.entity.TextMessage;
import com.onoff.wechatofficialaccount.service.WeChatService;
import com.onoff.wechatofficialaccount.utils.WeChatUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
class WechatOfficialAccountApplicationTests {

    @Autowired
    WeChatService service;

    @Test
    public void test() {
        log.info(System.currentTimeMillis()+"");
    }

    @Test
    public void testGetUserInfo() {
        String user="ol6oswnPXDcyaUxvS5xhnoMfO4f8";
        String info = service.getUserInfo(user);
        System.out.println(info);
    }

    @Test
    public void testUpload() {
        String file = "C:\\Users\\lev\\Pictures\\20.7.7\\444667181938.png";
        String result = service.upload(file, "image");
        log.info(result);
    }

    /**
     * 设置行业
     */
    @Test
    public void set() {
        String at = service.getAccessToken();
        String url="https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token="+at;
        String data="{\n" +
                "          \"industry_id1\":\"34\",\n" +
                "          \"industry_id2\":\"37\"\n" +
                "       }";
        String result = WeChatUtils.post(url, data);
        System.out.println(result);
    }

    /**
     * 获取行业
     */
    @Test
    public void get() {
        String at = service.getAccessToken();
        String url="https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token="+at;
        String result = WeChatUtils.get(url);
        System.out.println(result);
    }

    @Test
    public  void testToken() {
        log.info("->"+service.getAccessToken());
    }

    @Test
    void contextLoads() {
        Map<String, String> map = new HashMap<>();
        map.put("ToUserName", "to");
        map.put("FromUserName", "from");
        map.put("MsgType", "type");
        TextMessage tm = new TextMessage(map, "还好");
        String xml=service.beanToXml(tm);
        log.info(xml);
    }

}
