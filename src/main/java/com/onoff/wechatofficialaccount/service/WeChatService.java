package com.onoff.wechatofficialaccount.service;

import com.onoff.wechatofficialaccount.entity.BaseMessage;
import com.onoff.wechatofficialaccount.entity.User;
import com.onoff.wechatofficialaccount.entity.VO.Connect;

import java.io.InputStream;
import java.util.Map;


public interface WeChatService {
    /**
     * 验证消息是否来自微信服务器
     *
     * @param connect
     * @return
     */
    boolean check(Connect connect);

    /**
     * 获取用户信息
     * @param openid
     * @return
     */
     User getUserInfo(String openid);

    /**
     * 暴露获取access_token
     * @return
     */
    String getAccessToken();

    /**
     * 解析xml数据包
     *
     * @param is
     * @return
     */
    Map<String, String> parseRequest(InputStream is);

    /**
     * 将对象转为xml
     * @param msg
     * @return
     */
    String beanToXml(BaseMessage msg);

    /**
     * 用于处理所有的事件和消息的回复
     * @param requestMap
     * @return 返回的是xml数据包
     */
    String getRespose(Map<String, String> requestMap);

    /**
     * 调用聊天机器人
     * @param msg
     * @return
     */
    String chat(String msg);

    /**
     * 上传临时素材
     * @param path 文件路径
     * @param type 文件类型
     * @return
     */
    String upload(String path,String type);

}
