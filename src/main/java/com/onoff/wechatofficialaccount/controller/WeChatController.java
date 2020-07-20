package com.onoff.wechatofficialaccount.controller;

import com.onoff.wechatofficialaccount.entity.VO.Connect;
import com.onoff.wechatofficialaccount.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/9 13:57
 * @VERSION 1.0
 **/
@Slf4j
@RestController
public class WeChatController {

    @Autowired
    WeChatService service;

    /**
     * 验证微信请求接口
     *
     * @param connect
     * @return
     */
    @GetMapping(value = "/wechat/check")
    public String check(Connect connect) {
        if (service.check(connect)) {
            log.info("->微信验证成功");
            return connect.getEchostr();
        } else {
            return null;
        }
    }


    /**
     * 接受消息和事件推送
     */
    @PostMapping(value = "/wechat/check")
    public void recvMessage(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf8");
        response.setCharacterEncoding("utf8");
        try {
            Map<String, String> map = service.parseRequest(request.getInputStream());
            log.info("接受消息->" + map);
            String respXml = service.getRespose(map);
            log.info("返回消息->" + respXml);
            PrintWriter out = response.getWriter();
            out.print(respXml);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
