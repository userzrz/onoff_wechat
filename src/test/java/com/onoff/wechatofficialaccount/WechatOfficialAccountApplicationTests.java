package com.onoff.wechatofficialaccount;

import com.onoff.wechatofficialaccount.entity.TextMessage;
import com.onoff.wechatofficialaccount.entity.User;
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


    /**
     * 客服发送消息
     */
    @Test
    public void kfSend() {
        String data="{\n" +
                "    \"touser\":\""+"ol6oswhUiS-T9TeMfyM5GgrH3eGw"+"\",\n" +
                "    \"msgtype\":\"text\",\n" +
                "    \"text\":\n" +
                "    {\n" +
                "         \"content\":\"Hello World\"\n" +
                "    }\n" +
                "}";
        log.info(data);
        service.kfSendMsg(data);
    }



    /**
     * 获取用户信息
     */
    @Test
    public void testGetUserInfo() {
        String openID="ol6oswhUiS-T9TeMfyM5GgrH3eGw";
        User user= service.getUserInfo(openID);
        System.out.println(user);
    }

    /**
     * 添加临时素材
     */
    @Test
    public void testUpload() {
//        String file = "";
//        String result = service.upload(file, "image");
//        log.info(result);
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
